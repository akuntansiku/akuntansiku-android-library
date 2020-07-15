package id.co.akuntansiku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.model.DataCurrency;

/**
 * Created by diditsepiyanto on 8/21/17.
 */

public class CurrencyFormater {
    static class Utils {
        public static SortedMap<Currency, Locale> currencyLocaleMap;

        static {
            currencyLocaleMap = new TreeMap<Currency, Locale>(new Comparator<Currency>() {
                public int compare(Currency c1, Currency c2) {
                    return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
                }
            });
            for (Locale locale : Locale.getAvailableLocales()) {
                try {
                    Currency currency = Currency.getInstance(locale);
                    currencyLocaleMap.put(currency, locale);
                } catch (Exception e) {
                }
            }
        }


        public static String getCurrencySymbol(String currencyCode) {
            Currency currency = Currency.getInstance(currencyCode);
            System.out.println(currencyCode + ":-" + currency.getSymbol(currencyLocaleMap.get(currency)));
            return currency.getSymbol(currencyLocaleMap.get(currency));
        }

    }

    public static String cur(Context x, Double s) {
        Locale local = x.getResources().getConfiguration().locale;
        SharedPreferences sharedPreferences = x.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY, "").equals("")) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY, Utils.getCurrencySymbol(Currency.getInstance(local).getCurrencyCode()));
            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY_COUNTRY, x.getResources().getConfiguration().locale.getDisplayCountry());
            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY_CODE, Currency.getInstance(local).getCurrencyCode());
            editor.apply();
        }

        double parsed;
        try {
            parsed = s;
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }

        NumberFormat formatter =
                NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setParseIntegerOnly(true);
        String formatted = formatter.format((parsed));

        return sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY, "")+" "+formatted;
    }


    public static void changeCurrency(Context x, String currency_code) {
        try {
            ArrayList<DataCurrency> currency = new ArrayList<>();
            JSONObject c_language = new JSONObject(x.getResources().getString(R.string.currency));
            HashMap<String, String> map = new HashMap<>();
            Iterator<?> keys = c_language.keys();
            ArrayList<DataCurrency> currency_tmp = new ArrayList<>();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = c_language.getString(key);
                JSONObject hasil = new JSONObject(value);
                currency_tmp.add(new DataCurrency( hasil.getString("code"), hasil.getInt("decimal_digits"), hasil.getString("name"),
                        hasil.getString("name_plural"), hasil.getInt("rounding"), hasil.getString("symbol"), hasil.getString("symbol_native")));
                map.put(key.trim(), value);
            }
            Collections.sort(currency_tmp);
            currency.addAll(currency_tmp);
            for (int i = 0; i < currency.size(); i++){
                if (currency.get(i).getCode().equals(currency_code)){
                    SharedPreferences sharedPreferences = x.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY, currency.get(i).getSymbol_native());
                    editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY_COUNTRY, currency.get(i).getName());
                    editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SETTING_CURRENCY_CODE, currency.get(i).getCode());
                    editor.apply();
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
