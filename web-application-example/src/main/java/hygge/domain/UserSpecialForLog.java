package hygge.domain;

import hygge.commons.template.definitions.HyggeLogInfoObject;

/**
 * @author Xavier
 * @date 2023/1/11
 */
public class UserSpecialForLog implements HyggeLogInfoObject {
    private String uid;
    private String userName;
    private String pw;

    public UserSpecialForLog() {
    }

    public UserSpecialForLog(String uid, String userName, String pw) {
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

    @Override
    public String toJsonLogInfo() {
        return String.format("{\"uid\":\"%s\",\"userName\":\"%s\"}",
                uid,
                userName);
    }
}
