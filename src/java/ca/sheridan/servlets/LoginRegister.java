package ca.sheridan.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.sheridan.beans.*;
import ca.sheridan.dao.DAO;

/**
 * Servlet implementation class LoginRegister
 */
@WebServlet("/LoginRegister")
public class LoginRegister extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginRegister() {
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
        String host = getServletContext().getInitParameter("host");
        String username = getServletContext().getInitParameter("user");
        String pass = getServletContext().getInitParameter("password");

        DAO dao = new DAO(host, username, pass);
        String action = request.getParameter("action");
        String path = this.getServletContext().getRealPath("/") + "/mymusic/";
        PrintWriter pw = response.getWriter();
        if (action.equalsIgnoreCase("redirectRegister")) {
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("redirectLogin")) {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else if (action.equalsIgnoreCase("login")) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            User user = new User(email, password);

            try {
                if (dao.isValidUser(user.getEmail())) {
                    if (dao.isUserAuthenticated(user)) {
                        HttpSession session = request.getSession(true);
                        session.setAttribute("userId", user.getEmail());
                        User loggedUser = dao.getUserDetail(email);

                        String fname = loggedUser.getFullname().replaceAll("\\s+", "");

                        getAllSongs(fname, loggedUser.getUid(), request, dao);

                        request.setAttribute("fname", fname);

                        request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

                    } else {
                        request.setAttribute("msg", "Invalid credentials! Wrong Username or password");

                        request.getRequestDispatcher("/index.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("msg", "Username does not exists");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } catch (ClassNotFoundException | SQLException e) {
                pw.println("Error: " + e);
            }

        } else if (action.equalsIgnoreCase("register")) {
            String email = request.getParameter("email");
            String fullname = request.getParameter("fullname");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String cpassword = request.getParameter("cpassword");

            if (password.equals(cpassword)) {
                User user = new User(fullname, email, phone, password);

                try {
                    if (dao.isValidUser(user.getEmail())) {
                        request.setAttribute("msg", "User already exists!! Please try different Email");
                        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                    } else {
                        if (dao.insertUser(user)) {
                            String fname = fullname.replaceAll("\\s+", "");
                            createUserFolder(fname, path);
                            request.setAttribute("msg", "User successfully registered!<br> Please login to get access to Music");
                            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                        } else {
                            request.setAttribute("msg", "Unable to add new user");
                            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
                        }
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    pw.println("Error: " + e);
                }
            } else {
                request.setAttribute("msg", "Passwords doesnot match!!");
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            }

        }

    }

    protected boolean createUserFolder(String name, String path) {
        boolean result = false;
        File theDir = new File(path, name);
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

    public void getAllSongs(String fname, int uid, HttpServletRequest request, DAO dao) {
        try {

            List<Song> list = dao.getAllSongByPlaylistAndUserToAdd(uid, -1);
            if (list.size() > 0) {
                request.setAttribute("songs", list);
            } else {
                request.setAttribute("songs", list);
            }

            List<Playlist> playlist = dao.getAllPlaylistByUser(uid);

            request.setAttribute("playlist", playlist);

        } catch (Exception e) {

        }
    }

}
