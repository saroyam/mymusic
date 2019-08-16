package ca.sheridan.beans;

import java.sql.Date;

public class Playlist extends User {

    private int pid;
    private String pname;

    public Playlist() {
        super();
    }

    public Playlist(int uid, String pname) {
        super(uid);
        this.pname = pname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

}
