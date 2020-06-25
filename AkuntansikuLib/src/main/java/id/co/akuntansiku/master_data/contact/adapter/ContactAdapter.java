package id.co.akuntansiku.master_data.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.master_data.contact.model.DataContact;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    public ArrayList<DataContact> mData = new ArrayList<>();
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;
    boolean is_loading = true;

    // data is passed into the constructor
    public ContactAdapter(Context context, ArrayList<DataContact> data, boolean is_loading) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.is_loading = is_loading;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (is_loading){
            View view = mInflater.inflate(R.layout.adapter_contact_loading, parent, false);
            return new ViewHolder(view);
        }else {
            View view = mInflater.inflate(R.layout.adapter_contact, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!is_loading){
            holder.t_name.setText(mData.get(position).getName());
            holder.t_email.setText(mData.get(position).getEmail());
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_name;
        TextView t_email;

        ViewHolder(View itemView) {
            super(itemView);
            t_name = itemView.findViewById(R.id.t_name);
            t_email = itemView.findViewById(R.id.t_email);
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
