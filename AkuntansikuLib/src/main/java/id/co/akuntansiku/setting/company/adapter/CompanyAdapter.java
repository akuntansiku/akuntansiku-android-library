package id.co.akuntansiku.setting.company.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import id.co.akuntansiku.R;
import id.co.akuntansiku.accounting.Account.Account;
import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.accounting.Account.model.DataCategory;
import id.co.akuntansiku.accounting.Account.sqlite.ModelAccount;
import id.co.akuntansiku.accounting.Account.sqlite.ModelCategory;
import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.setting.company.model.DataCompany;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.CurrencyFormater;
import id.co.akuntansiku.utils.retrofit.GetDataService;
import id.co.akuntansiku.utils.retrofit.RetrofitClientInstance;
import id.co.akuntansiku.utils.retrofit.model.ApiResponse;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    public ArrayList<DataCompany> mData = new ArrayList<>();
    public LayoutInflater mInflater;
    public ItemClickListener mClickListener;
    Activity context;

    // data is passed into the constructor
    public CompanyAdapter(Activity context, ArrayList<DataCompany> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.akuntansiku_adapter_company, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.t_name.setText(mData.get(position).getName());
        holder.t_status.setText(mData.get(position).getSync());
        try {
            if (!mData.get(position).getList_user().equals("") && !mData.get(position).getList_user().equals("NULL")) {
                try {
                    SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                    String email = sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_USER_EMAIL, "");
                    JSONArray jsonArray = new JSONArray(mData.get(position).getList_user());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (email.equals(jsonObject.getString("email"))) {
                            holder.t_role.setText(jsonObject.getString("role"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.l_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                company_switch(mData.get(position).getId());
            }
        });

        SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getInt(ConfigAkuntansiku.AKUNTANSIKU_USER_DEFAULT_COMPANY_WEB, 0) == mData.get(position).getId()) {
            holder.i_check.setVisibility(View.VISIBLE);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView t_name;
        TextView t_role;
        TextView t_status;
        ImageView i_check;
        LinearLayout l_switch;

        ViewHolder(View itemView) {
            super(itemView);
            t_name = itemView.findViewById(R.id.t_name);
            t_role = itemView.findViewById(R.id.t_role);
            t_status = itemView.findViewById(R.id.t_status);
            l_switch = itemView.findViewById(R.id.l_switch);
            i_check = itemView.findViewById(R.id.i_check);
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

    private void company_switch(int id_company) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        retrofit2.Call<ApiResponse> call = service.company_switch(id_company);

        call.enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                try {
                    if (response.code() == 200) {
                        ApiResponse res = response.body();
                        if (res.getStatus().equals("success")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(ConfigAkuntansiku.AKUNTANSIKU_USER_DEFAULT_COMPANY_WEB, res.getData().getJSONObject("user").getInt("default_company_web"));
                            editor.putString(ConfigAkuntansiku.AKUNTANSIKU_USER_ROLE, res.getData().getJSONObject("user").getString("role_web"));
                            editor.putBoolean(ConfigAkuntansiku.AKUNTANSIKU_IS_LOGIN, true);
                            editor.apply();

                            Gson gson = new Gson();
                            ArrayList<DataAccount> dataAccounts = gson.fromJson(res.getData().getString("account"), new TypeToken<List<DataAccount>>() {}.getType());
                            ModelAccount modelAccount = new ModelAccount(context);
                            modelAccount.createAll(dataAccounts);

                            ArrayList<DataCategory> dataCategories = gson.fromJson(res.getData().getString("category"), new TypeToken<List<DataCategory>>() {}.getType());
                            ModelCategory modelCategory = new ModelCategory(context);
                            modelCategory.createAll(dataCategories);

                            CurrencyFormater.changeCurrency(context, res.getData().getJSONObject("company").getString("currency"));
                            Intent intent = new Intent(context, AccountingActivity.class);
                            context.startActivity(intent);
                            context.finish();
                        } else if (res.getStatus().equals("error")) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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
}
