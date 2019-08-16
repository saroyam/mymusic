var audio;
var x;
var old;

function playMusic(music, i) {
    if (i != x) {
        audio = null;

    }
    if (audio == null) {
        x = i;
        old = music;
        audio = new Audio(old);
    }

    var playing = false;

    if ($("#btn" + i).hasClass('fa-play')) {
        $("#btn" + i).removeClass('fa-play');
        $("#btn" + i).addClass('fa-pause');

        playing = true;

    } else {
        $("#btn" + i).removeClass('fa-pause');
        $("#btn" + i).addClass('fa-play');
        playing = false;

    }


    if (playing) {
        audio.play();
    } else {
        audio.pause();
    }

}
$(document).ready(function () {
    $(".playlistRow").click(function () {
        var pid = $(this).attr("pid");
        var pname = $(this).attr("pname");
        var fname = $(this).attr("fname");
        //var path = $(this).attr("pth");
        $(".playlistName").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + pname + " Playlist");
        $(".add-to-playlist").html("<form action='OpenPlaylist' method='POST'><table><input type='hidden' name='spid' value='" + pid + "' /><input type='hidden' name='action' value='AddToPlaylist' /><tr><td><label>Select Song: </label></td><td><select name='select-song' class=\"select-song\"></select></td><td> <input type='submit' class='astextBorder' name='addtoplaylist' value='Add to Playlist' /></td></tr></table></form><br><hr>");

        $.ajax({
            type: 'POST',
            url: 'OpenPlaylist',
            data: {pid: pid, pname: pname, action: "FetchSongs"},
            success: function (response) {
                var jsonObject = $.parseJSON(response);
                if (response != null) {
                    $("#playlistSong").html("");

                    var j = 0;

                    var path = "http://localhost:8080/Final_Assignment/mymusic/" + fname + "/";
                   // var path = "file:///C:/mymusic/" +fname +"/";

                    $.each(jsonObject.addSongList, function (key, value) {
                        if (jsonObject.addSongList.length == 0) {

                        } else {

                            $(".select-song").append("<option value='" + value['sid'] + "'>" + value['sname'] + "</option>");
                        }

                    });

                    $.each(jsonObject.songList, function (key, value) {
                        if (jsonObject.songList.length == 0) {
                            $(".playMsg").text("No Songs");
                        } else {


                            $("#playlistSong").append("<tr><td>" + value['sname'] + "</td><td><a class=\"play-pause-button\" id='play" + j
                                    + "' onClick=\"playMusicList('" + path + value['sname'] + "'," + j + ")\" ><i id='btnx" + j
                                    + "' class=\"fa fa-play\"></i></a></td></tr>");

                        }
                        j++;
                    });
                }
            }

        });
    });
});
var audio1;
var x1;
var old1;

function playMusicList(music, i) {
    if (i != x1) {
        audio1 = null;

    }
    if (audio1 == null) {
        x1 = i;
        old1 = music;
        audio1 = new Audio(old1);
    }

    var playing = false;

    if ($("#btnx" + i).hasClass('fa-play')) {
        $("#btnx" + i).removeClass('fa-play');
        $("#btnx" + i).addClass('fa-pause');

        playing = true;

    } else {
        $("#btnx" + i).removeClass('fa-pause');
        $("#btnx" + i).addClass('fa-play');
        playing = false;

    }


    if (playing) {
        audio1.play();
    } else {
        audio1.pause();
    }

}
