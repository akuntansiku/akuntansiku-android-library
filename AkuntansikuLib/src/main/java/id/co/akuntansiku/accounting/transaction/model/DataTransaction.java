package id.co.akuntansiku.accounting.transaction.model;

import java.util.List;

import id.co.akuntansiku.accounting.product.DataProduct;

public class DataTransaction {
    int id_;
    String code;
    int mode;
    String note;
    int maker_id;
    int contact_id;
    String attachment;
    String created_at;
    String app_source;
    String payment_method;
    String tag;
    String cost_number;
    boolean is_draft;
    String parent_code;
    String due_date;
    List<Journal> journal;
    int invoice_status;
    List<DataProduct> products;
    Delivery delivery;
    String recipient_address;

    public DataTransaction() {
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMaker_id() {
        return maker_id;
    }

    public void setMaker_id(int maker_id) {
        this.maker_id = maker_id;
    }

    public int getContact_id() {
        return contact_id;
    }

    public void setContact_id(int contact_id) {
        this.contact_id = contact_id;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getApp_source() {
        return app_source;
    }

    public void setApp_source(String app_source) {
        this.app_source = app_source;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCost_number() {
        return cost_number;
    }

    public void setCost_number(String cost_number) {
        this.cost_number = cost_number;
    }

    public boolean isIs_draft() {
        return is_draft;
    }

    public void setIs_draft(boolean is_draft) {
        this.is_draft = is_draft;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }

    public List<Journal> getJournal() {
        return journal;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setJournal(List<Journal> journal) {
        this.journal = journal;
    }

    public int getInvoice_status() {
        return invoice_status;
    }

    public void setInvoice_status(int invoice_status) {
        this.invoice_status = invoice_status;
    }

    public List<DataProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DataProduct> products) {
        this.products = products;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public String getRecipient_address() {
        return recipient_address;
    }

    public void setRecipient_address(String recipient_address) {
        this.recipient_address = recipient_address;
    }

    public static class Delivery{
        String courier_name;
        String receipt_number;

        public Delivery() {
        }

        public Delivery(String courier_name, String receipt_number) {
            this.courier_name = courier_name;
            this.receipt_number = receipt_number;
        }

        public String getCourier_name() {
            return courier_name;
        }

        public void setCourier_name(String courier_name) {
            this.courier_name = courier_name;
        }

        public String getReceipt_number() {
            return receipt_number;
        }

        public void setReceipt_number(String receipt_number) {
            this.receipt_number = receipt_number;
        }
    }

    public static class Journal{
        int id;
        String code;
        String name;
        Double debit;
        Double credit;
        int type;
        int id_category;
        String currency_format;
        String note;

        public Journal(String code, Double debit, Double credit) {
            this.code = code;
            this.debit = debit;
            this.credit = credit;
        }

        public Journal(int id, String code, String name, Double debit, Double credit, int type, int id_category, String currency_format, String note) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.debit = debit;
            this.credit = credit;
            this.type = type;
            this.id_category = id_category;
            this.currency_format = currency_format;
            this.note = note;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public Double getDebit() {
            return debit;
        }

        public void setDebit(Double debit) {
            this.debit = debit;
        }

        public Double getCredit() {
            return credit;
        }

        public void setCredit(Double credit) {
            this.credit = credit;
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

        public String getCurrency_format() {
            return currency_format;
        }

        public void setCurrency_format(String currency_format) {
            this.currency_format = currency_format;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

    }

}
