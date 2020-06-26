package id.co.akuntansiku.accounting.transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class TransactionDetail extends AppCompatActivity {
    DataTransaction dataTransaction;
    TextView t_transaction_mode, t_date, t_nominal, t_contact_name, t_due_date, t_note;
    DataContact dataContact;
    Button b_delete, b_edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_transaction_detail);
        t_transaction_mode = findViewById(R.id.t_transaction_mode);
        t_date = findViewById(R.id.t_date);
        t_nominal = findViewById(R.id.t_nominal);
        t_contact_name = findViewById(R.id.t_contact_name);
        t_due_date = findViewById(R.id.t_due_date);
        t_note = findViewById(R.id.t_note);
        b_edit  = findViewById(R.id.b_edit);
        b_delete = findViewById(R.id.b_delete);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String code = extras.getString("code");
            get_transaction_detail(code);
        }

        b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TransactionDetail.this);
                dialog.setTitle( "Hapus Data" )
                        .setMessage("Apakah anda yakin ingin menghapus data ini?")
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                transaction_delete(dataTransaction.getCode());
                            }
                        }).show();
            }
        });

        b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, "viewer").equals("viewer")){
            b_delete.setVisibility(View.GONE);
            b_edit.setVisibility(View.GONE);
        }

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Detail Transaksi");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void set_text(){
        t_transaction_mode.setText("mode " + dataTransaction.getMode());
        t_date.setText(Helper.dateConverter(dataTransaction.getCreated_at()));
        t_nominal.setText(CurrencyFormater.cur(this, Helper.debitToNominal(dataTransaction.getJournal())));
        t_note.setText(dataTransaction.getNote());
        t_due_date.setText(dataTransaction.getDue_date());
    }

    private void get_transaction_detail(String code) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.transaction_detail(code);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            Gson gson = new Gson();
                            dataTransaction = gson.fromJson(res.getData().getString("transaction"), new TypeToken<DataTransaction>(){}.getType());
                            set_text();
                            get_contact_detail(dataTransaction.getContact_id());
                        }else if (res.getStatus().equals("error")){

                        }
                    }else if (response.code() == 401){
                        Helper.forceLogout(TransactionDetail.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(TransactionDetail.this).create();
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
        });
    }

    private void get_contact_detail(int contact_id) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.contact_get_detail(contact_id);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            Gson gson = new Gson();
                            dataContact = gson.fromJson(res.getData().getString("contact"), new TypeToken<DataContact>(){}.getType());
                            //t_contact_name.setText(dataContact.getName());
                        }else if (res.getStatus().equals("error")){

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

    private void transaction_delete(String code) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.transaction_delete(code);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }else if (res.getStatus().equals("error")){

                        }
                    }else if (response.code() == 401){
                        Helper.forceLogout(TransactionDetail.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(TransactionDetail.this).create();
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
        });
    }
}
