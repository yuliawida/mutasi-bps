package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var tvUsername: TextView
    private lateinit var tvRole: TextView
    private lateinit var tvName: TextView
    private lateinit var tvNip: TextView
    private lateinit var tvUnitKerja: TextView
    private lateinit var tvJabatan: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnChangePassword: Button
    private lateinit var btnDeleteAccount: Button
    private lateinit var btnLogout: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Bind UI Elements
        tvUsername = findViewById(R.id.tvUsername)
        tvRole = findViewById(R.id.tvRole)
        tvName = findViewById(R.id.tvName)
        tvNip = findViewById(R.id.tvNip)
        tvUnitKerja = findViewById(R.id.tvUnitKerja)
        tvJabatan = findViewById(R.id.tvJabatan)

        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnChangePassword = findViewById(R.id.btnChangePassword)
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount)
        btnLogout = findViewById(R.id.btnLogout)
        bottomNavigationView = findViewById(R.id.bottomNavigationProfile)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)

        // Ambil token dari SharedPreferences
        val token = sharedPreferencesManager.getToken()

        // Jika token ada, ambil data user
        if (token != null && token.isNotEmpty()) {
            fetchUserProfile(token)
        } else {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }

        // Menangani tombol Edit Profile
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Menangani tombol Change Password
        btnChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        // Menangani tombol Delete Account
        btnDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }

        // Menangani tombol Logout
        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        // Menangani pemilihan menu dari BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_user_home -> {
                    if (sharedPreferencesManager.getRole() == "ADMIN") {
                        val intent = Intent(this, HomeAdminActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.nav_user_mutation_list -> {
                    val intent = Intent(this, MutationListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_user_edit_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi untuk mengambil data profil pengguna
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
                        tvUsername.text = user.username
                        tvRole.text = "Role: ${user.role}"
                        tvName.text = "Nama: ${user.name}"
                        tvNip.text = "NIP: ${user.nip}"
                        tvUnitKerja.text = "Unit Kerja: ${user.unitKerja}"
                        tvJabatan.text = "Jabatan: ${user.jabatan}"

                        // Simpan role di SharedPreferences untuk navigasi
                        sharedPreferencesManager.saveRole(user.role)
                    } else {
                        Toast.makeText(this@ProfileActivity, "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ProfileActivity", "Error fetching profile: ${e.message}")
                Toast.makeText(this@ProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk konfirmasi hapus akun
    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus Akun")
            .setMessage("Apakah Anda yakin ingin menghapus akun ini? Tindakan ini tidak dapat dibatalkan.")
            .setPositiveButton("Ya") { _, _ -> deleteAccount() }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Fungsi untuk menghapus akun
    private fun deleteAccount() {
        val token = sharedPreferencesManager.getToken()
        token?.let {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    // Membuat instance Retrofit dan memanggil API delete account
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiService::class.java)
                    val response: Response<Unit> = apiService.deleteAccount("Bearer $it")

                    if (response.isSuccessful) {
                        sharedPreferencesManager.clear()
                        Toast.makeText(this@ProfileActivity, "Akun berhasil dihapus!", Toast.LENGTH_SHORT).show()
                        navigateToLogin()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Gagal menghapus akun", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("ProfileActivity", "Error deleting account: ${e.message}")
                    Toast.makeText(this@ProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Fungsi untuk logout
    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ -> logoutUser() }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun logoutUser() {
        sharedPreferencesManager.clear()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
