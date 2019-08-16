package ca.sheridan.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.sheridan.beans.*;
import ca.sheridan.dao.DAO;

/**
 * Servlet implementation class AddPlaylist
 */
@WebServlet("/AddPlaylist")
public class AddPlaylist extends HttpServlet {

    private static final long serialVersionUID = 1L;

    //String path = "C://mymusic";
    /**
     * l
     *
     * @see HttpServlet#HttpServlet()
     */
    public AddPlaylist() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginRegister lr = new LoginRegister();
        String host = getServletContext().getInitParameter("host");
        String username = getServletContext().getInitParameter("user");
        String pass = getServletContext().getInitParameter("password");

        DAO dao = new DAO(host, username, pass);
        String path = this.getServletContext().getRealPath("/") + "/mymusic";
        try {
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("userId");
            User loggedUser = dao.getUserDetail(user);

            String fname = loggedUser.getFullname().replaceAll("\\s+", "");
            String playlistName = request.getParameter("playlistName");

            Playlist playlist = new Playlist(loggedUser.getUid(), playlistName);

            if (dao.isValidPlaylist(playlistName, loggedUser.getUid())) {
                request.setAttribute("msg", "Playlist already exists!! Please try different name");
            } else {
                if (dao.addPlaylist(playlist)) {
//					createFolder(fname,playlistName,path);
                    request.setAttribute("msg", "Playlist added successfully!!");
                } else {
                    request.setAttribute("msg", "Error while adding playlist");
                }
            }

            lr.getAllSongs(fname, loggedUser.getUid(), request, dao);
            request.setAttribute("fname", fname);

            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

        } catch (Exception e) {

        }
    }

    protected boolean createFolder(String name, String playlist, String path) {
        boolean result = false;
        File theDir = new File(path + File.separator + name + File.separator + playlist);
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                System.out.println("File creation: " + se);
                result = false;
            }
            if (result) {
            }
        } else {
            result = false;
        }
        return result;
    }

}
