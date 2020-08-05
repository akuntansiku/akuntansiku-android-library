package id.co.akuntansiku.utils.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import id.co.akuntansiku.utils.ConfigAkuntansiku;

public class ModelAllTable extends SQLiteOpenHelper {
    SQLiteDatabase db;

    public ModelAllTable(Context context, String database_name) {
        super(context, database_name, null, ConfigAkuntansiku.AKUNTANSIKU_DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL("create table if not exists " + "model_transaction_pending" + " (" +
                "code varchar(225) primary key, " +
                "user_email varchar(225), " +
                "status varchar(225), " +
                "data text, " +
                "created_at DEFAULT CURRENT_TIMESTAMP);");

        db.execSQL("create table if not exists " + "category" + " (" +
                "id integer primary key, " +
                "name varchar(225), " +
                "type integer );");

        db.execSQL("create table if not exists " + "account" + " (" +
                "code varchar(50) primary key, " +
                "name varchar(255), " +
                "status integer default 0, " +
                "type integer default 0, " +
                "id_category integer, " +
                "archived integer, " +
                "description text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
    }
}
