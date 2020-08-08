package id.co.akuntansiku.utils.log;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.transaction.TransactionFailed;
import id.co.akuntansiku.accounting.transaction.adapter.TransactionFailedAdapter;
import id.co.akuntansiku.accounting.transaction.sqlite.ModelTransactionPending;
import id.co.akuntansiku.utils.log.adapter.ActivityLogAdapter;
import id.co.akuntansiku.utils.log.model.DataActivityLog;
import id.co.akuntansiku.utils.log.sqlite.ModelActivityLog;

import static id.co.akuntansiku.utils.Helper.formatString;

public class ActivityLog extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<DataActivityLog> dataActivityLogs;
    Button b_send_again;
    TextView t_empty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_activity_log);
        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        ModelActivityLog modelTransactionPending = new ModelActivityLog(this);
        dataActivityLogs = modelTransactionPending.all();
        recyclerView = findViewById(R.id.r_transaction_failed);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        b_send_again = findViewById(R.id.b_send_again);
        t_empty = findViewById(R.id.t_empty);

        showAdapter();

        text_toolbar.setText("Log Aktivitas");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showAdapter(){
        ActivityLogAdapter transactionFailedAdapter = new ActivityLogAdapter(this, dataActivityLogs);
        recyclerView.setAdapter(transactionFailedAdapter);
        if (dataActivityLogs.size() == 0)
            t_empty.setVisibility(View.VISIBLE);
        transactionFailedAdapter.setClickListener(new ActivityLogAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final AlertDialog.Builder dialognya = new AlertDialog.Builder(ActivityLog.this);
                final AlertDialog alert = dialognya.create();
                LayoutInflater li = LayoutInflater.from(ActivityLog.this);
                View inputnya = li.inflate(R.layout.akuntansiku_dialog_transaction_failed, null);
                TextView t_data = inputnya.findViewById(R.id.t_data);
                t_data.setText(formatString(dataActivityLogs.get(position).getData()));
                final Button b_close = (Button) inputnya.findViewById(R.id.button_close);
                final Button b_copy = (Button) inputnya.findViewById(R.id.button_copy);
                final Button b_delete = (Button) inputnya.findViewById(R.id.button_delete);
                b_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityLog.this);
                        dialog.setTitle( "Hapus Data" )
                                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        dialoginterface.cancel();
                                    }
                                })
                                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        ModelActivityLog modelTransactionPending = new ModelActivityLog(ActivityLog.this);
                                        modelTransactionPending.delete(dataActivityLogs.get(position).getCode());
                                        dataActivityLogs = modelTransactionPending.all();
                                        dialoginterface.cancel();
                                        alert.cancel();
                                        showAdapter();
                                        Toast.makeText(ActivityLog.this, "Terhapus!", Toast.LENGTH_SHORT).show();
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
                        ClipData clip = ClipData.newPlainText("json_transaksi", dataActivityLogs.get(position).getData());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(ActivityLog.this, "Disalin!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setView(inputnya);
                alert.show();
            }
        });
    }
}
