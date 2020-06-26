package id.co.akuntansiku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.accounting.transaction.sqlite.ModelTransactionPending;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.user.Login;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class Akuntansiku {
    public static void initialization(Activity activity, String client_id, String client_secret) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.CLIENT_ID, client_id);
        editor.putString(Config.CLIENT_SECRET, client_secret);
        editor.putString(Config.GRANT_TYPE, "password");
        editor.putString(Config.SCOPE, "*");
        editor.apply();
    }

    public static void addTransaction(Activity context, DataContact dataContact, String created_at, String note, int mode, String payment_method,
                                      String tag, String cost_number, boolean is_draft, String due_date, String parent_code, ArrayList<DataTransaction.Journal> journals) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Config.IS_LOGIN, false))
            return;
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(context);

        String code = created_at.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
        int user_id = sharedPreferences.getInt(Config.USER_ID, 0);
        String date = created_at;
        if (created_at.length() == 19)
            date = date + ":000";

        try {
            JSONObject data_transaction = new JSONObject();

            JSONObject journal_check = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < journals.size(); i++) {
                if (!journal_check.isNull(journals.get(i).getCode())) {
                    Log.e("akuntansiku", "[akuntansiku] There is the same akuntansiku_account code");
                    return;
                }
                JSONObject journal = new JSONObject();
                journal.put("code", journals.get(i).getCode());
                journal.put("debit", journals.get(i).getDebit());
                journal.put("credit", journals.get(i).getCredit());
                jsonArray.put(journal);
            }

            if (Helper.creditToNominal(journals) != Helper.debitToNominal(journals)) {
                Log.e("akuntansiku", "[akuntansiku] The amount of debit and credit is not the same");
                return;
            }

            JSONObject data_contact = new JSONObject();
            data_contact.put("name", dataContact.getName());
            data_contact.put("email", dataContact.getEmail());
            data_contact.put("code", dataContact.getCode());
            data_contact.put("address", dataContact.getAddress());
            data_contact.put("note", dataContact.getNote());
            data_contact.put("no_hp", dataContact.getNo_hp());

            data_transaction.put("contact", data_contact);
            data_transaction.put("code", code);
            data_transaction.put("mode", mode);
            data_transaction.put("note", note);
            data_transaction.put("user_id", user_id);
            data_transaction.put("created_at", date);
            data_transaction.put("app_source", Config.CLIENT_ID);
            data_transaction.put("payment_method", payment_method);
            data_transaction.put("tag", tag);
            data_transaction.put("cost_number", cost_number);
            data_transaction.put("is_draft", is_draft);
            data_transaction.put("parent_code", parent_code);
            data_transaction.put("due_date", due_date);
            data_transaction.put("journal", jsonArray);

            modelTransactionPending.create(code, data_transaction.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        resendData(context);
    }

    public static void resendData(final Activity context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Config.IS_LOGIN, false))
            return;
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(context);
        String data = modelTransactionPending.toString();
        if (data.equals(""))
            return;
        retrofit2.Call<ApiResponse> call = service.transaction_pending_add(data);
        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            JSONArray jsonArray = res.getData().getJSONArray("transaction");
                            ModelTransactionPending modelTransactionPending = new ModelTransactionPending(context);
                            modelTransactionPending.clearTransactionPending(jsonArray);
                        } else if (res.getStatus().equals("error")) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
