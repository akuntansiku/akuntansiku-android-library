package id.co.akuntansiku.utils.retrofit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.TransactionAdd;
import id.co.akuntansiku.master_data.contact.ContactActivity;
import id.co.akuntansiku.master_data.contact.ContactDetail;
import id.co.akuntansiku.master_data.contact.adapter.ContactAdapter;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class RetrofitSend {
    public interface RetrofitSendListener {
        public void onSuccess(JSONObject data) throws JSONException;

        public void onError(ApiResponse.Meta meta);
    }

    public static GetDataService Service(Activity activity) {
        return RetrofitClientInstance.getRetrofitInstance(activity).create(GetDataService.class);
    }

    public static void sendData(final Activity activity, final boolean conn_error_show, retrofit2.Call<ApiResponse> call, final RetrofitSendListener listener) {
        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        Log.d("mantan",res.getData().toString());
                        if (res.getStatus().equals("success")) {
                            listener.onSuccess(res.getData());
                        } else if (res.getStatus().equals("error")) {
                            listener.onError(res.getMeta());
                        }
                    } else if (response.code() == 401) {
                        Helper helper = new Helper();
                        helper.refreshToken(activity, new Helper.RefreshTokenListener() {
                            @Override
                            public void onCallback(boolean success) {
                                if (success) {
                                    CustomToast customToast = new CustomToast();
                                    customToast.warning(activity, "Silahkan coba lagi", Gravity.TOP);
                                } else
                                    Helper.forceLogout(activity);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                if (conn_error_show) {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                    alertDialog.setTitle("Connection Error");
                    alertDialog.setMessage("please check your internet connection");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }
}
