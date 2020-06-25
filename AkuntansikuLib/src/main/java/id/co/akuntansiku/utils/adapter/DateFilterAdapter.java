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
import id.co.akuntansiku.utils.model.DataDateFilter;

import static id.co.akuntansiku.utils.Helper.convertDate;


public class DateFilterAdapter extends ArrayAdapter<DataDateFilter> {
        private Context context;
        private int viewResourceId;
        private List<DataDateFilter> objects;

        public DateFilterAdapter(Context context, int resource, List<DataDateFilter> objects) {
            super(context, resource, objects);
            this.context = context;
            this.objects = objects;
            this.viewResourceId = resource;
        }

        @Override
        public int getCount() {
            return objects.size();
        }

        @Override
        public DataDateFilter getItem(int pos) {
            return objects.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            final DataDateFilter cur = objects.get(position);
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            TextView nama = (TextView) v.findViewById(R.id.name);
            TextView tanggal = (TextView) v.findViewById(R.id.date);

            nama.setText(cur.getFilter_name());
            if (!cur.getFirst_date().equals("0")) {
                if (cur.getFirst_date().equals(cur.getLast_date()))
                    tanggal.setText(convertDate(cur.getFirst_date()));
                else
                    tanggal.setText(convertDate(cur.getFirst_date()) + " → " + convertDate(cur.getLast_date()));
            }
            return v;
        }

        public View getView(final int position, View ConvertView, @NonNull ViewGroup parent) {
            final DataDateFilter cur = objects.get(position);
            View v = ConvertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            TextView nama = (TextView) v.findViewById(R.id.name);
            TextView tanggal = (TextView) v.findViewById(R.id.date);

            nama.setText(cur.getFilter_name());
            if (!cur.getFirst_date().equals("0")) {
                if (cur.getFirst_date().equals(cur.getLast_date()))
                    tanggal.setText(convertDate(cur.getFirst_date()));
                else
                    tanggal.setText(convertDate(cur.getFirst_date()) + " → " + convertDate(cur.getLast_date()));
            }

            return v;
        }
    }