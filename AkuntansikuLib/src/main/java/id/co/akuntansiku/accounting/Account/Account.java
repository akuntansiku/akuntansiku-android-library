package id.co.akuntansiku.accounting.Account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.adapter.AccountAdapter;
import id.co.akuntansiku.accounting.Account.adapter.NotifikasiAccountAdapter;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.accounting.Account.model.DataNotifikasi;
import id.co.akuntansiku.accounting.Account.sqlite.ModelAccount;
import id.co.akuntansiku.accounting.Account.sqlite.ModelCategory;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.utils.ComingSoon;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;
import okhttp3.ResponseBody;

public class Account extends AppCompatActivity {
    AccountAdapter adapter;
    ListView lv;
    Object[] categories = new Object[200];

    ArrayList<DataCategory> dataCategories = new ArrayList<>();
    ArrayList<DataAccount> dataAccounts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_account);

        Button addCashBox = findViewById(R.id.addCashBox);
        lv = findViewById(R.id.list_cashbox);

        lv.setEmptyView(findViewById(R.id.empty));
        lv.setDividerHeight(0);

        addCashBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account.this, ComingSoon.class);
                startActivity(intent);
//                dialogAddCashBox();
            }
        });


        showActionBar();

        final EditText editCariBarang = (EditText) findViewById(R.id.cari_barang);
        final Button delete_text = (Button) findViewById(R.id.hapus_text);
        delete_text.setVisibility(View.GONE);
        delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCariBarang.setText("");
                delete_text.setVisibility(View.GONE);
            }
        });

//        editCariBarang.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (count == 0) {
//                    status_cari = true;
//                    delete_text.setVisibility(View.GONE);
//                } else {
//                    delete_text.setVisibility(View.VISIBLE);
//                    status_cari = true;
//                }
//
//                findBarang(s.toString());
//            }
//        });

        account_get_all();

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Daftar Akun");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //startActivity(new Intent(this, Akuntansiku.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void showActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayUseLogoEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void dialogAddCashBox() {
        final AlertDialog.Builder dialognya = new AlertDialog.Builder(this);
        final AlertDialog alert = dialognya.create();
        LayoutInflater li = LayoutInflater.from(this);
        View inputnya = li.inflate(R.layout.akuntansiku_dialog_cashbox_add, null);
        final Button btnTidak = (Button) inputnya.findViewById(R.id.batal);
        final Button btnYa = (Button) inputnya.findViewById(R.id.lanjut);
        final EditText namaCashbox = (EditText) inputnya.findViewById(R.id.editNamaCashBox);
        final EditText jmlah = (EditText) inputnya.findViewById(R.id.editJumlahCashBox);
        final Spinner spiner = (Spinner) inputnya.findViewById(R.id.spinerJenisCashBox);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (int i = 0; i < dataCategories.size(); i++) {
            categories.add(dataCategories.get(i).getName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.akuntansiku_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.akuntansiku_spinner_item);

        // attaching data adapter to spinner
        spiner.setAdapter(dataAdapter);


        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetDataService service = RetrofitClientInstance.getRetrofitInstance(Account.this).create(GetDataService.class);
                retrofit2.Call<ResponseBody> call = service.akunCreate(namaCashbox.getText().toString(), "01-" + randomString(5), String.valueOf(dataCategories.get(spiner.getSelectedItemPosition()).getType()), jmlah.getText().toString());

                call.enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Log.d("cek result", result);
                            int id = 0;
                            if (response.isSuccessful()) {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getString("status").equals("success")) {
                                    id = jsonObject.getInt("data");
                                }
                                alert.dismiss();

                            } else {
                                // Request not successful
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Account.this).create();
                        alertDialog.setTitle("Koneksi Terputus");
                        alertDialog.setMessage("Cek koneksi internet Anda atau hubungi pengembang!");
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
        });

        alert.setView(inputnya);
        alert.show();
    }

    private void showAdapter(ArrayList<DataAccount> data_cashbox) {
        dataAccounts = data_cashbox;
        if (data_cashbox.size() == 0) {
            ArrayList<DataNotifikasi> newsNotif = new ArrayList<DataNotifikasi>();
            newsNotif.add(new DataNotifikasi(0, "Tidak Ada Akun"));
            NotifikasiAccountAdapter adapter = new NotifikasiAccountAdapter(this, R.layout.akuntansiku_adapter_notification, newsNotif);
            lv.setAdapter(adapter);
        } else {
            adapter = new AccountAdapter(this, R.layout.akuntansiku_adapter_account, dataAccounts, dataCategories, categories);
            lv.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String randomString(int size) {
        char[] chars = "0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private void account_get_all() {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).account_get_all();
        RetrofitSend.sendData(this, true, call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                Gson gson = new Gson();
                dataAccounts = gson.fromJson(data.getString("account"), new TypeToken<List<DataAccount>>() {
                }.getType());
                ModelAccount modelAccount = new ModelAccount(Account.this);
                modelAccount.createAll(dataAccounts);
                dataCategories = gson.fromJson(data.getString("category"), new TypeToken<List<DataCategory>>() {
                }.getType());
                ModelCategory modelCategory = new ModelCategory(Account.this);
                modelCategory.createAll(dataCategories);
                for (int i = 0; i < dataCategories.size(); i++) {
                    categories[i + 1] = dataCategories.get(i);
                }
                showAdapter(dataAccounts);
            }

            @Override
            public void onError(ApiResponse.Meta meta) {

            }
        });
    }


}
