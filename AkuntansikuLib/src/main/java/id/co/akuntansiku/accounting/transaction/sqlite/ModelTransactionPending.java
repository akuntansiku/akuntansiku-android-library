package id.co.akuntansiku.accounting.transaction.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import id.co.akuntansiku.accounting.transaction.model.DataTransactionPending;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.Helper;

public class ModelTransactionPending extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = ConfigAkuntansiku.AKUNTANSIKU_DATABASE_VERSION;
    private String TABLE_NAME = "model_transaction_pending";
    private Context context;
    private static ModelTransactionPending sInstance;
    /*
        status
        1 = add
        2 = update
        3 = delete
     */

    public ModelTransactionPending(Context context) {
        super(context, Helper.getDatabaseName(context), null, DATABASE_VERSION);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setWriteAheadLoggingEnabled(true);
        }
    }

    public static synchronized ModelTransactionPending getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ModelTransactionPending(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void hapus_table() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_NAME + "'");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void create(String code, String status, String data_transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("code", code);
        data.put("status", status);
        data.put("data", data_transaction);
        db.insert(TABLE_NAME, null, data);
        db.close();
    }

    public ArrayList<DataTransactionPending> all() {
        ArrayList<DataTransactionPending> listTransaction = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME , null);
        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            DataTransactionPending dataAccount = new DataTransactionPending(
                    cur.getString(cur.getColumnIndex("code")),
                    cur.getString(cur.getColumnIndex("data"))
            );
            listTransaction.add(dataAccount);
            cur.moveToNext();
            i++;
        }
        cur.close();
        db.close();
        return listTransaction;
    }

    public String toString() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME +" order by  status asc" , null);
        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        JSONArray jsonArray = new JSONArray();
        while (i < cur.getCount()) {
            try {
                JSONObject jsonObject = new JSONObject();
                String code = cur.getString(cur.getColumnIndex("code"));
                String status = cur.getString(cur.getColumnIndex("status"));
                JSONObject data = new JSONObject(cur.getString(cur.getColumnIndex("data")));
                jsonObject.put("code", code);
                jsonObject.put("status", status);
                jsonObject.put("data", data);
                jsonArray.put(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
            cur.moveToNext();
            i++;
        }
        Log.d("ModelTransactionPending", jsonArray.toString());
        cur.close();
        db.close();
        return jsonArray.toString();
    }

    public void clearTransactionPending(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++){
                delete(jsonArray.getString(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean delete(String code) {
        boolean success;
        SQLiteDatabase db = this.getWritableDatabase();
        success = db.delete(TABLE_NAME, "code" + "= '" + code + "'", null) > 0;
        db.close();
        return success;
    }
}
