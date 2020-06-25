package id.co.akuntansiku.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.co.akuntansiku.R;


/**
 * Created by diditsepiyanto on 5/24/17.
 */

public class CustomToast {
    public void warning(Activity context, String kata, int position) {
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) context.findViewById(R.id.custom_toast_container));
            LinearLayout background = (LinearLayout) layout.findViewById(R.id.back);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(kata);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(position | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warning_long(Activity context, String kata, int position){
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) context.findViewById(R.id.custom_toast_container));
            LinearLayout background = (LinearLayout) layout.findViewById(R.id.back);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(kata);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(position|Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void danger(Activity context, String kata, int position) {
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) context.findViewById(R.id.custom_toast_container));
            LinearLayout background = (LinearLayout) layout.findViewById(R.id.back);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(kata);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(position | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void success(Activity context, String kata, int position) {
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) context.findViewById(R.id.custom_toast_container));
            LinearLayout background = (LinearLayout) layout.findViewById(R.id.back);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(kata);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(position | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void warningCenter(Activity context, String kata, int position) {
        try {
            LayoutInflater inflater = context.getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast_activity,
                    (ViewGroup) context.findViewById(R.id.custom_toast_container));
            LinearLayout background = (LinearLayout) layout.findViewById(R.id.back);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
            TextView text = (TextView) layout.findViewById(R.id.text);
            text.setText(kata);
            Toast toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL , 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
