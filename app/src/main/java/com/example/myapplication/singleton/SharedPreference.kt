package com.example.myapplication.singleton

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SharedPreference {
    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        val master= MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "jwts", // Name of the shared preferences file
            master,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveData(key: String,value: String){
        sharedPreferences.edit().putString(key,value).apply()
    }
    fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}