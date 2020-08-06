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
import id.co.akuntansiku.accounting.transaction.model.DataTransactionPending;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.Helper;

import static id.co.akuntansiku.utils.Helper.dateConverter;


public class TransactionFailedAdapter extends RecyclerView.Adapter<TransactionFailedAdapter.ViewHolder> {
    public ArrayList<DataTransactionPending> mData = new ArrayList<>();
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;
    String[] transactionMode = new String[200];
    Context context;
    boolean is_loading = true;

    // data is passed into the constructor
    public TransactionFailedAdapter(Context context, ArrayList<DataTransactionPending> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.transactionMode = transactionMode;
        this.is_loading = is_loading;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.akuntansiku_adapter_transaction_failed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.t_code.setText(mData.get(position).getCode());
            if (mData.get(position).getStatus() == 1)
                holder.t_status.setText("[add]");
            else if (mData.get(position).getStatus() == 2)
                holder.t_status.setText("[update]");
            else if (mData.get(position).getStatus() == 3)
                holder.t_status.setText("[delete]");
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_code;
        TextView t_status;

        ViewHolder(View itemView) {
            super(itemView);
            t_code = itemView.findViewById(R.id.t_code);
            t_status = itemView.findViewById(R.id.t_status);
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
