package id.co.akuntansiku.accounting.transaction;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.accounting.transaction.adapter.TransactionAdapter;
import id.co.akuntansiku.accounting.transaction.adapter.TransactionFailedAdapter;
import id.co.akuntansiku.accounting.transaction.model.DataTransactionPending;
import id.co.akuntansiku.accounting.transaction.sqlite.ModelTransactionPending;
import id.co.akuntansiku.utils.Akuntansiku;

import static id.co.akuntansiku.utils.Helper.formatString;

public class TransactionFailed extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<DataTransactionPending> dataTransactionPendings;
    Button b_send_again;
    TextView t_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_transaction_failed);
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(this);
        dataTransactionPendings = modelTransactionPending.all();
        recyclerView = findViewById(R.id.r_transaction_failed);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        b_send_again = findViewById(R.id.b_send_again);
        t_empty = findViewById(R.id.t_empty);

        showAdapter();

        b_send_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Akuntansiku.resendData(TransactionFailed.this, new Akuntansiku.ResendTransactionListener() {
                    @Override
                    public void onCallback(boolean success) {
                        if (success) {
                            ModelTransactionPending modelTransactionPending = new ModelTransactionPending(TransactionFailed.this);
                            dataTransactionPendings = modelTransactionPending.all();
                            showAdapter();
                            Toast.makeText(TransactionFailed.this, "Berhasil mengirim data", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(TransactionFailed.this, "Gagal mengirim data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Daftar Transaksi Gagal");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showAdapter(){
        TransactionFailedAdapter transactionFailedAdapter = new TransactionFailedAdapter(this, dataTransactionPendings);
        recyclerView.setAdapter(transactionFailedAdapter);
        if (dataTransactionPendings.size() == 0)
            t_empty.setVisibility(View.VISIBLE);
        transactionFailedAdapter.setClickListener(new TransactionFailedAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder dialognya = new AlertDialog.Builder(TransactionFailed.this);
                final AlertDialog alert = dialognya.create();
                LayoutInflater li = LayoutInflater.from(TransactionFailed.this);
                View inputnya = li.inflate(R.layout.akuntansiku_dialog_transaction_failed, null);
                TextView t_data = inputnya.findViewById(R.id.t_data);
                t_data.setText(formatString(dataTransactionPendings.get(position).getData()));
                final Button b_close = (Button) inputnya.findViewById(R.id.button_close);
                final Button b_copy = (Button) inputnya.findViewById(R.id.button_copy);
                final Button b_delete = (Button) inputnya.findViewById(R.id.button_delete);
                b_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(TransactionFailed.this);
                        dialog.setTitle( "Hapus Data" )
                                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        dialoginterface.cancel();
                                    }
                                })
                                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(TransactionFailed.this);
                                        modelTransactionPending.delete(dataTransactionPendings.get(position).getCode());
                                        dataTransactionPendings = modelTransactionPending.all();
                                        dialoginterface.cancel();
                                        alert.cancel();
                                        showAdapter();
                                        Toast.makeText(TransactionFailed.this, "Terhapus!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                });
                b_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.cancel();
                    }
                });
                b_copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("json_transaksi", dataTransactionPendings.get(position).getData());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(TransactionFailed.this, "Disalin!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setView(inputnya);
                alert.show();
            }
        });
    }
}
