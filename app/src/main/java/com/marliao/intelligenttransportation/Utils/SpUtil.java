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
        return sp.getBoolean(key, value);
    }

    public static void putString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("comfig", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String value) {
        if (sp == null) {
            sp = context.getSharedPreferences("comfig", Context.MODE_PRIVATE);
        }
        return sp.getString(key, value);
    }
}
