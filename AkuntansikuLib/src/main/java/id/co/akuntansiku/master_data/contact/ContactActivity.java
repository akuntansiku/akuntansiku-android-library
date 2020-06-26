package id.co.akuntansiku.master_data.contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.co.akuntansiku.R;
import id.co.akuntansiku.master_data.contact.adapter.ContactAdapter;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class ContactActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataContact> dataContacts = new ArrayList<>();
    ContactAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String key = "";
    public static final int ADD_CONTACT = 1;
    public static final int DETAIL_CONTACT = 2;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_contact_activity);
        recyclerView = findViewById(R.id.r_contact);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            key = extras.getString("key");
        }

        contact_get_all();
        FloatingActionButton b_add_contact = findViewById(R.id.b_contact_add);
        b_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactActivity.this, ContactAdd.class);
                startActivityForResult(intent, ADD_CONTACT);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, "admin").equals("viewer")){
            b_add_contact.setVisibility(View.GONE);
        }

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Kontak");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void contact_get_all() {
        dataContacts.add(new DataContact());
        dataContacts.add(new DataContact());
        dataContacts.add(new DataContact());
        dataContacts.add(new DataContact());
        dataContacts.add(new DataContact());

        adapter = new ContactAdapter(ContactActivity.this, dataContacts, true);
        recyclerView.setAdapter(adapter);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.contact_get_all();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            Gson gson = new Gson();
                            dataContacts.clear();
                            dataContacts = gson.fromJson(res.getData().getString("contact"), new TypeToken<List<DataContact>>() {
                            }.getType());
                            adapter = new ContactAdapter(ContactActivity.this, dataContacts, false);
                            adapter.setClickListener(new ContactAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (key.equals("from_transaction_add")) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("id_contact",dataContacts.get(position).getId());
                                        returnIntent.putExtra("name_contact",dataContacts.get(position).getName());
                                        setResult(Activity.RESULT_OK,returnIntent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(ContactActivity.this, ContactDetail.class);
                                        intent.putExtra("contact_id", dataContacts.get(position).getId());
                                        startActivityForResult(intent, DETAIL_CONTACT);
                                    }

                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } else if (res.getStatus().equals("error")) {

                        }
                    } else if (response.code() == 401) {
                        Helper.forceLogout(ContactActivity.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_CONTACT:
                if (resultCode == RESULT_OK) {
                    contact_get_all();
                }
                break;
            case DETAIL_CONTACT:
                if (resultCode == RESULT_OK) {
                    contact_get_all();
                }
                break;
        }
    }
}
