package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.polstat.mutasibps.R
import com.polstat.mutasibps.data.ApiClient
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var tvUserName: TextView
    private lateinit var tvUnitKerja: TextView
    private lateinit var btnCreate: MaterialButton
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inisialisasi UI
        tvUserName = findViewById(R.id.tvUserName)
        tvUnitKerja = findViewById(R.id.tvUnitKerja)
        btnCreate = findViewById(R.id.btnBuatPengajuan)
        bottomNavigationView = findViewById(R.id.bottomNavigationUser)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)

        // Ambil token dari SharedPreferences
        val token = sharedPreferencesManager.getToken()

        if (token.isNullOrEmpty()) {
            Log.e("HomeActivity", "Token is null or empty!")
        } else {
            Log.d("HomeActivity", "Using token: $token")
            fetchUserProfile(token)  // Panggil getProfile dengan token
        }

        // Button untuk pengajuan mutasi
        btnCreate.setOnClickListener {
            val intent = Intent(this, CreateMutationRequestActivity::class.java)
            startActivity(intent)
        }

        // Menangani pemilihan menu dari BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_user_home -> {
                    // Tidak ada aksi karena ini adalah halaman utama
                    true
                }
                R.id.nav_user_mutation_list -> {
                    // Navigasi ke MutationListActivity
                    val intent = Intent(this, MutationListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_user_edit_profile -> {
                    // Navigasi ke ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchUserProfile(token: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Membuat instance Retrofit dan memanggil API
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)
                val response: Response<User> = apiService.getProfile("Bearer $token")

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Update UI dengan data profil pengguna
                        tvUserName.text = user.username
                        tvUnitKerja.text = "Unit Kerja: ${user.unitKerja}"
                    } else {
                        Toast.makeText(this@HomeActivity, "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("HomeActivity", "Error fetching profile: ${e.message}")
                Toast.makeText(this@HomeActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
