package id.co.akuntansiku.accounting.transaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.transaction.model.DataTransactionMode;
import id.co.akuntansiku.master_data.contact.ContactActivity;
import id.co.akuntansiku.utils.Helper;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class TransactionAdd extends AppCompatActivity {
    public int id_contact = 0;
    private SimpleDateFormat dateFormatter1, dateFormatter2;
    private DatePickerDialog datePickerDialog;
    private LinearLayout l_date, l_contact_name, l_due_date, l_danger;
    private TextView t_date, t_due_date, t_debit, t_credit, t_contact_name, t_a_danger;
    private EditText e_nominal, e_note;
    private Spinner spinnerCategory, spinnerDebit, spinnerCredit;
    private Button b_add;
    LinearLayout l_optional_button, l_optional;
    String[] category;
    String mode, debit_code, credit_code, tanggal_asli, jatuh_tempo;
    RelativeLayout rel_loading;
    String time_now = "";

    ArrayList<DataAccount> dataAccounts = new ArrayList<>();
    ArrayList<DataAccount> debit = new ArrayList<>();
    ArrayList<DataAccount> credit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_transaction_add);
        rel_loading = findViewById(R.id.rel_loading);
        rel_loading.setVisibility(View.VISIBLE);
        t_a_danger = findViewById(R.id.t_a_danger);
        l_danger = findViewById(R.id.a_danger);
        l_danger.setVisibility(View.GONE);

        init();

        try {
            SimpleDateFormat dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            tanggal_asli = dateFormatter1.format(new Date());
            SimpleDateFormat dateFormatter2 = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            t_date.setText(dateFormatter2.format(new Date()));

            SimpleDateFormat timeFormater = new SimpleDateFormat("HHmmssSSS", Locale.US);
            time_now = timeFormater.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }

        transaction_format();

        dateFormatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatter2 = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        l_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


        spinnerDebit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                debit_code = debit.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCredit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                credit_code = credit.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpan();
            }
        });

        l_contact_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionAdd.this, ContactActivity.class);
                i.putExtra("key", "from_transaction_add");
                startActivityForResult(i, 1);
            }
        });


        l_due_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });

        l_optional_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l_optional.getVisibility() == View.GONE)
                    l_optional.setVisibility(View.VISIBLE);
                else l_optional.setVisibility(View.GONE);
            }
        });

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Tambah Transaksi");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private ArrayList<DataAccount> getAccountByTransactionMode(Integer[] category) {
        ArrayList<DataAccount> m = new ArrayList<>();
        for (int j = 0; j < category.length; j++) {
            for (int i = 0; i < dataAccounts.size(); i++) {
                if (category[j] == dataAccounts.get(i).getId_category()) {
                    m.add(dataAccounts.get(i));
                }
            }
        }
        return m;
    }

    private void setAdapterCategory() {
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.akuntansiku_spinner_item, category);
        aa.setDropDownViewResource(R.layout.akuntansiku_spinner_item);
        spinnerCategory.setAdapter(aa);
        spinnerCategory.setSelection(2);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = String.valueOf(dataTransactionModes.get(position).getId());
                DataTransactionMode dataTransactionMode = dataTransactionModes.get(position);
                t_debit.setText(dataTransactionMode.getDebit_hint() + " (Debit)");
                t_credit.setText(dataTransactionMode.getCredit_hint() + " (Credit)");

                setAdapterSpinnerDebitKredit(getAccountByTransactionMode(dataTransactionMode.getDebit()),
                        getAccountByTransactionMode(dataTransactionMode.getCredit()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAdapterSpinnerDebitKredit(ArrayList<DataAccount> cashbox_dari, ArrayList<DataAccount> cashbox_ke) {
        this.debit = cashbox_dari;
        this.credit = cashbox_ke;

        SpinnerCashboxAdapter aaCashboxDari = new SpinnerCashboxAdapter(this, R.layout.akuntansiku_spinner_item, cashbox_dari);
        SpinnerCashboxAdapter aaCashboxKe = new SpinnerCashboxAdapter(this, R.layout.akuntansiku_spinner_item, cashbox_ke);

        spinnerDebit.setAdapter(aaCashboxDari);
        spinnerCredit.setAdapter(aaCashboxKe);
    }

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggal_asli = dateFormatter1.format(newDate.getTime());
                t_date.setText(dateFormatter2.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getDate() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(TransactionAdd.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                jatuh_tempo = dateFormatter1.format(newDate.getTime());
                t_due_date.setText(dateFormatter2.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    private void init() {
        t_debit = findViewById(R.id.dari);
        t_credit = findViewById(R.id.ke);
        l_date = findViewById(R.id.iv_tanggal);
        t_date = findViewById(R.id.tanggal_tambah_transaksi);
        spinnerCategory = findViewById(R.id.spiner_kategori);
        spinnerDebit = findViewById(R.id.spinercashbox);
        spinnerCredit = findViewById(R.id.spinercashbox2);
        l_optional = findViewById(R.id.l_optional);
        l_optional_button = findViewById(R.id.l_optional_button);
        t_due_date = findViewById(R.id.t_due_date);
        e_nominal = findViewById(R.id.et_nominal);
        b_add = findViewById(R.id.btn_simpan);
        e_note = findViewById(R.id.et_catatan);
        l_contact_name = findViewById(R.id.l_contact);
        t_contact_name = findViewById(R.id.t_contact_name);
        l_due_date = findViewById(R.id.lin_jatuh_tempo_hutang);

    }


    public class SpinnerCashboxAdapter extends ArrayAdapter<DataAccount> {
        private Context context;
        private int viewResourceId;
        private List<DataAccount> objects;

        public SpinnerCashboxAdapter(Context context, int resource, List<DataAccount> objects) {
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
        public DataAccount getItem(int pos) {
            return objects.get(pos);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            final DataAccount cur = objects.get(position);
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            TextView nama = (TextView) v.findViewById(R.id.text_itemlist_spinner);

            nama.setText(cur.getName());

            return v;
        }

        public View getView(final int position, View ConvertView, @NonNull ViewGroup parent) {
            final DataAccount cur = objects.get(position);
            View v = ConvertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(viewResourceId, null);
            }
            TextView nama = (TextView) v.findViewById(R.id.text_itemlist_spinner);

            nama.setText(cur.getName());

            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int id = data.getIntExtra("id_contact", 0);
                String contact_name = data.getStringExtra("name_contact");
                t_contact_name.setText(contact_name);
                id_contact = id;
            }
        }
    }


    private void simpan() {
        l_danger.setVisibility(View.GONE);
        String nominal = "0";
        if (!e_nominal.getText().toString().equals(""))
            nominal = e_nominal.getText().toString();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.transaction_add(
                tanggal_asli, debit_code, credit_code, e_nominal.getText().toString(), "akuntansiku_android",
                mode, e_note.getText().toString(), id_contact, jatuh_tempo
        );

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else if (res.getStatus().equals("error")) {
                            l_danger.setVisibility(View.VISIBLE);
                            t_a_danger.setText(res.getMeta().getError_message());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(TransactionAdd.this).create();
                alertDialog.setTitle("Connection Error");
                alertDialog.setMessage("please check your internet connection");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }


    ArrayList<DataTransactionMode> dataTransactionModes = new ArrayList<>();

    private void transaction_format() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(this).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.transaction_mode();

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            rel_loading.setVisibility(View.GONE);
                            Gson gson = new Gson();
                            dataAccounts = gson.fromJson(res.getData().getString("akuntansiku_account"), new TypeToken<List<DataAccount>>() {
                            }.getType());

                            dataTransactionModes = gson.fromJson(res.getData().getString("transaction_mode"), new TypeToken<List<DataTransactionMode>>() {
                            }.getType());
                            category = new String[dataTransactionModes.size()];
                            for (int i = 0; i < dataTransactionModes.size(); i++) {
                                category[i] = dataTransactionModes.get(i).getName();
                            }
                            setAdapterCategory();
                        } else if (res.getStatus().equals("error")) {

                        }
                    } else if (response.code() == 401) {
                        Helper.forceLogout(TransactionAdd.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
