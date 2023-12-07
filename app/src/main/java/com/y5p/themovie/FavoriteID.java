package com.y5p.themovie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoriteID {
    private static final String PREFERENCE_NAME = "MyMovie";
    private static final String KEY_FAVORITE_MOVIE_IDS = "favorite_movie_ids";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void addFavoriteMovieId(Context context, int movieId) {
        Set<String> favoriteIds = getFavoriteMovieIds(context);
        favoriteIds.add(String.valueOf(movieId));

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet(KEY_FAVORITE_MOVIE_IDS, favoriteIds);
        editor.apply();
    }

    public static Set<String> getFavoriteMovieIds(Context context) {
        return getSharedPreferences(context).getStringSet(KEY_FAVORITE_MOVIE_IDS, new HashSet<>());
    }

    public static void removeFavoriteMovieId(Context context, int movieId) {
        Set<String> favoriteIds = getFavoriteMovieIds(context);
        favoriteIds.remove(String.valueOf(movieId));

        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet(KEY_FAVORITE_MOVIE_IDS, favoriteIds);
        editor.apply();
    }

    public static void clearFavoriteMovieIds(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_FAVORITE_MOVIE_IDS);
        editor.apply();
    }

    public static boolean isFavoriteMovieIdExist(Context context, int movieId) {
        Set<String> favoriteIds = getFavoriteMovieIds(context);
        return favoriteIds.contains(String.valueOf(movieId));
    }
}
