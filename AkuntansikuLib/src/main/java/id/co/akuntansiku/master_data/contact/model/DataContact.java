package id.co.akuntansiku.master_data.contact.model;

public class DataContact {
    int id_;
    int id;
    int id_user;
    String name;
    String code;
    String email;
    String no_hp;
    String note;
    String address;

    public DataContact() {}

    public DataContact(String name, String email, String no_hp, String address) {
        this.name = name;
        this.email = email;
        this.no_hp = no_hp;
        this.address = address;
    }

    public DataContact(int id_, int id, int id_user, String name, String code, String email, String no_hp, String note, String address) {
        this.id_ = id_;
        this.id = id;
        this.id_user = id_user;
        this.name = name;
        this.code = code;
        this.email = email;
        this.no_hp = no_hp;
        this.note = note;
        this.address = address;
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

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
