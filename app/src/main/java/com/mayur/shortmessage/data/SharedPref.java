package com.mayur.shortmessage.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    /**
     * Universal shared preference
     * for string
     * key_val = key value (unique)
     * def_val = default value
     */
    public static String getStringPref(String key_val, String def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
        return pref.getString(key_val, def_val);
    }

    public static void setStringPref(String key_val, String val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for integer
     */
    public static int getIntPref(String key_val, int def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val, context.MODE_PRIVATE);
        return pref.getInt(key_val, def_val);
    }

    public static void setIntPref(String key_val, int val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val, context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putInt(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for boolean
     */
    public static boolean getBooleanPref(String key_val, boolean def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
        return pref.getBoolean(key_val, def_val);
    }

    public static void setBooleanPref(String key_val, boolean val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_"+key_val,context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putBoolean(key_val, val);
        prefEditor.commit();
    }
}
