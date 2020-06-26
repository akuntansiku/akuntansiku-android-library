package org.owline.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.Akuntansiku;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button = findViewById(R.id.launch);
        Akuntansiku.initialization(this, "kasir_pintar", "1NGvqXi5qjWtIuKIAOB5FB8E47n16B7mlHDxehVo");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambah();
                Akuntansiku.lauch(MainActivity.this);
            }
        });
    }

    private void tambah() {
        String created_at = "2020-06-25 12:00:01";

        String contact_name = "Didit Sepiyanto";
        String contact_email = "sepiyantodidit@gmail.com";
        String contact_address = "Surabaya";
        String contact_no_hp = "085746692273";

        DataContact dataContact = new DataContact(contact_name,contact_email, contact_no_hp, contact_address);
        ArrayList<DataTransaction.Journal> data_journal = new ArrayList<>();
        data_journal.add(new DataTransaction.Journal("1-10703", 0.0, 10000.0));
        data_journal.add(new DataTransaction.Journal("1-10502", 10000.0, 0.0));

        Akuntansiku.addTransaction(this, dataContact, created_at, "", Akuntansiku.PENJUALAN_KASIR_PINTAR, "cash", "", "", false, "", "", data_journal );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Akuntansiku.resendData(this);
    }
}
