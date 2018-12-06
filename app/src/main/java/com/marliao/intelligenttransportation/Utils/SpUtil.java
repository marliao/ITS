package com.marliao.intelligenttransportation.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sp;

    public static void putBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("comfig", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            sp = context.getSharedPreferences("comfig", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,value);
    }
}
