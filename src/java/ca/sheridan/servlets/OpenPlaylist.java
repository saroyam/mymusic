package ca.sheridan.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import ca.sheridan.beans.*;
import ca.sheridan.dao.DAO;

/**
 * Servlet implementation class OpenPlaylist
 */
@WebServlet("/OpenPlaylist")
public class OpenPlaylist extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public OpenPlaylist() {
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
        String host = getServletContext().getInitParameter("host");
        String username = getServletContext().getInitParameter("user");
        String pass = getServletContext().getInitParameter("password");

        DAO dao = new DAO(host, username, pass);
        try {
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("userId");
            User loggedUser = dao.getUserDetail(user);
            String action = request.getParameter("action");
            String fname = loggedUser.getFullname().replaceAll("\\s+", "");
            LoginRegister lr = new LoginRegister();

            if (action.equalsIgnoreCase("FetchSongs")) {
                String pname = request.getParameter("pname");
                int pid = Integer.parseInt(request.getParameter("pid"));
                List<Song> songList = dao.getAllSongByPlaylistAndUser(loggedUser.getUid(), pid);
                List<Song> addSongList = dao.getAllSongByPlaylistAndUserToAdd(loggedUser.getUid(), pid);
                Gson gson = new Gson();
                JsonObject myObj = new JsonObject();
                JsonElement songElement = gson.toJsonTree(songList, new TypeToken<List<Song>>() {
                }.getType());
                JsonElement addSongElement = gson.toJsonTree(addSongList, new TypeToken<List<Song>>() {
                }.getType());

                myObj.add("songList", songElement);
                myObj.add("addSongList", addSongElement);

                response.getWriter().println(myObj.toString());
//				System.out.println("AA: "+myObj.toString());
                response.getWriter().close();

            } else if (action.equalsIgnoreCase("AddToPlaylist")) {

                int spid = Integer.parseInt(request.getParameter("spid"));
                int sid = Integer.parseInt(request.getParameter("select-song"));

                if (dao.addToPlayList(spid, sid)) {

                    request.setAttribute("myplaylist", "Song added to playlist");
                } else {
                    request.setAttribute("myplaylist", "Unable to add Song to playlist");
                }
                lr.getAllSongs(fname, loggedUser.getUid(), request, dao);
                request.setAttribute("fname", fname);

                request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
            } else if (action.equalsIgnoreCase("SearchMusic")) {
                List<Song> songlist = new ArrayList<Song>();
                String music = request.getParameter("search-song");
                if (music.isEmpty()) {
                    lr.getAllSongs(fname, loggedUser.getUid(), request, dao);
                } else {
                    songlist = dao.searchMusic(music);
                    request.setAttribute("songs", songlist);
                    List<Playlist> playlist = dao.getAllPlaylistByUser(loggedUser.getUid());
                    request.setAttribute("playlist", playlist);
                }

                request.setAttribute("fname", fname);

                request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
            } else {
                lr.getAllSongs(fname, loggedUser.getUid(), request, dao);
                request.setAttribute("fname", fname);
                request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            response.getWriter().println("Error: " + e);
        }
    }

}
