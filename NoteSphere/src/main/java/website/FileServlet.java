package website;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String fileName = request.getPathInfo(); // /file.pdf

        if (fileName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String fullPath = getServletContext().getRealPath("")
                + File.separator + UPLOAD_DIR
                + File.separator + fileName.substring(1);

        File file = new File(fullPath);

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();
    }
}