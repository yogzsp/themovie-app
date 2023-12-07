package com.y5p.themovie;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchHistoryManager {

    private static final String PREF_NAME = "SearchHistoryPrefs";
    private static final String KEY_SEARCH_HISTORY = "searchHistory";

    private SharedPreferences sharedPreferences;

    public SearchHistoryManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Mendapatkan list riwayat pencarian dari SharedPreferences
    public List<String> getSearchHistory() {
        Set<String> searchHistorySet = sharedPreferences.getStringSet(KEY_SEARCH_HISTORY, new HashSet<>());
        return new ArrayList<>(searchHistorySet);
    }

    // Menyimpan query ke dalam list riwayat pencarian
    public void saveSearchHistory(String query) {
        List<String> searchHistoryList = getSearchHistory();
        searchHistoryList.add(0, query); // Tambahkan ke posisi pertama (indeks 0)

        // Batasi jumlah riwayat pencarian menjadi 5
        if (searchHistoryList.size() > 5) {
            searchHistoryList = searchHistoryList.subList(0, 5);
        }

        // Simpan list riwayat pencarian yang baru
        Set<String> searchHistorySet = new HashSet<>(searchHistoryList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_SEARCH_HISTORY, searchHistorySet);
        editor.apply();
    }

    // Menghapus riwayat pencarian dari SharedPreferences
    public void clearSearchHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_SEARCH_HISTORY);
        editor.apply();
    }
}
