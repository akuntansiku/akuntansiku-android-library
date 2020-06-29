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

public class AccountAdapter extends ArrayAdapter<DataAccount> {
    private List<DataAccount> items;
    private ArrayList<DataCategory> dataCategories;
    Object[] categories = null;
    private int viewResourceId;
    private Activity context;

    public AccountAdapter(Activity context, int viewResourceId, List<DataAccount> items, ArrayList<DataCategory> dataCategories, Object[] categories) {
        super(context, viewResourceId, items);
        this.items = items;
        this.context = context;
        this.categories = categories;
        this.viewResourceId = viewResourceId;
        this.dataCategories = dataCategories;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        final DataAccount cashbox = items.get(position);

        TextView judul = (TextView) v.findViewById(R.id.judul_cashbox);
        TextView jumlah = (TextView) v.findViewById(R.id.jumlah_cashbox);
        TextView kategori = (TextView) v.findViewById(R.id.kategori);
        TextView kode = v.findViewById(R.id.kode_cashbox);
        LinearLayout edit = (LinearLayout) v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, AccountEdit.class);
//                intent.putExtra("nama_akun", cashbox.getName());
//                intent.putExtra("saldo_saat_ini", 0);
//                intent.putExtra("id_akun", cashbox.getId_());
//                intent.putExtra("kode_akun", cashbox.getCode());
//                context.startActivity(intent);
            }
        });

        DataCategory dataCategory = (DataCategory)categories[cashbox.getId_category()];
        kategori.setText(dataCategory.getName());
        judul.setText(cashbox.getName());
        jumlah.setText(CurrencyFormater.cur(context, 0.0));
        kode.setText(cashbox.getCode());

        return v;
    }



}
