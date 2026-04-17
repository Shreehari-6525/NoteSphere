package website;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    // ── GET: Fetch all uploads (optionally filter by status) ──
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        String status = request.getParameter("status"); // pending / approved / rejected / all

        try {
            Connection conn = DBConnection.getConnection();

            String sql;
            PreparedStatement stmt;

            if (status == null || status.equals("all")) {
                sql  = "SELECT * FROM uploads ORDER BY uploaded_at DESC";
                stmt = conn.prepareStatement(sql);
            } else {
                sql  = "SELECT * FROM uploads WHERE status = ? ORDER BY uploaded_at DESC";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, status);
            }

            ResultSet rs = stmt.executeQuery();
            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) {
					json.append(",");
				}
                json.append("{")
                    .append("\"id\":"          ).append(rs.getInt("id")                        ).append(",")
                    .append("\"title\":\""     ).append(escape(rs.getString("title"))          ).append("\",")
                    .append("\"subject\":\""   ).append(escape(rs.getString("subject"))        ).append("\",")
                    .append("\"year\":\""      ).append(escape(rs.getString("year"))           ).append("\",")
                    .append("\"unit\":\""      ).append(escape(rs.getString("unit"))           ).append("\",")
                    .append("\"filePath\":\""  ).append(escape(rs.getString("file_path"))      ).append("\",")
                    .append("\"uploadedBy\":\""  ).append(escape(rs.getString("uploaded_by")) ).append("\",")
                    .append("\"status\":\""    ).append(escape(rs.getString("status"))         ).append("\",")
                    .append("\"uploadedAt\":\""  ).append(rs.getTimestamp("uploaded_at")      ).append("\"")
                    .append("}");
                first = false;
            }

            json.append("]");
            rs.close();
            stmt.close();
            conn.close();

            out.print(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    // ── POST: Approve or Reject an upload ──
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {



        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        String action   = request.getParameter("action"); // "approve" or "reject"
        String idParam  = request.getParameter("id");

        System.out.println("ID: " + request.getParameter("id"));
        System.out.println("ACTION: " + request.getParameter("action"));

        if (action == null || idParam == null) {
            out.print("{\"success\": false, \"message\": \"Missing parameters\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            String newStatus = action.equals("approve") ? "approved" : "rejected";

            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE uploads SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            stmt.close();
            conn.close();

            if (rows > 0) {
                out.print("{\"success\": true, \"message\": \"Upload " + newStatus + " successfully!\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Upload not found\"}");
            }



        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    // Helper: escape special chars for JSON
    private String escape(String s) {
        if (s == null) {
			return "";
		}
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
