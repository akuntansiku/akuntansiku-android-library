package org.owline.example;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import id.co.akuntansiku.accounting.Account.adapter.AccountAdapter;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.master_data.contact.model.DataContact;
import id.co.akuntansiku.utils.Akuntansiku;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button button_launch = findViewById(R.id.launch);
        Akuntansiku.initialization(this,
                "kasir_pintar",
                "1NGvqXi5qjWtIuKIAOB5FB8E47n16B7mlHDxehVo",
                "Kasir Pintar Pro");

        button_launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Akuntansiku.lauch(MainActivity.this);
            }
        });

        Button button_add = findViewById(R.id.add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambah();
            }
        });
    }

    private void tambah() {
        int random = new Random().nextInt((60 - 10) + 1) + 10;
        String created_at = "2020-07-02 12:"+random+":01";
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
        Akuntansiku.resendData(this, null);
        showAccount();
        ArrayList<DataAccount> dataAccounts = Akuntansiku.Account.all(this);
        for (int i = 0; i < dataAccounts.size(); i++){
            Log.d("mantan", dataAccounts.get(i).getName());
        }
    }

    private void showAccount(){
        ArrayList<DataAccount> data = Akuntansiku.Account.byCategory(this, 1);

        Spinner spinner_account = findViewById(R.id.s_account);
        spinner_account.setAdapter(Akuntansiku.Account.accountSpinner(this, data));
        spinner_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void deleteTransaction(){
        String code = "";
        Akuntansiku.deteleTransaction(this, code, new Akuntansiku.DeleteTransactionListener() {
            @Override
            public void onCallback(boolean success) {

            }
        });
    }
}
