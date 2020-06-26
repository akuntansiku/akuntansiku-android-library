package id.co.akuntansiku.setting.company;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.adapter.CurrencyAdapter;
import id.co.akuntansiku.utils.model.DataCurrency;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class CompanyAdd extends AppCompatActivity {
    ArrayList<DataCurrency> dataCurrencies = new ArrayList<>();
    RelativeLayout rel_loading;
    TextView t_name, t_address;
    String currency = "IDR";
    LinearLayout a_danger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_company_add);
        t_name = findViewById(R.id.t_name);
        t_address = findViewById(R.id.t_address);


        rel_loading = findViewById(R.id.rel_loading);
        rel_loading.setVisibility(View.VISIBLE);

        a_danger = findViewById(R.id.a_danger);
        a_danger.setVisibility(View.GONE);
        get_currency_and_time_zone();

        Button b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                company_add();
            }
        });
    }

    private void company_add() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(CompanyAdd.this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.company_add(t_name.getText().toString(), currency,
                62,7, t_address.getText().toString());

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            Intent i = new Intent(getBaseContext(), CompanyActivity.class);
                            startActivity(i);
                            finish();
                        }else if (res.getStatus().equals("error")){

                        }
                    }else if (response.code() == 401){
                        Helper.forceLogout(CompanyAdd.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(CompanyAdd.this).create();
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

    private void get_currency_and_time_zone() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(CompanyAdd.this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.get_currency_and_time_zone();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            JSONObject c_currency = res.getData().getJSONObject("currency");
                            HashMap<String, String> map = new HashMap<>();
                            Iterator<?> keys = c_currency.keys();
                            ArrayList<DataCurrency> currency_tmp = new ArrayList<>();
                            while (keys.hasNext()) {
                                String key = (String) keys.next();
                                String value = c_currency.getString(key);
                                JSONObject m = new JSONObject(value);
                                currency_tmp.add(new DataCurrency(m.getString("code"), m.getInt("decimal_digits"),
                                        m.getString("name"), m.getString("name_plural"), m.getInt("rounding"),
                                        m.getString("symbol"), m.getString("symbol_native")));
                                map.put(key.trim(), value);
                            }
                            Collections.sort(currency_tmp);
                            dataCurrencies.addAll(currency_tmp);

                            rel_loading.setVisibility(View.GONE);
                            CurrencyAdapter adapter = new CurrencyAdapter(CompanyAdd.this, R.layout.akuntansiku_adapter_currency, dataCurrencies);
                            Spinner s = (Spinner) findViewById(R.id.s_currency);
                            s.setAdapter(adapter);
                            s.setSelection(0);
                            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    currency = dataCurrencies.get(i).getCode();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    currency = "IDR";
                                }
                            });
                        }else if (res.getStatus().equals("error")){

                        }
                    }else if (response.code() == 401){
                        Helper.forceLogout(CompanyAdd.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(CompanyAdd.this).create();
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
