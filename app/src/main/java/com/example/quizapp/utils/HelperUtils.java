package com.example.quizapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class HelperUtils {

    public static  String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    public static String convertDateToStr(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date);
        return  formattedDate;
    }
}
