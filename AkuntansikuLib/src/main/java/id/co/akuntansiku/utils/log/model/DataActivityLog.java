package id.co.akuntansiku.utils.log.model;

public class DataActivityLog {
    String code;
    int status;
    String note;
    String user_email;
    String data;
    String created_at;

    public DataActivityLog(String code, int status, String note, String user_email, String data, String created_at) {
        this.code = code;
        this.status = status;
        this.note = note;
        this.user_email = user_email;
        this.data = data;
        this.created_at = created_at;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
