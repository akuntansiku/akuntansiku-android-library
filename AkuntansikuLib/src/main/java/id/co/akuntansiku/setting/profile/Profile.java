package id.co.akuntansiku.setting.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.log.ActivityLog;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class Profile extends AppCompatActivity {
    TextView t_name, t_email, t_no_phone, t_company_name, t_role;
    Button button_activity_log;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_profile);
        t_name = findViewById(R.id.t_name);
        t_email = findViewById(R.id.t_email);
        t_no_phone = findViewById(R.id.t_no_phone);
        t_company_name = findViewById(R.id.t_company_name);
        t_role = findViewById(R.id.t_role);
        button_activity_log = findViewById(R.id.button_activity_log);

        button_activity_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, ActivityLog.class);
                startActivity(intent);
            }
        });

        profile_get();
        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Profil");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void profile_get() {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).profile_get();

        RetrofitSend.sendData(this, true,  call, new RetrofitSend.RetrofitSendListener() {
            @Override
            public void onSuccess(JSONObject data) throws JSONException {
                JSONObject user = data.getJSONObject("user");
                t_name.setText(user.getString("name"));
                t_email.setText(user.getString("email"));
                t_no_phone.setText(user.getString("no_hp"));
                JSONObject company = data.getJSONObject("company");
                t_company_name.setText(company.getString("name"));
                String role = data.getString("role");
                t_role.setText(role);
            }

            @Override
            public void onError(ApiResponse.Meta meta) {
            }
        });
    }
}
