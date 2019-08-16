<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/MyCustomTag.tld" prefix="mine"%>
<%@ page import="ca.sheridan.beans.*,java.util.*,java.lang.*,java.io.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/creative.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>My Music</title>
    </head>
    <body>
    <section id="nav">
        <ul>
            <li><img src="images/logo.jpg" alt="My music Photo" width="100%"
                     height="50%"></li>

            <li><a href="#my-songs">My Songs</a></li>
            <li><a href="#my-playlist">My PlayList</a></li>
            <li><a href="#add-playlist">Add PlayList</a></li>
            <li><a href="Logout">Logout</a></li>
            <li><h4 align="center">Welcome: &nbsp;${requestScope.fname }</h4></li>
        </ul>
    </section>

    <div style="margin-left: 250px; padding: 1px 16px; height: 1000px;">

        <div id="my-songs" style="background: #fff; opacity: 0.9">
            <br>
            <form action="OpenPlaylist" method="POST">
                <input type="hidden" name="action" value="SearchMusic" />
                <table >
                    <tr>
                        <td><h2>My Songs</h2></td>
                        <td><label>Search Song:</label></td>
                        <td> <input type="text" name="search-song"  /></td>
                        <td> <input type="submit" name="searchSubmit" class="astextBorder" value="Search" /></td>
                    </tr>
                </table>
            </form>


            <form action="UploadSong" method="POST" enctype="multipart/form-data">
                <mine:myTag msg="${requestScope.songMsg }" />
                <table>
                    <tr>
                        <td><h3>Add New Song:</h3></td>
                        <td> <input type="file" name="selectSong" required /></td>
                        <td> <input type="submit" name="addSong" class="astextBorder" value="Add Song" /></td>
                    </tr>
                </table>
            </form>

            <br>
            <table>

                <%
                    String fname = (String) request.getAttribute("fname");
                    String path = "http://localhost:8080/Final_Assignment/mymusic/" + fname + "/";
                    // String path =  this.getServletContext().getRealPath("/") + "/mymusic/"+ fname + "/";

                    ArrayList<Song> listOfFiles = (ArrayList<Song>) request.getAttribute("songs");
                    int i = 0;
                    if (listOfFiles.size() > 0) {
                        for (Song song : listOfFiles) {
                            out.println("<tr><td>" + song.getSname() + "</td><td><a class=\"play-pause-button\" id='play" + i
                                    + "' onClick=\"playMusic('" + path + song.getSname() + "'," + i + ")\" ><i id='btn" + i
                                    + "' class=\"fa fa-play\"></i></a></td></tr>");

                            i++;
                        }
                    } else {
                        out.println("<tr  style=\"padding:8px;border-radius:5px;\"><td colspan='2'>No Songs in Application</td></tr>");
                    }
                %>
            </table>
        </div>

        <div id="my-playlist" style="background: #fff; opacity: 0.9">
            <br>
            <h2>My Playlist</h2>
            <hr>
            <br>
            <div id="playMain">
                <c:if test="${ empty( playlist ) }">
                    <h2>No Playlist created</h2>
                </c:if>
                <div id="playLeft">

                    <c:if test="${! empty( playlist ) }">
                        <table>

                            <c:forEach var="playlist" items="${playlist}">

                                <tr pid="${playlist.pid }" pname="${playlist.pname }" fname="${requestScope.fname }"
                                    class="playlistRow" pth ="<%=path%>" >
                                    <td>${playlist.pname }</td>
                                </tr>

                            </c:forEach>
                        </table>
                    </c:if>
                </div>
                <div id="playRight">
                    <h2 class="playlistName"></h2><hr>
                    <h2 class="playMsg"></h2>
                    <section class="add-to-playlist"></section>
                    <table id="playlistSong">
                    </table>
                </div>
            </div>
        </div>

        <div id="add-playlist">
            <br>
            <h2>Add Playlist</h2>
            <hr>
            <br>
            <form action="AddPlaylist" method="POST">
                <table
                    style="width: 500px; margin: auto; border: 5px solid gray; border-radius: 5px; padding: 20px; background: #fff;">
                    <tr>
                        <td colspan="2"><mine:myTag msg="${requestScope.msg }" /></td>
                    </tr>
                    <tr>
                        <td><h3>Playlist Name:</h3></td>
                        <td><input type="text" name="playlistName" required /></td>
                    </tr>
                    <tr>
                        <td colspan="2"><input id="addPlaylist" class="myBtn"
                                               name="addPlaylist" type="submit" value="Add Playlist" /></td>
                    </tr>
                </table>
            </form>
        </div>
    </div>


    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>


    <script src="js/creative.js"></script>

</body>
</html>