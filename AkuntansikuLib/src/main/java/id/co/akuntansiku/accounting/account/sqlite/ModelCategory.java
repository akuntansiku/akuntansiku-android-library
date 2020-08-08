package id.co.akuntansiku.accounting.account.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.util.ArrayList;

import id.co.akuntansiku.accounting.account.model.DataCategory;
import id.co.akuntansiku.utils.ConfigAkuntansiku;
import id.co.akuntansiku.utils.Helper;

public class ModelCategory extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = ConfigAkuntansiku.AKUNTANSIKU_DATABASE_VERSION;
    private String TABLE_NAME = "category";
    private Context context;
    private static ModelCategory sInstance;

    public ModelCategory(Context context) {
        super(context, Helper.getDatabaseName(context), null, DATABASE_VERSION);
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setWriteAheadLoggingEnabled(true);
        }
    }

    public static synchronized ModelCategory getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ModelCategory(context.getApplicationContext());
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

    public void create(DataCategory dataCashBox){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("id", dataCashBox.getId());
        data.put("name", dataCashBox.getName());
        data.put("type", dataCashBox.getType());
        db.insert(TABLE_NAME, null, data);
        db.close();
    }

    public void createAll(ArrayList<DataCategory> dataCategory){
        delete_table();
        for (int i = 0; i < dataCategory.size(); i ++){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues data = new ContentValues();
            data.put("id", dataCategory.get(i).getId());
            data.put("name", dataCategory.get(i).getName());
            data.put("type", dataCategory.get(i).getType());
            db.insert(TABLE_NAME, null, data);
            db.close();
        }
    }

    public ArrayList<DataCategory> getAll() {
        ArrayList<DataCategory> listCashBox = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME, null);
        int i = 0;
        if (cur.getCount() > 0) cur.moveToFirst();
        while (i < cur.getCount()) {
            DataCategory dataBarang = new DataCategory(
                    cur.getInt(cur.getColumnIndex("id")),
                    cur.getString(cur.getColumnIndex("name")),
                    cur.getInt(cur.getColumnIndex("type"))
            );
            listCashBox.add(dataBarang);
            cur.moveToNext();
            i++;
        }
        cur.close();
        db.close();
        return listCashBox;
    }
}
