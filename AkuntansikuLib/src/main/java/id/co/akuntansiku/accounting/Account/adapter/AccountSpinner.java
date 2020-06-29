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
import java.util.List;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.utils.CurrencyFormater;

public class AccountSpinner extends ArrayAdapter<DataAccount> {
    private List<DataAccount> items;
    private int viewResourceId;
    private Activity context;

    public AccountSpinner(Activity context, int viewResourceId, List<DataAccount> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.context = context;
        this.viewResourceId = viewResourceId;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final DataAccount cur = items.get(position);
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        TextView name = (TextView) v.findViewById(R.id.t_name);
        name.setText(cur.getName() + " (" + cur.getCode() + ")");
        return v;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        final DataAccount cashbox = items.get(position);

        TextView name = (TextView) v.findViewById(R.id.t_name);

        name.setText(cashbox.getName());
        return v;
    }

}
