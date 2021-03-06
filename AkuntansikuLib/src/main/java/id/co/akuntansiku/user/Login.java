package id.co.akuntansiku.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.setting.company.CompanyActivity;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.Webview;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;
import id.co.akuntansiku.utils.sqlite.ModelAllTable;
import okhttp3.ResponseBody;

public class Login extends AppCompatActivity {
    EditText t_email, t_password;
    ProgressBar p_login;
    LinearLayout l_danger;
    TextView t_a_danger, t_1, t_2;
    LinearLayout l_welcome, l_login;
    Button b_register, b_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.akuntansiku_login);
        Button button_login = findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_token();
            }
        });

        t_email = findViewById(R.id.t_email);
        t_password = findViewById(R.id.t_password);
        p_login = findViewById(R.id.p_login);
        l_danger = findViewById(R.id.a_danger);
        t_a_danger = findViewById(R.id.t_a_danger);
        l_danger.setVisibility(View.GONE);
        b_register = findViewById(R.id.l_register);
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Webview.class);
                startActivity(intent);
            }
        });
        t_1 = findViewById(R.id.t_1);
        t_2 = findViewById(R.id.t_2);
        l_welcome = findViewById(R.id.l_welcome);
        l_login = findViewById(R.id.l_login);
        b_next = findViewById(R.id.b_next);
        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l_welcome.setVisibility(View.GONE);
                l_login.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        t_1.setText("Selamat Datang di Integrasi " + sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_APP_NAME, "") + " dengan Akuntansiku");
        t_2.setText("Terintregasi dengan " + sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_APP_NAME, ""));

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Integrasi dengan Akuntansiku");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void get_token() {
        p_login.setVisibility(View.VISIBLE);
        l_danger.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ResponseBody> call = service.getToken(
                t_email.getText().toString(),
                t_password.getText().toString(),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_GRANT_TYPE, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_ID, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_CLIENT_SECRET, ""),
                sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_SCOPE, "")
        );


        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.code() == 401) {
                        p_login.setVisibility(View.GONE);
                        l_danger.setVisibility(View.VISIBLE);
                        t_a_danger.setText("Login gagal, cek kembali email dan password anda");
                        return;
                    } else if (response.code() == 200) {
                        String res = response.body().string();
                        SharedPreferences sharedPreferences = Login.this.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        JSONObject jsonObject = new JSONObject(res);
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN_TYPE, jsonObject.getString("token_type"));
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_TOKEN, jsonObject.getString("access_token"));
                        editor.putString(ConfigAkuntansiku.AKUNTANSIKU_API_REFRESH_TOKEN, jsonObject.getString("refresh_token"));
                        editor.apply();

                        profile_get();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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

    private void profile_get() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(Login.this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.profile_get();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            SharedPreferences sharedPreferences = Login.this.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            JSONObject user = res.getData().getJSONObject("user");
                            String database_name = "AKUNTANSIKU_" + user.getInt("id");

                            editor.putInt(ConfigAkuntansiku.AKUNTANSIKU_USER_ID, user.getInt("id"));
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_DATABASE_NAME, database_name);
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_USER_NAME, user.getString("name"));
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_USER_EMAIL, user.getString("email"));
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, user.getString("role_web"));
                            if (res.getData().getJSONObject("user").isNull("default_company_web"))
                                editor.putInt(ConfigAkuntansiku.AKUNTANSIKU_USER_DEFAULT_COMPANY_WEB, -1);
                            else
                                editor.putInt(ConfigAkuntansiku.AKUNTANSIKU_USER_DEFAULT_COMPANY_WEB, res.getData().getJSONObject("user").getInt("default_company_web"));

                            ModelAllTable db = new ModelAllTable(Login.this, database_name);
                            db.getWritableDatabase();

                            editor.apply();
                            Intent intent = new Intent(Login.this, CompanyActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (res.getStatus().equals("error")) {

                        }
                    } else if (response.code() == 401) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
