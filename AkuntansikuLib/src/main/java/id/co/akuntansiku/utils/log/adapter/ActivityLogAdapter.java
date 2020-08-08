package id.co.akuntansiku.utils.log.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.transaction.model.DataTransactionPending;
import id.co.akuntansiku.utils.log.model.DataActivityLog;


public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ViewHolder> {
    public ArrayList<DataActivityLog> mData = new ArrayList<>();
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;
    String[] transactionMode = new String[200];
    Context context;
    boolean is_loading = true;

    // data is passed into the constructor
    public ActivityLogAdapter(Context context, ArrayList<DataActivityLog> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.transactionMode = transactionMode;
        this.is_loading = is_loading;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.akuntansiku_adapter_activity_log, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.t_code.setText(mData.get(position).getCode());
        holder.t_note.setText(mData.get(position).getNote());
        holder.t_date.setText(mData.get(position).getCreated_at());
            if (mData.get(position).getStatus() == 1)
                holder.t_status.setText("[add]");
            else if (mData.get(position).getStatus() == 2)
                holder.t_status.setText("[update]");
            else if (mData.get(position).getStatus() == 3)
                holder.t_status.setText("[delete]");
            else holder.t_status.setText("[]");
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_code;
        TextView t_status;
        TextView t_note;
        TextView t_date;

        ViewHolder(View itemView) {
            super(itemView);
            t_code = itemView.findViewById(R.id.t_code);
            t_status = itemView.findViewById(R.id.t_status);
            t_note = itemView.findViewById(R.id.t_note);
            t_date = itemView.findViewById(R.id.t_date);
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
