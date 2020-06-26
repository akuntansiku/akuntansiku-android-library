package id.co.akuntansiku.utils;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import id.co.akuntansiku.R;

public class Webview extends AppCompatActivity {
    private WebView myWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.akuntansiku_webview);
        myWebView = (WebView) findViewById(R.id.web_view);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ("http://akuntansiku.co.id/register".equals(url)
                        || "http://akuntansiku.co.id/account/transaction".equals(url)) {
                    if ("http://akuntansiku.co.id/account/transaction".equals(url)){
                        CustomToast customToast = new CustomToast();
                        customToast.success(Webview.this, "Berhasil daftar akun silahkan akuntansiku_login", Gravity.TOP);
                    }
                    return false;
                }
                finish();
                return true;
            }
        });
        myWebView.loadUrl("http://akuntansiku.co.id/register");

        LinearLayout button_toolbar = findViewById(R.id.button_toolbar);
        TextView text_toolbar = findViewById(R.id.text_toolbar);

        text_toolbar.setText("Daftar Akuntansiku");
        button_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
