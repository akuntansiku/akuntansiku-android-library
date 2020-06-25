package id.co.akuntansiku.accounting.transaction.model;

public class DataTransactionPending {
    String code;
    String data;

    public DataTransactionPending(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
