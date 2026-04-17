package website;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/UploadServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1 MB
    maxFileSize       = 1024 * 1024 * 20,  // 20 MB
    maxRequestSize    = 1024 * 1024 * 25   // 25 MB
)
public class UploadServlet extends HttpServlet {

    // ⚠️ Change this to your actual upload folder path
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        try {
            // Get form fields
            String title       = request.getParameter("title");
            String subject     = request.getParameter("subject");
            String year        = request.getParameter("year");
            String unit        = request.getParameter("unit");
            String uploadedBy  = request.getParameter("uploadedBy"); // user email from Google login

            // Get uploaded file
            Part filePart = request.getPart("file");
            String fileName = extractFileName(filePart);

            if (fileName == null || fileName.isEmpty()) {
                out.print("{\"success\": false, \"message\": \"No file uploaded\"}");
                return;
            }

            // Create upload directory if not exists
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

            // Save file with unique name to avoid conflicts
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;
            String filePath = uploadPath + File.separator + uniqueFileName;
            filePart.write(filePath);

            // Save metadata to MySQL
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO uploads (title, subject, year, unit, file_path, uploaded_by, status) VALUES (?, ?, ?, ?, ?, ?, 'pending')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, subject);
            stmt.setString(3, year);
            stmt.setString(4, unit);
            stmt.setString(5, UPLOAD_DIR + "/" + uniqueFileName);
            stmt.setString(6, uploadedBy);
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            out.print("{\"success\": true, \"message\": \"File uploaded successfully! Awaiting admin approval.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Error: " + e.getMessage() + "\"}");
        }
    }

    // Extract file name from Part header
    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
