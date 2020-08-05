package id.co.akuntansiku.accounting.transaction.model;

public class DataTransactionPending {
    String code;
    String user_email;
    String data;

    public DataTransactionPending(String code, String user_email, String data) {
        this.code = code;
        this.user_email = user_email;
        this.data = data;
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
