package id.co.akuntansiku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import okhttp3.ResponseBody;

public class Helper {
    public static String convertNominal(Context context, Double nominal) {
        if (nominal >= 0)
            return  CurrencyFormater.cur(context, nominal);
        return  "("+CurrencyFormater.cur(context, -1*nominal)+")";
    }

    public static double debitToNominal(List<DataTransaction.Journal> journals) {
        double total = 0;
        try {
            for (int i = 0; i < journals.size(); i++){
                total += journals.get(i).getDebit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return total;
    }

    public static double creditToNominal(List<DataTransaction.Journal> journals) {
        double total = 0;
        try {
            for (int i = 0; i < journals.size(); i++){
                total += journals.get(i).getCredit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return total;
    }

    public static String dateConverter(String date){
        SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        try {
            Date newDate=spf.parse(date);
            spf= new SimpleDateFormat("dd MMM yyyy");
            date = spf.format(newDate);
            return  date ;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  "";
    }

    public static void forceLogout(Activity activity){
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false);
        editor.apply();
    }

    public static String setDate(int number) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, number);
        return dateFormatter.format(cal.getTime());

    }

    public static String setFirstDayThisMonth() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormatter.format(cal.getTime());
    }

    public static String setFirstDayLastMonth() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormatter.format(cal.getTime());
    }

    public static String setLastDayLastMonth() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormatter.format(cal.getTime());
    }

    public static String setLastDayThisMonth() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormatter.format(cal.getTime());
    }

    public static String convertDate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getDatabaseName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_DATABASE_NAME, "AKUNTANSIKU");
    }

    public interface RefreshTokenListener {
        public void onCallback(boolean success);
    }

    private RefreshTokenListener listener;

    public void refreshToken(final Activity activity, final RefreshTokenListener listener){
        this.listener = listener;
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(activity).create(GetDataService.class);
        retrofit2.Call<ResponseBody> call = service.refreshToken(
                "refresh_token",
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_SECRET, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_SCOPE, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_API_REFRESH_TOKEN, "")
        );

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.code() == 401){
                        listener.onCallback(false);
                    }else if (response.code() == 200) {
                        String res = response.body().string();
                        SharedPreferences sharedPreferences = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONObject jsonObject = new JSONObject(res);
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN_TYPE, jsonObject.getString("token_type"));
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN, jsonObject.getString("access_token"));
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_REFRESH_TOKEN, jsonObject.getString("refresh_token"));
                        editor.apply();
                        listener.onCallback(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
