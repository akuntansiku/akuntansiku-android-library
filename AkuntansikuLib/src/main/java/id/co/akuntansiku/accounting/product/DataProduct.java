package id.co.akuntansiku.accounting.product;

public class DataProduct {
    String code;
    String name;
    String category;
    double quantity;
    double discount;
    double weight;
    String unit;
    String note;
    double subtotal;
    int type;

    public DataProduct(String code, String name, String category, double quantity, double discount, double weight, String unit, String note, double subtotal, int type) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.discount = discount;
        this.weight = weight;
        this.unit = unit;
        this.note = note;
        this.subtotal = subtotal;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
