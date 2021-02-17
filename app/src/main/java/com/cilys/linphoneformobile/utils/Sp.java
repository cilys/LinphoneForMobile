package com.cilys.linphoneformobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Sp {

    private static String spFile = "default_sp_config";

    public static void putStr(Context cx, String key, String value) {
        putStr(cx, spFile, key, value);
    }

    public static void putStr(Context cx, String spFile, String key, String value) {
        if (cx == null) {
            return;
        }

        SharedPreferences sp = cx.getSharedPreferences(spFile, Context.MODE_PRIVATE);
        if (value != null) {
            sp.edit().putString(key, value).commit();
        } else {
            sp.edit().remove(key).commit();
        }
    }

    public static String getStr(Context cx, String key, String defValue){
        return getStr(cx, spFile, key, defValue);
    }

    public static String getStr(Context cx, String spFile, String key, String defValue){
        if (cx == null || key == null) {
            return defValue;
        }
        return cx.getSharedPreferences(spFile, Context.MODE_PRIVATE).getString(key, defValue);
    }
}
