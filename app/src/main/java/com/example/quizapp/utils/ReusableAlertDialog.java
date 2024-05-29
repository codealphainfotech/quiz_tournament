package com.example.quizapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ReusableAlertDialog {
    public static void showConfirmationDialog(Context context, String title, String message,
                                              DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", positiveListener);
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
