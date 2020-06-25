package id.co.akuntansiku.utils;

public class Config {
    public static final String DATABASE_NAME = "AKUNTANSIKU";
    public static final int DATABASE_VERSION = 1;

    public static String BASE_URL = "http://akuntansiku.co.id/";
    public static String TERM_OF_SERVICE = "tos";

    public static final String SHARED_KEY = "SHARED_KEY";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String SETTING_CURRENCY = "SETTING_CURRENCY";
    public static final String SETTING_CURRENCY_COUNTRY = "SETTING_CURRENCY_COUNTRY";
    public static final String SETTING_CURRENCY_CODE = "SETTING_CURRENCY_CODE";

    public static final String API_TOKEN = "API_TOKEN";
    public static final String API_TOKEN_TYPE = "API_TOKEN_TYPE";
    public static final String API_REFRESH_TOKEN = "API_REFRESH_TOKEN";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_ROLE = "USER_ROLE";
    public static final String USER_DEFAULT_COMPANY_WEB = "USER_DEFAULT_COMPANY_WEB";

    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String CLIENT_SECRET = "CLIENT_SECRET";
    public static final String GRANT_TYPE = "GRANT_TYPE";
    public static final String SCOPE = "SCOPE";

    public static final Integer GENERAL = -1;
    public static final Integer PEMASUKAN = 0;
    public static final Integer PENGELUARAN = 1;
    public static final Integer HUTANG = 2;
    public static final Integer PIUTANG = 3;
    public static final Integer TANAM_MODAL = 4;
    public static final Integer TARIK_MODAL = 5;
    public static final Integer TRANSFER_UANG = 6;
    public static final Integer PEMBAYARAN_HUTANG = 7;
    public static final Integer PEMBAYARAN_PIUTANG = 8;
    public static final Integer PEMBELIAN_KASIR_PINTAR = 9;
    public static final Integer PENJUALAN_KASIR_PINTAR = 10;
    public static final Integer HUTANG_KASIR_PINTAR = 11;
    public static final Integer PIUTANG_KASIR_PINTAR = 12;

}
