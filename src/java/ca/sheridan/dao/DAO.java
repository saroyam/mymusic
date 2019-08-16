package ca.sheridan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ca.sheridan.beans.*;
import ca.sheridan.customTags.*;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAO {

    final String HOST;
    final String USER;
    final String PASS;
    //private final String dbURL = "jdbc:mysql://localhost/mymusic";
    //private final String dbUserName = "root";
    //private final String dbPassword = "sheridan";
    private final String tblUsers = "users";
    private final String tblPlaylist = "playlist";
    private final String tblSongs = "songs";

    public DAO(String HOST, String USER, String PASS) {
        this.HOST = HOST;
        this.USER = USER;
        this.PASS = PASS;
    }

    private Connection getDBConnection() {
        Connection con = null;
        String database = "mymusic";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(HOST + database, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            try {
                con = DriverManager.getConnection(HOST, USER, PASS);
                Statement st = con.createStatement();

                st.executeUpdate("CREATE DATABASE " + database);
                con = DriverManager.getConnection(HOST + database, USER, PASS);

            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return con;
    }

    public void createTables() {
        Connection con = getDBConnection();
        try {
            Statement st1 = con.createStatement();
            Statement st2 = con.createStatement();
            Statement st3 = con.createStatement();
            String users = "CREATE TABLE IF NOT EXISTS users(\n"
                    + "	`UID`			INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "    `FULLNAME` 		VARCHAR(255) NOT NULL,\n"
                    + "    `EMAIL`			VARCHAR(255) NOT NULL,\n"
                    + "    `PHONENUMBER`	VARCHAR(20) NOT NULL,\n"
                    + "    `PASSWORD`		VARCHAR(100) NOT NULL\n"
                    + ")";
            String songs = "CREATE TABLE IF NOT EXISTS songs(\n"
                    + "	`SID`			INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "    `UID`			INT,\n"
                    + "    `PID`			INT,\n"
                    + "    `SNAME` 		VARCHAR(255) NOT NULL\n"
                    + "    \n"
                    + ")";
            String playlist = "CREATE TABLE IF NOT EXISTS playlist(\n"
                    + "	`PID`			INT PRIMARY KEY AUTO_INCREMENT,\n"
                    + "    `UID`			INT,\n"
                    + "    `PNAME` 		VARCHAR(255) NOT NULL\n"
                    + "    \n"
                    + ");";
            st1.executeUpdate(users);
            st2.executeUpdate(songs);
            st3.executeUpdate(playlist);
        } catch (SQLException e) {

        }
    }

    // Validate user if exists
    public boolean isValidUser(String userId) throws ClassNotFoundException, SQLException {
        Connection dbConnection;
        createTables();
        dbConnection = getDBConnection();

        ResultSet resultSet;
        String sql = "SELECT count(*) FROM " + tblUsers + " WHERE EMAIL = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, userId);
        resultSet = ps.executeQuery();

        int userCount = 0;

        while (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }

        resultSet.close();
        dbConnection.close();

        if (userCount == 0) {
            return false;
        }

        return true;
    }

    // Authenticate User with userId and password
    public boolean isUserAuthenticated(User userModel) throws ClassNotFoundException, SQLException {
        Connection dbConnection = getDBConnection();
        
        ResultSet resultSet;
        String sql = "Select count(*) from " + tblUsers + " where EMAIL = ? and PASSWORD = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, userModel.getEmail());
        ps.setString(2, userModel.getPassword());
        resultSet = ps.executeQuery();

        int userCount = 0;

        while (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }

        resultSet.close();
        dbConnection.close();

        if (userCount == 0) {
            return false;
        }

        return true;
    }

    public User getUserDetail(String email) throws ClassNotFoundException, SQLException {
        User user = null;
        Connection dbConnection = getDBConnection();
        ResultSet rs;
        String sql = "Select * from " + tblUsers + " where EMAIL = ? ";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, email);

        rs = ps.executeQuery();

        while (rs.next()) {
            user = new User(rs.getInt("UID"), rs.getString("FULLNAME"), rs.getString("EMAIL"),
                    rs.getString("PHONENUMBER"), rs.getString("PASSWORD"));

        }

        rs.close();
        dbConnection.close();

        return user;
    }

    public boolean insertUser(User userModel) {
        boolean isOkay = false;
        Connection dbConnection;
        try {
            dbConnection = getDBConnection();
            createTables();
            String sql = "INSERT INTO " + tblUsers + " (FULLNAME,EMAIL,PHONENUMBER,PASSWORD) VALUES (?,?,?,?)";
            PreparedStatement ps = dbConnection.prepareStatement(sql);
            ps.setString(1, userModel.getFullname());
            ps.setString(2, userModel.getEmail());
            ps.setString(3, userModel.getPhone());
            ps.setString(4, userModel.getPassword());

            int rowsUpdated = ps.executeUpdate();
            isOkay = rowsUpdated > 0;

            dbConnection.close();
        } catch (SQLException e) {
             
        }
        return isOkay;
    }

    public boolean addPlaylist(Playlist playlist) throws ClassNotFoundException, SQLException {
        boolean isOkay = false;
        Connection dbConnection;

        dbConnection = getDBConnection();
        String sql = "INSERT INTO " + tblPlaylist + " (UID,PNAME) VALUES (?,?)";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, playlist.getUid());
        ps.setString(2, playlist.getPname());

        int rowsUpdated = ps.executeUpdate();
        isOkay = rowsUpdated > 0;

        dbConnection.close();

        return isOkay;
    }

    public boolean addSong(Song song) throws ClassNotFoundException, SQLException {
        boolean isOkay = false;
        Connection dbConnection;

        dbConnection = getDBConnection();
        String sql = "INSERT INTO " + tblSongs + " (UID,PID,SNAME) VALUES (?,?,?)";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, song.getUid());
        ps.setInt(2, song.getPid());
        ps.setString(3, song.getSname());

        int rowsUpdated = ps.executeUpdate();
        isOkay = rowsUpdated > 0;

        dbConnection.close();

        return isOkay;
    }

    public boolean addToPlayList(int pid, int sid) throws ClassNotFoundException, SQLException {
        boolean isOkay = false;
        Connection dbConnection;

        dbConnection = getDBConnection();
        String sql = "UPDATE " + tblSongs + " SET PID = ? WHERE SID = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, pid);
        ps.setInt(2, sid);

        int rowsUpdated = ps.executeUpdate();
        isOkay = rowsUpdated > 0;

        dbConnection.close();

        return isOkay;
    }

    public boolean isValidPlaylist(String name, int uid) throws ClassNotFoundException, SQLException {
        Connection dbConnection;

        dbConnection = getDBConnection();

        ResultSet resultSet;
        String sql = "SELECT count(*) FROM " + tblPlaylist + " WHERE PNAME = ? AND UID = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, uid);
        resultSet = ps.executeQuery();

        int userCount = 0;

        while (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }

        resultSet.close();
        dbConnection.close();

        if (userCount == 0) {
            return false;
        }

        return true;
    }

    public boolean isValidSong(String name, int uid) throws ClassNotFoundException, SQLException {
        Connection dbConnection;

        dbConnection = getDBConnection();

        ResultSet resultSet;
        String sql = "SELECT count(*) FROM " + tblSongs + " WHERE SNAME = ? AND UID = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2, uid);
        resultSet = ps.executeQuery();

        int userCount = 0;

        while (resultSet.next()) {
            userCount = resultSet.getInt(1);
        }

        resultSet.close();
        dbConnection.close();

        if (userCount == 0) {
            return false;
        }

        return true;
    }

    public List<Playlist> getAllPlaylistByUser(int userId) throws ClassNotFoundException, SQLException {
        List<Playlist> playlist = new ArrayList<Playlist>();
        Playlist pModel = null;
        Connection dbConnection = getDBConnection();
        ResultSet rs;
        String sql = "Select * from " + tblPlaylist + " where UID = ? ";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, userId);

        rs = ps.executeQuery();

        while (rs.next()) {
            pModel = new Playlist();
            pModel.setPid(rs.getInt("PID"));
            pModel.setPname(rs.getString("PNAME"));
            playlist.add(pModel);
        }

        rs.close();
        dbConnection.close();

        return playlist;
    }

    public List<Song> getAllSongByPlaylistAndUser(int userId, int pid) throws ClassNotFoundException, SQLException {
        List<Song> songlist = new ArrayList<Song>();
        Song sModel = null;
        Connection dbConnection = getDBConnection();
        ResultSet rs;
        String sql = "Select * from " + tblSongs + " where UID = ? AND PID = ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, pid);
        rs = ps.executeQuery();

        while (rs.next()) {
            sModel = new Song();
            sModel.setSname(rs.getString("SNAME"));
            songlist.add(sModel);
        }

        rs.close();
        dbConnection.close();

        return songlist;
    }

    public List<Song> getAllSongByPlaylistAndUserToAdd(int userId, int pid)
            throws ClassNotFoundException, SQLException {
        List<Song> songlist = new ArrayList<Song>();
        Song sModel = null;
        Connection dbConnection = getDBConnection();
        ResultSet rs;
        String sql = "Select * from " + tblSongs + " where UID = ? AND PID != ?";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setInt(1, userId);
        ps.setInt(2, pid);
        rs = ps.executeQuery();

        while (rs.next()) {
            sModel = new Song();
            sModel.setSname(rs.getString("SNAME"));
            sModel.setSid(rs.getInt("SID"));
            songlist.add(sModel);
        }

        rs.close();
        dbConnection.close();

        return songlist;
    }

    public List<Song> searchMusic(String music)
            throws ClassNotFoundException, SQLException {
        List<Song> songlist = new ArrayList<Song>();
        Song sModel = null;
        Connection dbConnection = getDBConnection();
        ResultSet rs;
        String sql = "Select * from " + tblSongs + " WHERE SNAME LIKE (?)";
        PreparedStatement ps = dbConnection.prepareStatement(sql);
        ps.setString(1, "%" + music + "%");

        rs = ps.executeQuery();

        while (rs.next()) {
            sModel = new Song();
            sModel.setSname(rs.getString("SNAME"));
            sModel.setSid(rs.getInt("SID"));
            songlist.add(sModel);
        }

        rs.close();
        dbConnection.close();

        return songlist;
    }

}
