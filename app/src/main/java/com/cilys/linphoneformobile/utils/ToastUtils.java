package com.cilys.linphoneformobile.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void show(Context cx, String str){
        if (cx == null) {
            return;
        }
        if (str != null) {
            Toast.makeText(cx, str, Toast.LENGTH_SHORT).show();
        }
    }
}
