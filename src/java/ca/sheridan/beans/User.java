package ca.sheridan.beans;

public class User {

    private int uid;
    private String fullname;
    private String email;
    private String phone;
    private String password;

    public User() {
        super();
    }

    public User(int uid) {
        super();
        this.uid = uid;
    }

    public User(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public User(int uid, String fullname, String email, String phone, String password) {
        super();
        this.uid = uid;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(String fullname, String email, String phone, String password) {
        super();
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
