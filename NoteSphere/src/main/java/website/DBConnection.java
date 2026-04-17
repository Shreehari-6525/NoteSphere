package website;

import java.io.IOException;
import java.io.PrintWriter;
// MySQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DBConnection")
public class DBConnection extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private static final String URL = "jdbc:mysql://localhost:3307/NoteSphere";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "12345";


    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!", e);
        }
    }


    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = getConnection();

            if (conn != null) {
                out.println("<h2>✅ Database Connected Successfully!</h2>");
                conn.close();
            } else {
                out.println("<h2>❌ Connection Failed!</h2>");
            }

        } catch (SQLException e) {
            out.println("<h2>❌ Error: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }

      @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}