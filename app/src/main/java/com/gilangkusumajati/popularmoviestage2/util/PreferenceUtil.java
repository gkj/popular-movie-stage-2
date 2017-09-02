package com.gilangkusumajati.popularmoviestage2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.gilangkusumajati.popularmoviestage2.model.MovieMenu;


/**
 * Created by Gilang Kusuma Jati on 7/29/17.
 */

public class PreferenceUtil {

    private static final String PREF_SELECTED_MENU = "selected_menu";
    private static final String DEFAULT_SELECTED_MENU = MovieMenu.POPULAR.toString();

    public static void setSelectedMenu(Context context, MovieMenu movieMenu) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(PREF_SELECTED_MENU, movieMenu.toString());
        editor.apply();
    }

    public static MovieMenu getSelectedMenu(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String menu = sp.getString(PREF_SELECTED_MENU, DEFAULT_SELECTED_MENU);
        return MovieMenu.getType(menu);
    }

}
