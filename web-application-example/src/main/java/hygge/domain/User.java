package hygge.domain;

/**
 * @author Xavier
 * @date 2023/1/11
 */
public class User {
    private String uid;
    private String userName;
    private String pw;

    public User() {
    }

    public User(String uid, String userName, String pw) {
        this.uid = uid;
        this.userName = userName;
        this.pw = pw;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
