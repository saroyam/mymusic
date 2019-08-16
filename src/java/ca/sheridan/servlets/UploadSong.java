package ca.sheridan.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import ca.sheridan.beans.Song;
import ca.sheridan.beans.User;
import ca.sheridan.dao.DAO;

/**
 * Servlet implementation class UploadSong
 */
@WebServlet("/UploadSong")
@MultipartConfig
public class UploadSong extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //String path = "C://mymusic/";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadSong() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html");
        String host = getServletContext().getInitParameter("host");
        String username = getServletContext().getInitParameter("user");
        String pass = getServletContext().getInitParameter("password");

        DAO dao = new DAO(host, username, pass);

        String path = this.getServletContext().getRealPath("/") + "/mymusic/";
        PrintWriter out = response.getWriter();
        LoginRegister lr = new LoginRegister();
        try {
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("userId");
            User loggedUser = dao.getUserDetail(user);

            String fname = loggedUser.getFullname().replaceAll("\\s+", "");

            String fileName = getFileName(request.getPart("selectSong"));

            String fileFirstName = "", fileNameTime = "";
            String extension = "";
            Part part = request.getPart("selectSong");

            int pos = fileName.lastIndexOf(".");
            if (pos > 0) {
                fileFirstName = fileName.substring(0, pos);
                extension = fileName.substring(pos + 1);
            }
            fileNameTime = fileFirstName + "." + extension;

            Song song = new Song(loggedUser.getUid(), 0, fileNameTime);
            if (uploadSong(fileName, part, fileNameTime, fname, path)) {
                if (!dao.isValidSong(fileNameTime, loggedUser.getUid())) {
                    if (dao.addSong(song)) {
                        request.setAttribute("songMsg", "New Song added to my music!!");

                    } else {
                        request.setAttribute("songMsg", "Failed to add new song!!");

                    }
                }

            } else {
                request.setAttribute("songMsg", "Song failed to upload!!");

            }
            lr.getAllSongs(fname, loggedUser.getUid(), request, dao);

            request.setAttribute("fname", fname);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("error:" + e);
            out.println("error: " + e);
        }
    }

    private String getFileName(final Part part) {
        try {
            final String partHeader = part.getHeader("content-disposition");
            //System.out.println(partHeader);
            for (String content : part.getHeader("content-disposition").split(";")) {
                if (content.trim().startsWith("filename")) {
                    return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                }
            }
        } catch (Exception e) {
            System.out.println("GetFileName: " + e);
        }
        return null;
    }

    private boolean uploadSong(String fileName, Part filePart, String fileNameTime, String fname, String path) {
        OutputStream out = null;
        InputStream filecontent = null;

        try {
            File newFile = new File(path + fname, fileNameTime);
            if (!newFile.exists()) {
                newFile.createNewFile();
                // System.out.println("File Created");
            }
            // new File(path + File.separator + fileNameTime)
            out = new FileOutputStream(newFile);
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            return true;
        } catch (FileNotFoundException fne) {
            System.out.println("try: " + fne);
            return false;
        } catch (IOException e) {

            System.out.println("Try: " + e);
        } finally {
            try {
                if (out != null) {
                    out.close();

                }
                if (filecontent != null) {

                    filecontent.close();

                }
            } catch (IOException e) {
                System.out.println("Final: " + e);
            }

        }
        return false;
    }

}
