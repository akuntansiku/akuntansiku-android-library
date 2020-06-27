package id.co.akuntansiku.master_data.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.utils.CustomToast;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.RetrofitSend;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;


public class ContactAdd extends AppCompatActivity {
    EditText e_email, e_name, e_address, e_no_hp, e_note;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_contact_add);
        e_email = findViewById(R.id.e_email);
        e_name = findViewById(R.id.e_name);
        e_address = findViewById(R.id.e_address);
        e_no_hp = findViewById(R.id.e_no_hp);
        e_note = findViewById(R.id.e_note);

        Button b_add = findViewById(R.id.b_add);
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact_add();
            }
        });

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Tambah Kontak");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void contact_add(){
        retrofit2.Call<ApiResponse> call = RetrofitSend.Service(this).contact_add(
                e_email.getText().toString(),
                e_name.getText().toString(),
                e_address.getText().toString(),
                e_no_hp.getText().toString(),
                e_note.getText().toString()
        );

        RetrofitSend.sendData(this, true, call, new RetrofitSend.RetrofitSendListener() {
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
