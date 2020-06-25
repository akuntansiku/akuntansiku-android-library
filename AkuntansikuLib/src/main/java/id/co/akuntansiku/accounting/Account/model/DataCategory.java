package id.co.akuntansiku.accounting.Account.model;

public class DataCategory {
    int id_;
    int id;
    String name;
    int type;

    public DataCategory(int id_, int id, String name, int type) {
        this.id_ = id_;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
