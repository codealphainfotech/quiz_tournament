package com.example.quizapp.utils;

import java.util.UUID;

public class HelperUtils {

    public static  String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}
