package id.co.akuntansiku.accounting.account.model;

/**
 * Created by diditsepiyanto on 11/10/16.
 */

public class DataNotifikasi {
    private int id;
    private String notifikasi;

    public DataNotifikasi(int id, String notifikasi) {
        this.id = id;
        this.notifikasi = notifikasi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotifikasi() {
        return notifikasi;
    }

    public void setNotifikasi(String notifikasi) {
        this.notifikasi = notifikasi;
    }
}
