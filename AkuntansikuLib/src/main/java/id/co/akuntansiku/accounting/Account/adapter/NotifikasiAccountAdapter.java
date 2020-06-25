package id.co.akuntansiku.accounting.Account.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.model.DataNotifikasi;

public class NotifikasiAccountAdapter extends ArrayAdapter<DataNotifikasi> {
    private ArrayList<DataNotifikasi> inputData;
    private int viewResourceId;
    private Activity context;

    public NotifikasiAccountAdapter(Activity context, int viewResourceId, ArrayList<DataNotifikasi> inputData) {
        super(context, viewResourceId, inputData);
        this.inputData = inputData;
        this.context = context;
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        TextView notifikasi;
        LinearLayout linier;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }

        notifikasi = (TextView) v.findViewById(R.id.txtNotifikasi);
        linier = (LinearLayout) v.findViewById(R.id.linier);

        notifikasi.setText(inputData.get(position).getNotifikasi());
        return v;
    }
}
