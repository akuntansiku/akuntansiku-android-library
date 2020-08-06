package id.co.akuntansiku.accounting.transaction.model;

public class DataTransactionPending {
    String code;
    int status;
    String user_email;
    String data;

    public DataTransactionPending(String code, int status, String user_email, String data) {
        this.code = code;
        this.status = status;
        this.user_email = user_email;
        this.data = data;
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
}
