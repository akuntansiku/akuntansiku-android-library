package id.co.akuntansiku.accounting.transaction.model;

public class DataTransactionMode {
    int id;
    String name;
    String debit_hint;
    String credit_hint;
    Integer[] debit;
    Integer[] credit;

    public String getDebit_hint() {
        return debit_hint;
    }

    public void setDebit_hint(String debit_hint) {
        this.debit_hint = debit_hint;
    }

    public String getCredit_hint() {
        return credit_hint;
    }

    public void setCredit_hint(String credit_hint) {
        this.credit_hint = credit_hint;
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

    public Integer[] getDebit() {
        return debit;
    }

    public void setDebit(Integer[] debit) {
        this.debit = debit;
    }

    public Integer[] getCredit() {
        return credit;
    }

    public void setCredit(Integer[] credit) {
        this.credit = credit;
    }
}
