package br.com.wifi.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class PrefUtils {

    public static void save(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static <T> T loadJson(Context context, String key, Class<T> tClass) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String pref = preferences.getString(key, "");

        if (pref == null || pref.isEmpty()) {
            return null;
        }

        return new Gson().fromJson(pref, tClass);
    }

}
