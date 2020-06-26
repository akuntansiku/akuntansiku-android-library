package id.co.akuntansiku.accounting.transaction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.transaction.model.DataTransaction;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.Helper;

import static id.co.akuntansiku.utils.Helper.dateConverter;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    public ArrayList<DataTransaction> mData = new ArrayList<>();
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;
    String[] transactionMode = new String[200];
    Context context;
    boolean is_loading = true;

    // data is passed into the constructor
    public TransactionAdapter(Context context, ArrayList<DataTransaction> data, String[] transactionMode, boolean is_loading) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.transactionMode = transactionMode;
        this.is_loading = is_loading;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (is_loading){
            View view = mInflater.inflate(R.layout.akuntansiku_adapter_transaction_loading, parent, false);
            return new ViewHolder(view);
        }else {
            View view = mInflater.inflate(R.layout.akuntansiku_adapter_transaction, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!is_loading) {
            holder.t_mode.setText(transactionMode[mData.get(position).getMode() + 1]);
            holder.t_created_at.setText(dateConverter(mData.get(position).getCreated_at()));
            holder.t_note.setText(mData.get(position).getNote());
            holder.t_nominal.setText(CurrencyFormater.cur(context, Helper.debitToNominal(mData.get(position).getJournal())));
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_mode;
        TextView t_created_at;
        TextView t_note;
        TextView t_nominal;

        ViewHolder(View itemView) {
            super(itemView);
            t_mode = itemView.findViewById(R.id.t_mode);
            t_created_at = itemView.findViewById(R.id.t_created_at);
            t_note = itemView.findViewById(R.id.t_note);
            t_nominal = itemView.findViewById(R.id.t_nominal);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
