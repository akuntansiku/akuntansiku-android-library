package id.co.akuntansiku.utils.model;

public class DataDateFilter {
    int id;
    String filter_name;
    String first_date;
    String last_date;

    public DataDateFilter(int id, String filter_name, String first_date, String last_date) {
        this.id = id;
        this.filter_name = filter_name;
        this.first_date = first_date;
        this.last_date = last_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilter_name() {
        return filter_name;
    }

    public void setFilter_name(String filter_name) {
        this.filter_name = filter_name;
    }

    public String getFirst_date() {
        return first_date;
    }

    public void setFirst_date(String first_date) {
        this.first_date = first_date;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }
}
