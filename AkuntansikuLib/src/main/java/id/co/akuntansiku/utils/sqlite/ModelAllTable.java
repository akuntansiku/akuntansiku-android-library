package id.co.akuntansiku.utils.sqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import id.co.akuntansiku.utils.Config;

public class ModelAllTable extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public ModelAllTable(Context context, String database_name) {
        super(context, database_name, null, Config.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("create table if not exists " + "model_transaction_pending" + " (" +
                "code varchar(225) primary key, " +
                "data text, " +
                "created_at DEFAULT CURRENT_TIMESTAMP);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
    }
}
