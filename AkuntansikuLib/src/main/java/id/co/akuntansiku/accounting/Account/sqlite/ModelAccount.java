package id.co.akuntansiku.accounting.Account.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.akuntansiku.accounting.Account.model.DataAccount;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.Helper;

public class ModelAccount extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = ConfigAkuntansiku.AKUNTANSIKU_DATABASE_VERSION;
    private String TABLE_NAME = "account";
    private Context context;
    private static ModelAccount sInstance;

    public ModelAccount(Context context) {
        super(context, Helper.getDatabaseName(context), null, DATABASE_VERSION);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setWriteAheadLoggingEnabled(true);
        }
    }

    public static synchronized ModelAccount getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ModelAccount(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void delete_table() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void create(DataAccount dataAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("name", dataAccount.getName());
        data.put("code", dataAccount.getCode());
        data.put("status", dataAccount.getStatus());
        data.put("type", dataAccount.getType());
        data.put("id_category", dataAccount.getId_category());
        data.put("description", dataAccount.getDescription());
        data.put("archived", dataAccount.getArchived());
        db.insert(TABLE_NAME, null, data);
        db.close();
    }

    public void createAll(ArrayList<DataAccount> dataAccount) {
        delete_table();
        for (int i = 0; i < dataAccount.size(); i ++){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues data = new ContentValues();
            data.put("name", dataAccount.get(i).getName());
            data.put("code", dataAccount.get(i).getCode());
            data.put("status", dataAccount.get(i).getStatus());
            data.put("type", dataAccount.get(i).getType());
            data.put("id_category", dataAccount.get(i).getId_category());
            data.put("description", dataAccount.get(i).getDescription());
            data.put("archived", dataAccount.get(i).getArchived());
            db.insert(TABLE_NAME, null, data);
            db.close();
        }
    }

    public boolean delete(String code) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        success = db.delete(TABLE_NAME, "code" + "=" + code, null) > 0;
        db.close();
        return success;
    }


    public ArrayList<DataAccount> getAll() {
        ArrayList<DataAccount> listCashBox = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME , null);
        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            DataAccount dataAccount = new DataAccount(
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getString(cur.getColumnIndex("code")),
                    cur.getInt(cur.getColumnIndex("status")),
                    cur.getInt(cur.getColumnIndex("type")),
                    cur.getInt(cur.getColumnIndex("id_category")),
                    cur.getString(cur.getColumnIndex("description")),
                    cur.getInt(cur.getColumnIndex("archived"))
            );
            listCashBox.add(dataAccount);
            cur.moveToNext();
            i++;
        }
        cur.close();
        db.close();
        return listCashBox;
    }

    public ArrayList<DataAccount> getByCategory(int id_category) {
        ArrayList<DataAccount> listCashBox = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME + " where id_category = " + id_category, null);
        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            DataAccount dataAccount = new DataAccount(
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getString(cur.getColumnIndex("code")),
                    cur.getInt(cur.getColumnIndex("status")),
                    cur.getInt(cur.getColumnIndex("type")),
                    cur.getInt(cur.getColumnIndex("id_category")),
                    cur.getString(cur.getColumnIndex("description")),
                    cur.getInt(cur.getColumnIndex("archived"))
            );
            listCashBox.add(dataAccount);
            cur.moveToNext();
            i++;
        }
        cur.close();
        db.close();
        return listCashBox;
    }

    public DataAccount findByName(String nama) {
        SQLiteDatabase db = this.getWritableDatabase();
        DataAccount dataAccount = new DataAccount();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME
                + " where nama = '" + nama + "'", null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            dataAccount = new DataAccount(
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getString(cur.getColumnIndex("code")),
                    cur.getInt(cur.getColumnIndex("status")),
                    cur.getInt(cur.getColumnIndex("type")),
                    cur.getInt(cur.getColumnIndex("id_category")),
                    cur.getString(cur.getColumnIndex("description")),
                    cur.getInt(cur.getColumnIndex("archived"))
            );
        }
        cur.close();
        db.close();
        return dataAccount;

    }


    public DataAccount getCashBox(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME + " WHERE id_account = '" + id + "'", null);
        DataAccount dataAccount = new DataAccount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            dataAccount.setName(cur.getString(cur.getColumnIndex("name")));
            dataAccount.setCode(cur.getString(cur.getColumnIndex("code")));
        }
        cur.close();
        db.close();
        return dataAccount;
    }
}
