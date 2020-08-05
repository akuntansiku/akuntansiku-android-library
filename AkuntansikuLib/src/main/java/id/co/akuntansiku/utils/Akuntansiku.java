package id.co.akuntansiku.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.adapter.AccountAdapter;
import id.co.akuntansiku.accounting.Account.adapter.AccountSpinner;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.accounting.Account.sqlite.ModelAccount;
import id.co.akuntansiku.accounting.Account.sqlite.ModelCategory;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.TransactionAdd;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.accounting.transaction.sqlite.ModelTransactionPending;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.user.Login;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;
import id.co.akuntansiku.utils.sqlite.ModelAllTable;

public class Akuntansiku {
    public static void initialization(Activity activity, String client_id, String client_secret, String app_name) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, client_id);
        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_SECRET, client_secret);
        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_GRANT_TYPE, "password");
        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_SCOPE, "*");
        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_APP_NAME, app_name);
        editor.apply();
    }

    public static void lauch(Activity activity) {
        SharedPreferences sharedPreferencess = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferencess.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, "").equals("")
                || sharedPreferencess.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_SECRET, "").equals("")) {
            throw new RuntimeException("You haven't entered client_id and client_secret yet");
        }
        if (sharedPreferencess.getBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false)) {
            String database_name = sharedPreferencess.getString(ConfigAkuntansiku.AKUNTANSIKU_DATABASE_NAME, "AKUNTANSIKU");
            ModelAllTable db = new ModelAllTable(activity, database_name);
            db.getWritableDatabase();
            Intent i = new Intent(activity, AccountingActivity.class);
            activity.startActivity(i);
        } else {
            Intent i = new Intent(activity, Login.class);
            activity.startActivity(i);
        }
    }

    public static void addTransaction(Activity context, DataContact dataContact, String created_at, String note, int mode, String payment_method,
                                      String tag, String cost_number, boolean is_draft, String due_date, String parent_code, ArrayList<DataTransaction.Journal> journals) {
        if (!checkInitialize(context)) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false))
            return;
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(context);

        String code = created_at.replaceAll(" ", "").replaceAll("-", "").replaceAll(":", "");
        int user_id = sharedPreferences.getInt(ConfigAkuntansiku.AKUNTANSIKU_USER_ID, 0);
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

            JSONObject data_contact = null;
            if (dataContact != null) {
                data_contact = new JSONObject();
                data_contact.put("name", dataContact.getName());
                data_contact.put("email", dataContact.getEmail());
                data_contact.put("code", dataContact.getCode());
                data_contact.put("address", dataContact.getAddress());
                data_contact.put("note", dataContact.getNote());
                data_contact.put("no_hp", dataContact.getNo_hp());
            }

            data_transaction.put("contact", data_contact);
            data_transaction.put("code", code);
            data_transaction.put("mode", mode);
            data_transaction.put("note", note);
            data_transaction.put("user_id", user_id);
            data_transaction.put("created_at", date);
            data_transaction.put("app_source", sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, ""));
            data_transaction.put("payment_method", payment_method);
            data_transaction.put("tag", tag);
            data_transaction.put("cost_number", cost_number);
            data_transaction.put("is_draft", is_draft);
            data_transaction.put("parent_code", parent_code);
            data_transaction.put("due_date", due_date);
            data_transaction.put("journal", jsonArray);

            modelTransactionPending.create(code, sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_EMAIL, ""), ConfigAkuntansiku.AKUNTANSIKU_ADD,data_transaction.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        resendData(context);
    }

    public interface DeleteTransactionListener {
        public void onCallback(boolean success);
    }

    public static void deteleTransaction(Activity activity, String code, final DeleteTransactionListener listener) {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(activity).transaction_delete(code);
        RetrofitSend.sendData(activity, true, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                listener.onCallback(true);
            }

            @Override
            public void onError(ApiResponse.Meta meta) {
                listener.onCallback(false);
            }
        });
    }

    public static void resendData(final Activity context) {
        if (!checkInitialize(context)) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false))
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
                    } else if (response.code() == 401) {
                        Helper helper = new Helper();
                        helper.refreshToken(context, new Helper.RefreshTokenListener() {
                            @Override
                            public void onCallback(boolean success) {
                                if (!success) {
                                    Helper.forceLogout(context);
                                }
                            }
                        });
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

    public static class Category {
        public static ArrayList<DataCategory> getCategoryAll(Activity activity) {
            if (!checkInitialize(activity)) new ArrayList<>();
            ModelCategory modelCategory = new ModelCategory(activity);
            return modelCategory.getAll();
        }
    }

    public static class Account {
        public static AccountSpinner accountSpinner(Activity activity, ArrayList<DataAccount> data) {
            return new AccountSpinner(activity, R.layout.akuntansiku_simple_spinner_item, data);
        }

        public static ArrayList<DataAccount> all(Activity activity) {
            if (!checkInitialize(activity)) return new ArrayList<>();
            ModelAccount modelAccount = new ModelAccount(activity);
            return modelAccount.getAll();
        }

        public static ArrayList<DataAccount> byCategory(Activity activity, int id_category) {
            if (!checkInitialize(activity)) return new ArrayList<>();
            ModelAccount modelAccount = new ModelAccount(activity);
            return modelAccount.getByCategory(id_category);
        }
    }

    public static boolean checkInitialize(Activity activity) {
        SharedPreferences sharedPreferencess = activity.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferencess.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, "").equals("")
                || sharedPreferencess.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_SECRET, "").equals("")) {
            Log.e("Akuntansiku", "You haven't entered client_id and client_secret yet");
            return false;
        }
        if (!sharedPreferencess.getBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false)) {
            Log.e("Akuntansiku", "Data is not saved because Akuntansiku isn't logged in");
            return false;
        }
        return true;
    }

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
