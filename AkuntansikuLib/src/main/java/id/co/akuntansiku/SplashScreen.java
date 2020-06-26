package id.co.akuntansiku;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.co.akuntansiku.accounting.AccountingActivity;
import id.co.akuntansiku.user.Login;
import id.co.akuntansiku.utils.Config;
import id.co.akuntansiku.utils.sqlite.ModelAllTable;


public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_splash_screen);

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(100);
                    SharedPreferences sharedPreferencess = getSharedPreferences(Config.SHARED_KEY, Context.MODE_PRIVATE);
                    if (sharedPreferencess.getBoolean(Config.IS_LOGIN, false)) {
                        String database_name = sharedPreferencess.getString(Config.DATABASE_NAME, "AKUNTANSIKU");
                        ModelAllTable db = new ModelAllTable(SplashScreen.this, database_name);
                        db.getWritableDatabase();
                        Intent i = new Intent(getBaseContext(), AccountingActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getBaseContext(), Login.class);
                        startActivity(i);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        background.start();
    }
}
