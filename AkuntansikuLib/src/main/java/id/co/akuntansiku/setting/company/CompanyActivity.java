package id.co.akuntansiku.setting.company;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.accounting.Account.sqlite.ModelAccount;
import id.co.akuntansiku.accounting.Account.sqlite.ModelCategory;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.setting.company.adapter.CompanyAdapter;
import id.co.akuntansiku.setting.company.model.DataCompany;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class CompanyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataCompany> dataCompanies = new ArrayList<>();
    CompanyAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    RelativeLayout rel_loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_company);
        rel_loading = findViewById(R.id.rel_loading);
        rel_loading.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.r_company);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        company_get();
        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Pilih Perusahaan");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void company_get() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(CompanyActivity.this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.company_get();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            Gson gson = new Gson();
                            ArrayList<DataCompany> my_compnay = gson.fromJson(res.getData().getString("company"), new TypeToken<List<DataCompany>>() {
                            }.getType());
                            ArrayList<DataCompany> another_compnay = gson.fromJson(res.getData().getString("another_company"), new TypeToken<List<DataCompany>>() {
                            }.getType());
                            final ArrayList<DataCompany> dataCompanies = new ArrayList<>();
                            dataCompanies.addAll(my_compnay);
                            dataCompanies.addAll(another_compnay);

                            if (dataCompanies.size() == 0) {
                                Intent intent = new Intent(CompanyActivity.this, CompanyAdd.class);
                                startActivity(intent);
                                finish();
                            } else {
                                rel_loading.setVisibility(View.GONE);
                                adapter = new CompanyAdapter(CompanyActivity.this, dataCompanies);
                                adapter.setClickListener(new CompanyAdapter.ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        company_switch(dataCompanies.get(position).getId());
                                        rel_loading.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                });
                                recyclerView.setAdapter(adapter);
                            }

                        } else if (res.getStatus().equals("error")) {

                        }
                    } else if (response.code() == 401) {
                        Helper.forceLogout(CompanyActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                rel_loading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                AlertDialog alertDialog = new AlertDialog.Builder(CompanyActivity.this).create();
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

    private void company_switch(int id_company) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.company_switch(id_company);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            SharedPreferences sharedPreferences = getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(ConfigAkuntansiku.AKUNTANSIKU_USER_DEFAULT_COMPANY_WEB, res.getData().getJSONObject("user").getInt("default_company_web"));
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, res.getData().getJSONObject("user").getString("role_web"));
                            editor.putBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, true);
                            editor.apply();

                            Gson gson = new Gson();
                            ArrayList<DataAccount> dataAccounts = gson.fromJson(res.getData().getString("account"), new TypeToken<List<DataAccount>>() {}.getType());
                            ModelAccount modelAccount = new ModelAccount(CompanyActivity.this);
                            modelAccount.createAll(dataAccounts);

                            ArrayList<DataCategory> dataCategories = gson.fromJson(res.getData().getString("category"), new TypeToken<List<DataCategory>>() {}.getType());
                            ModelCategory modelCategory = new ModelCategory(CompanyActivity.this);
                            modelCategory.createAll(dataCategories);

                            CurrencyFormater.changeCurrency(CompanyActivity.this, res.getData().getJSONObject("company").getString("currency"));
                            Intent intent = new Intent(CompanyActivity.this, AccountingActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getStatus().equals("error")) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(CompanyActivity.this).create();
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
