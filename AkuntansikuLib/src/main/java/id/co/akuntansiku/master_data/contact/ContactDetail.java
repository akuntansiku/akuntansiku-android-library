package id.co.akuntansiku.master_data.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class ContactDetail extends AppCompatActivity {
    DataContact dataContact;
    TextView t_email, t_name, t_address, t_no_hp, t_note;
    Button b_delete, b_edit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_contact_detail);
        t_email = findViewById(R.id.t_email);
        t_name = findViewById(R.id.t_name);
        t_address = findViewById(R.id.t_address);
        t_no_hp = findViewById(R.id.t_no_hp);
        t_note = findViewById(R.id.t_note);
        b_delete = findViewById(R.id.b_delete);
        b_edit = findViewById(R.id.b_edit);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt("contact_id");
            get_contact_detail(id);
        }

        b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ContactDetail.this);
                dialog.setTitle( "Hapus Data" )
                        .setMessage("Apakah anda yakin ingin menghapus data ini?")
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.cancel();
                            }
                        })
                        .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                contact_delete(dataContact.getId());
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
        if (sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, "admin").equals("viewer")){
            b_edit.setVisibility(View.GONE);
            b_delete.setVisibility(View.GONE);
        }

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Detail Kontak");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void set_view(){
        t_email.setText(dataContact.getEmail());
        t_name.setText(dataContact.getName());
        t_address.setText(dataContact.getAddress());
        t_no_hp.setText(dataContact.getNo_hp());
        t_note.setText(dataContact.getNote());
    }

    private void get_contact_detail(int contact_id) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.contact_get_detail(contact_id);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")){
                            Gson gson = new Gson();
                            dataContact = gson.fromJson(res.getData().getString("contact"), new TypeToken<DataContact>(){}.getType());
                            set_view();
                        }else if (res.getStatus().equals("error")){

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void contact_delete(int contact_id) {
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).contact_delete(contact_id);
        RetrofitSend.sendData(this, false, call, new RetrofitSend.RetrofitSendListener() {
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
