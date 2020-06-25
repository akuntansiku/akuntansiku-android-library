package id.co.akuntansiku.setting.company.model;

public class DataCompany {
    int id_;
    int id;
    String name;
    String sync;
    String address;
    String list_user;
    int id_user;
    String currency;
    int time_zone_id;
    int time_zone_value;
    String last_usage;
    String created_at;
    String updated_at;

    public DataCompany(int id_, int id, String name, String sync, String address, String list_user, int id_user, String currency, int time_zone_id, int time_zone_value, String last_usage, String created_at, String updated_at) {
        this.id_ = id_;
        this.id = id;
        this.name = name;
        this.sync = sync;
        this.address = address;
        this.list_user = list_user;
        this.id_user = id_user;
        this.currency = currency;
        this.time_zone_id = time_zone_id;
        this.time_zone_value = time_zone_value;
        this.last_usage = last_usage;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getList_user() {
        return list_user;
    }

    public void setList_user(String list_user) {
        this.list_user = list_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getTime_zone_id() {
        return time_zone_id;
    }

    public void setTime_zone_id(int time_zone_id) {
        this.time_zone_id = time_zone_id;
    }

    public int getTime_zone_value() {
        return time_zone_value;
    }

    public void setTime_zone_value(int time_zone_value) {
        this.time_zone_value = time_zone_value;
    }

    public String getLast_usage() {
        return last_usage;
    }

    public void setLast_usage(String last_usage) {
        this.last_usage = last_usage;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
