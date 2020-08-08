package id.co.akuntansiku.accounting.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.account.model.DataAccount;

public class AccountEdit extends AppCompatActivity {
    EditText nama_akun;
    int id_akun;
    ArrayList<DataAccount> dataAccounts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_account_edit);

        nama_akun = (EditText) findViewById(R.id.nama_akun);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            id_akun = extras.getInt("id_akun");
            nama_akun.setText(extras.getString("nama_akun"));
        }

        Button hapus =  findViewById(R.id.hapus);
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDeleteCashBox(extras.getString("nama_akun"));
            }
        });
        Button simpan = findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        hideKeyboard(this);
        showActionBar();

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Edit Akun");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sesuaikanSaldo(Double nominal_saldo) {
        final AlertDialog.Builder dialognya = new AlertDialog.Builder(this);
        final AlertDialog alert = dialognya.create();
        LayoutInflater li = LayoutInflater.from(this);
        View inputnya = li.inflate(R.layout.akuntansiku_dialog_sesuaikan_saldo, null);
        final Button btnTidak = (Button) inputnya.findViewById(R.id.batal);
        final Button btnYa = (Button) inputnya.findViewById(R.id.lanjut);
        final EditText nominal = (EditText) inputnya.findViewById(R.id.editNominal);
        nominal.setText(String.valueOf(nominal_saldo));

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });
        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        alert.setView(inputnya);
        alert.show();
    }

    public void alertDeleteCashBox(String nama_akun) {
//        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Apakah Anda ingin menghapus " + nama_akun + "?");
//        alertDialogBuilder.setMessage("Anda juga akan menghapus semua transaksi dan tindakan ini tidak dapat dibatalkan").setCancelable(true).setPositiveButton(getResources().getString(R.string.database_sub_barang_iya), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                delete();
//            }
//        }).setNegativeButton(getResources().getString(R.string.database_sub_barang_tidak), null);
//        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void showActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayUseLogoEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
