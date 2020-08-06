package id.co.akuntansiku.setting;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.transaction.TransactionFailed;
import id.co.akuntansiku.setting.developer.DeveloperActivity;
import id.co.akuntansiku.setting.profile.Profile;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.akuntansiku_fragment_setting);

        LinearLayout l_profile = findViewById(R.id.l_profile);
        l_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this, Profile.class);
                startActivity(i);
            }
        });

        LinearLayout l_developer_mode = findViewById(R.id.l_developer_mode);
        l_developer_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialognya = new AlertDialog.Builder(SettingActivity.this);
                final AlertDialog alert = dialognya.create();
                LayoutInflater li = LayoutInflater.from(SettingActivity.this);
                View inputnya = li.inflate(R.layout.akuntansiku_dialog_developer_mode_password, null);
                final EditText e_password = inputnya.findViewById(R.id.e_password);
                final TextView t_wrong_password = inputnya.findViewById(R.id.t_wrong_password);
                final Button b_login = (Button) inputnya.findViewById(R.id.button_login);

                b_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (e_password.getText().toString().trim().equals("akuntansiku123")){
                            Intent i = new Intent(SettingActivity.this, DeveloperActivity.class);
                            startActivity(i);
                            t_wrong_password.setVisibility(View.GONE);
                            alert.cancel();
                        }else t_wrong_password.setVisibility(View.VISIBLE);
                    }
                });
                alert.setView(inputnya);
                alert.show();
            }
        });

        LinearLayout l_logout = findViewById(R.id.l_log_out);
        l_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                Helper.forceLogout(SettingActivity.this);
                finish();
            }
        });

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Pengaturan");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void logout() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.logout();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {

            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}