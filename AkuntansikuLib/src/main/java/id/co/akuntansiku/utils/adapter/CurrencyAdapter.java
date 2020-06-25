package id.co.akuntansiku.utils.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import java.util.List;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.model.DataCurrency;

public class CurrencyAdapter extends ArrayAdapter<DataCurrency> {
    private Context context;
    private int viewResourceId;

    public CurrencyAdapter(Context context, int resource, List<DataCurrency> objects) {
        super(context, resource, objects);
        this.context = context;
        this.viewResourceId = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final DataCurrency cur = getItem(position);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        TextView t_name = (TextView) v.findViewById(R.id.t_name);
        TextView t_code = (TextView) v.findViewById(R.id.t_code);
        TextView t_currency = (TextView) v.findViewById(R.id.t_currency);

        t_name.setText(cur.getName());
        t_code.setText(cur.getCode());
        t_currency.setText(cur.getSymbol_native());

        return v;
    }

    public View getView(final int position, View ConvertView, @NonNull ViewGroup parent) {
        final DataCurrency cur = getItem(position);
        View v = ConvertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        TextView nama = (TextView) v.findViewById(R.id.t_name);
        TextView kode = (TextView) v.findViewById(R.id.t_code);
        TextView mata_uang = (TextView) v.findViewById(R.id.t_currency);

        nama.setText(cur.getName());
        kode.setText(cur.getCode());
        mata_uang.setText(cur.getSymbol_native());

        return v;
    }
}
