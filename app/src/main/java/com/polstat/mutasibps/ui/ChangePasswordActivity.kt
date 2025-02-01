package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.polstat.mutasibps.R
import com.polstat.mutasibps.utils.SharedPreferencesManager
import com.polstat.mutasibps.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private lateinit var etNewPassword: TextInputEditText
    private lateinit var etConfirmPassword: TextInputEditText
    private lateinit var btnChangePassword: Button
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Inisialisasi UI
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)

        // Tombol Ubah Kata Sandi
        btnChangePassword.setOnClickListener {
            changePassword()
        }

        // Tombol Kembali
        backButton.setOnClickListener {
            finish() // Tutup activity dan kembali ke halaman sebelumnya
        }
    }

    private fun changePassword() {
        val token = sharedPreferencesManager.getToken()
        val newPassword = etNewPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
            return
        }

        // Menampilkan loading
        progressBar.visibility = View.VISIBLE

        if (token != null) {
            // Membuat instance Retrofit dan memanggil API
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Memanggil API untuk mengubah password
                    val response: Response<Unit> = apiService.changePassword("Bearer $token", newPassword)
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@ChangePasswordActivity, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ChangePasswordActivity, ProfileActivity::class.java))
                        finish()
                    } else {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@ChangePasswordActivity, "Gagal memperbarui password!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@ChangePasswordActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
    }
}
