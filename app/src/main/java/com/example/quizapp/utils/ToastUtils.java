package com.example.quizapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.example.quizapp.SignUpPage;

public class ToastUtils {

   public static void errorToast(Context context, String message){

       LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View layout = inflater.inflate(R.layout.custom_error_toast, null); // Replace with your custom toast layout resource id

       TextView text = (TextView) layout.findViewById(R.id.toast_text); // Replace with your TextView id in the layout
       text.setText(message);

       int redColor = Color.RED; // You can also use resource like R.color.your_red_color
       layout.setBackgroundColor(redColor);
       text.setTextColor(Color.WHITE);

       Toast toast = new Toast(context);
       toast.setDuration(Toast.LENGTH_SHORT);
       toast.setView(layout);
       toast.show();
    }

    public static void successToast(Context context, String message){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_error_toast, null); // Replace with your custom toast layout resource id

        TextView text = (TextView) layout.findViewById(R.id.toast_text); // Replace with your TextView id in the layout
        text.setText(message);

        int redColor = Color.BLUE; // You can also use resource like R.color.your_red_color
        layout.setBackgroundColor(redColor);
        text.setTextColor(Color.WHITE);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
