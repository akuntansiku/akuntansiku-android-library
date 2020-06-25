package id.co.akuntansiku.utils.model;

public class DataCurrency implements Comparable<DataCurrency> {
    String code;
    int decimal_digits;
    String name;
    String name_plural;
    int rounding;
    String symbol;
    String symbol_native;

    public DataCurrency(String code, int decimal_digits, String name, String name_plural, int rounding, String symbol, String symbol_native) {
        this.code = code;
        this.decimal_digits = decimal_digits;
        this.name = name;
        this.name_plural = name_plural;
        this.rounding = rounding;
        this.symbol = symbol;
        this.symbol_native = symbol_native;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDecimal_digits() {
        return decimal_digits;
    }

    public void setDecimal_digits(int decimal_digits) {
        this.decimal_digits = decimal_digits;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_plural() {
        return name_plural;
    }

    public void setName_plural(String name_plural) {
        this.name_plural = name_plural;
    }

    public int getRounding() {
        return rounding;
    }

    public void setRounding(int rounding) {
        this.rounding = rounding;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol_native() {
        return symbol_native;
    }

    public void setSymbol_native(String symbol_native) {
        this.symbol_native = symbol_native;
    }

    @Override
    public int compareTo(DataCurrency b) {
        return name_plural.compareToIgnoreCase(b.getName_plural());
    }

    @Override
    public String toString(){
        return this.name_plural;
    }

}
