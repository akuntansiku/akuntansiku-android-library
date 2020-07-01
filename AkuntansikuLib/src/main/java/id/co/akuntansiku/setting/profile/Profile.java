package id.co.akuntansiku.setting.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class Profile extends AppCompatActivity {
    TextView t_name, t_email, t_no_phone, t_company_name, t_role;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_profile);
        t_name = findViewById(R.id.t_name);
        t_email = findViewById(R.id.t_email);
        t_no_phone = findViewById(R.id.t_no_phone);
        t_company_name = findViewById(R.id.t_company_name);
        t_role = findViewById(R.id.t_role);

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
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(Profile.this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.profile_get();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            JSONObject user = res.getData().getJSONObject("user");
                            t_name.setText(user.getString("name"));
                            t_email.setText(user.getString("email"));
                            t_no_phone.setText(user.getString("no_hp"));
                            JSONObject company = res.getData().getJSONObject("company");
                            t_company_name.setText(company.getString("name"));
                            String role = res.getData().getString("role");
                            t_role.setText(role);

                        }else if (res.getStatus().equals("error")){

                        }
                    }else if (response.code() == 401){
                        Helper.forceLogout(Profile.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(Profile.this).create();
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
