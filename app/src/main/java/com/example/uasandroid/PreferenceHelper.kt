package com.example.uasandroid

import android.content.Context
import com.example.uasandroid.database.Book
import com.google.gson.Gson

class PreferenceHelper(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("app_prefs",
        Context.MODE_PRIVATE)

    fun saveTheme(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("theme", isDarkMode).apply()
    }

    fun getTheme(): Boolean = sharedPreferences.getBoolean("theme",
        false)

    fun saveData(books : List<Book>) {
        val editor = sharedPreferences.edit()
        editor.putString("Book", Gson().toJson(books))
        editor.apply()
    }

    fun getData(): List<Book> {
        val data = sharedPreferences.getString("Book", "").orEmpty()
        return Gson().fromJson(data, Array<Book>::class.java).toList()
    }
}