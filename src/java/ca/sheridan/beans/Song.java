package ca.sheridan.beans;

import java.sql.Date;

public class Song extends Playlist {

    private int sid;
    private String sname;

    public Song() {
        super();
    }

    public Song(int uid, int pid, String sname) {
        super.setUid(uid);
        super.setPid(pid);
        this.sname = sname;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

}
