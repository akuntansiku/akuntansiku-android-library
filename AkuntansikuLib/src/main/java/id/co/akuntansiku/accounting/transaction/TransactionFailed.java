package id.co.akuntansiku.accounting.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ModelTransactionPending modelTransactionPending = new ModelTransactionPending(this);
        ArrayList<DataTransactionPending> dataTransactionPendings = modelTransactionPending.all();
        recyclerView = findViewById(R.id.r_transaction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TransactionFailedAdapter transactionFailedAdapter = new TransactionFailedAdapter(this, dataTransactionPendings);
        transactionFailedAdapter.setClickListener(new TransactionFailedAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                
            }
        });
    }
}
