package id.co.akuntansiku.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import id.co.akuntansiku.R;
import id.co.akuntansiku.user.Login;
import id.co.akuntansiku.utils.Config;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_setting);

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

        text_toolbar.setText("");
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