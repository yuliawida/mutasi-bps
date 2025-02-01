package com.polstat.mutasibps.utils

import android.content.Context
import android.content.SharedPreferences
import com.polstat.mutasibps.model.Role

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    // Menyimpan token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    // Mendapatkan token (mengembalikan String non-nullable)
    fun getToken(): String {
        // Jika token tidak ditemukan, kembalikan string kosong atau nilai default lainnya
        return sharedPreferences.getString("token", "")?.trim()?.replace("\"", "") ?: ""
    }

    // Menyimpan role
    fun saveRole(role: String) {
        sharedPreferences.edit().putString("role", role).apply()
    }

    // Mendapatkan role
    fun getRole(): String? {
        return sharedPreferences.getString("role", null)
    }

    // Menyimpan username
    fun saveUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }

    // Mendapatkan username
    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    // Menyimpan role sebagai enum
    fun getRoleEnum(): Role {
        val roleString = sharedPreferences.getString("role", "USER") ?: "USER"
        return try {
            Role.valueOf(roleString.uppercase())
        } catch (e: IllegalArgumentException) {
            Role.USER
        }
    }

    // Fungsi untuk membersihkan data (misalnya token, username, dll.)
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
