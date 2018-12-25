package com.example.sara.grammy.Utils;

public class StringManipulation {
    public static String expandUserName(String usrname){
        return usrname.replace(".", " ");
    }
    public static String condenseUserName(String usrname){
        return usrname.replace(" ", ".");
    }
}
