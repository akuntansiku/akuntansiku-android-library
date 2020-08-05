package id.co.akuntansiku.accounting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.co.akuntansiku.BuildConfig;
import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.accounting.Account.sqlite.ModelCategory;
import id.co.akuntansiku.accounting.transaction.TransactionAdd;
import id.co.akuntansiku.accounting.transaction.TransactionDetail;
import id.co.akuntansiku.accounting.transaction.TransactionFailed;
import id.co.akuntansiku.accounting.transaction.adapter.TransactionAdapter;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.accounting.transaction.model.DataTransactionPending;
import id.co.akuntansiku.accounting.transaction.sqlite.ModelTransactionPending;
import id.co.akuntansiku.report.Report;
import id.co.akuntansiku.setting.SettingActivity;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.DateFilter;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class AccountingActivity extends AppCompatActivity {
    ArrayList<DataTransaction> dataTransactions = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    public static final int ADD_TRANSACTION = 1;
    public static final int DETAIL_TRANSACTION = 2;
    String[] transactionMode = new String[200];
    TextView t_empty;
    LinearLayout l_back, l_danger;
    TextView t_a_danger;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_fragment_accounting);
        t_a_danger = findViewById(R.id.t_a_danger);
        l_danger = findViewById(R.id.a_danger);
        l_danger.setVisibility(View.GONE);
        l_back = findViewById(R.id.l_back);
        l_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountingActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        ImageView report = findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountingActivity.this, Report.class);
                startActivity(intent);
            }
        });
        t_empty = findViewById(R.id.t_empty);
        FloatingActionButton b_accounting_add = findViewById(R.id.b_accounting_add);
        b_accounting_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AccountingActivity.this, TransactionAdd.class), ADD_TRANSACTION);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, "admin").equals("viewer")){
            b_accounting_add.setVisibility(View.GONE);
        }

        recyclerView = findViewById(R.id.r_transaction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(AccountingActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        new DateFilter(AccountingActivity.this, (Spinner) findViewById(R.id.spiner_filter_tanggal), new DateFilter.FilterDate() {
            @Override
            public void onChangeFilterDate(String from, String to) {
                first_day = from;
                last_day = to;
                transaction_get(from, to);
            }
        });
    }

    String first_day, last_day;
    public void transaction_get(String from, String to) {
        dataTransactions.add(new DataTransaction());
        dataTransactions.add(new DataTransaction());
        dataTransactions.add(new DataTransaction());
        dataTransactions.add(new DataTransaction());
        dataTransactions.add(new DataTransaction());
        dataTransactions.add(new DataTransaction());

        transactionAdapter = new TransactionAdapter(AccountingActivity.this, dataTransactions, transactionMode, true);
        recyclerView.setAdapter(transactionAdapter);

        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).transaction_get_day_filter(from, to);
        RetrofitSend.sendData(this, false, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                JSONObject issueObj = data.getJSONObject("transaction_mode");
                Iterator iterator = issueObj.keys();
                while(iterator.hasNext()){
                    String key = (String)iterator.next();
                    String value = issueObj.getString(key);
                    transactionMode[Integer.parseInt(key)+1] = value;
                }
                dataTransactions.clear();
                Gson gson = new Gson();
                dataTransactions = gson.fromJson(data.getString("transaction"), new TypeToken<List<DataTransaction>>(){}.getType());
                transactionAdapter = new TransactionAdapter(AccountingActivity.this, dataTransactions, transactionMode, false);
                transactionAdapter.setClickListener(new TransactionAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(AccountingActivity.this, TransactionDetail.class);
                        intent.putExtra("code", dataTransactions.get(position).getCode());
                        startActivityForResult(intent, DETAIL_TRANSACTION);
                    }
                });
                if (dataTransactions.size() == 0)
                    t_empty.setVisibility(View.VISIBLE);
                else t_empty.setVisibility(View.GONE);
                recyclerView.setAdapter(transactionAdapter);
            }

            @Override
            public void onError(ApiResponse.Meta data) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_TRANSACTION:
                if (resultCode == RESULT_OK) {
                    transaction_get(first_day, last_day);
                }
                break;
            case DETAIL_TRANSACTION:
                if (resultCode == RESULT_OK) {
                    transaction_get(first_day, last_day);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(this);
        ArrayList<DataTransactionPending> dataTransactionPendings = modelTransactionPending.all();
        if (dataTransactionPendings.size() > 0){
            l_danger.setVisibility(View.VISIBLE);
            t_a_danger.setText("Terdapat data yang belum terkirim ke server");
            l_danger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AccountingActivity.this, TransactionFailed.class);
                    startActivity(intent);
                }
            });
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, false)) {
            finish();
        }
    }
}