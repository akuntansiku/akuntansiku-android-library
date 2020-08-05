package id.co.akuntansiku.accounting.transaction;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class TransactionFailed extends AppCompatActivity {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ArrayList<DataTransactionPending> dataTransactionPendings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(this);
        dataTransactionPendings = modelTransactionPending.all();
        recyclerView = findViewById(R.id.r_transaction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TransactionFailedAdapter transactionFailedAdapter = new TransactionFailedAdapter(this, dataTransactionPendings);
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
                    }
                });
                alert.show();

            }
        });
    }

    public static String formatString(String text){

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}
