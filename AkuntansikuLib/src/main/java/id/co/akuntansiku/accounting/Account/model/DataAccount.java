package id.co.akuntansiku.accounting.Account.model;

public class DataAccount {
    String name;
    String code;
    int status;
    int type;
    int id_category;
    String description;
    int archived;

    public DataAccount(){}

    public DataAccount(String name, String code, int status, int type, int id_category, String description, int archived) {
        this.name = name;
        this.code = code;
        this.status = status;
        this.type = type;
        this.id_category = id_category;
        this.description = description;
        this.archived = archived;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }
}
