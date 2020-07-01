package id.co.akuntansiku.accounting.transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class TransactionDetail extends AppCompatActivity {
    DataTransaction dataTransaction;
    TextView t_transaction_mode, t_date, t_nominal, t_contact_name, t_due_date, t_note;
    DataContact dataContact;
    Button b_delete, b_edit;
    RelativeLayout rel_loading;
    LinearLayout l_journal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_transaction_detail);
        l_journal = findViewById(R.id.l_journal);
        rel_loading = findViewById(R.id.rel_loading);
        rel_loading.setVisibility(View.VISIBLE);
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



    private void setJournal(DataTransaction dataTransaction){
        for (int i = 0; i < dataTransaction.getJournal().size(); i++){
            LayoutInflater inflater = LayoutInflater.from(TransactionDetail.this);
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.akuntansiku_item_journal, null, false);
            TextView t_name = layout.findViewById(R.id.t_name);
            TextView t_debit = layout.findViewById(R.id.t_debit);
            TextView t_credit = layout.findViewById(R.id.t_credit);
            t_name.setText(dataTransaction.getJournal().get(i).getName());
            t_debit.setText(CurrencyFormater.cur(this, dataTransaction.getJournal().get(i).getDebit()));
            t_credit.setText(CurrencyFormater.cur(this, dataTransaction.getJournal().get(i).getCredit()));
            l_journal.addView(layout);
        }
    }

    private void set_text(){
        t_transaction_mode.setText("mode " + dataTransaction.getMode());
        t_date.setText(Helper.dateConverter(dataTransaction.getCreated_at()));
        t_nominal.setText(CurrencyFormater.cur(this, Helper.debitToNominal(dataTransaction.getJournal())));
        t_note.setText(dataTransaction.getNote());
        t_due_date.setText(dataTransaction.getDue_date());
    }

    private void get_transaction_detail(String code) {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).transaction_detail(code);
        RetrofitSend.sendData(this, true, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                rel_loading.setVisibility(View.GONE);
                Gson gson = new Gson();
                dataTransaction = gson.fromJson(data.getString("transaction"), new TypeToken<DataTransaction>(){}.getType());
                set_text();
                setJournal(dataTransaction);
                get_contact_detail(dataTransaction.getContact_id());
            }

            @Override
            public void onError(ApiResponse.Meta meta) {

            }
        });
    }

    private void get_contact_detail(int contact_id) {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).contact_get_detail(contact_id);
        RetrofitSend.sendData(this, true, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Gson gson = new Gson();
                dataContact = gson.fromJson(data.getString("contact"), new TypeToken<DataContact>(){}.getType());
                t_contact_name.setText(dataContact.getName());
            }

            @Override
            public void onError(ApiResponse.Meta meta) {

            }
        });
    }

    private void transaction_delete(String code) {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).transaction_delete(code);
        RetrofitSend.sendData(this, true, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }

            @Override
            public void onError(ApiResponse.Meta meta) {

            }
        });
    }
}
