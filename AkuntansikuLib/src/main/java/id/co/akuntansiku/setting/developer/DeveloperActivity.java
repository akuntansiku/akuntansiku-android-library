package id.co.akuntansiku.setting.developer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.co.akuntansiku.R;
import id.co.akuntansiku.utils.ConfigAkuntansiku;

public class DeveloperActivity extends AppCompatActivity {
    EditText e_base_url;
    Button b_save;
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_developer_mode);
        sharedPreferences = getSharedPreferences(ConfigAkuntansiku.AKUNTANSIKU_SHARED_KEY, Context.MODE_PRIVATE);

        e_base_url = findViewById(R.id.e_base_url);
        e_base_url.setText(sharedPreferences.getString(ConfigAkuntansiku.AKUNTANSIKU_URL, ConfigAkuntansiku.AKUNTANSIKU_BASE_URL));

        b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ConfigAkuntansiku.AKUNTANSIKU_URL, e_base_url.getText().toString());
                editor.apply();
                finish();
            }
        });

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Mode Pengembang");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
