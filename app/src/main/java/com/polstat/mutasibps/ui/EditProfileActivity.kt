package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.utils.SharedPreferencesManager
import com.polstat.mutasibps.model.UserRequest
import com.polstat.mutasibps.service.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var editName: TextInputEditText
    private lateinit var editNip: TextInputEditText
    private lateinit var editUnitKerja: TextInputEditText
    private lateinit var editJabatan: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var backButton: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Bind UI Elements
        editName = findViewById(R.id.editName)
        editNip = findViewById(R.id.editNip)
        editUnitKerja = findViewById(R.id.editUnitKerja)
        editJabatan = findViewById(R.id.editJabatan)
        btnSave = findViewById(R.id.btnSave)
        backButton = findViewById(R.id.backButton)
        progressBar = findViewById(R.id.progressBar)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)

        // Ambil token dari SharedPreferences
        val token = sharedPreferencesManager.getToken()

        // Jika token ada, ambil data user
        token?.let {
            fetchUserProfile(it)
        } ?: run {
            Toast.makeText(this, "Token tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }

        // Menangani tombol Edit Profile
        btnSave.setOnClickListener {
            saveProfileChanges()
        }

        // Menangani tombol Kembali
        backButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            finish() // Tutup activity dan kembali ke ProfileFragment
        }
    }

    // Fungsi untuk mengambil data profil pengguna
    private fun fetchUserProfile(token: String) {
        progressBar.visibility = View.VISIBLE

        // Call API untuk mendapatkan profil pengguna
        CoroutineScope(Dispatchers.Main).launch {
            try {
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
                        editName.setText(user.name)
                        editNip.setText(user.nip)
                        editUnitKerja.setText(user.unitKerja)
                        editJabatan.setText(user.jabatan)
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@EditProfileActivity, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    // Fungsi untuk menyimpan perubahan profil
    private fun saveProfileChanges() {
        val token = sharedPreferencesManager.getToken()
        val roleEnum = sharedPreferencesManager.getRoleEnum() // Ambil sebagai Enum

        if (editName.text.toString().isEmpty() || editNip.text.toString().isEmpty() ||
            editUnitKerja.text.toString().isEmpty() || editJabatan.text.toString().isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedUser = UserRequest(
            username = sharedPreferencesManager.getUsername().toString(),
            password = "",
            name = editName.text.toString(),
            nip = editNip.text.toString(),
            unitKerja = editUnitKerja.text.toString(),
            jabatan = editJabatan.text.toString(),
            role = roleEnum // Menggunakan Enum, bukan String
        )

        // Menampilkan loading sebelum update
        progressBar.visibility = View.VISIBLE

        token?.let {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val apiService = retrofit.create(ApiService::class.java)
                    val response: Response<User> = apiService.updateProfile("Bearer $it", updatedUser)
                    if (response.isSuccessful) {
                        val user = response.body()
                        // Update UI dengan data profil yang sudah diperbarui
                        if (user != null) {
                            editName.setText(user.name)
                            editNip.setText(user.nip)
                            editUnitKerja.setText(user.unitKerja)
                            editJabatan.setText(user.jabatan)
                        }
                        Toast.makeText(this@EditProfileActivity, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
