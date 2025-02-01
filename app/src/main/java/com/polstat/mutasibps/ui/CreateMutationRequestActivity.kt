package com.polstat.mutasibps.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.MutationRequest
import com.polstat.mutasibps.model.MutationRequestRequest
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CreateMutationRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_mutation_request)

        // Referensi input dan tombol
        val etNama = findViewById<TextInputEditText>(R.id.etNama)
        val etNip = findViewById<TextInputEditText>(R.id.etNip)
        val etJabatanSekarang = findViewById<TextInputEditText>(R.id.etJabatanSekarang)
        val etUnitKerjaSekarang = findViewById<TextInputEditText>(R.id.etUnitKerjaSekarang)
        val etProvinsi = findViewById<TextInputEditText>(R.id.etProvinsi)
        val etKabupaten = findViewById<TextInputEditText>(R.id.etKabupaten)
        val etJabatanTujuan = findViewById<TextInputEditText>(R.id.etJabatanTujuan)
        val etUnitKerjaTujuan = findViewById<TextInputEditText>(R.id.etUnitKerjaTujuan)
        val btnSubmit = findViewById<MaterialButton>(R.id.btnSubmit)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Tombol Back
        backButton.setOnClickListener {
            finish() // Menutup aktivitas dan kembali ke HomeFragment
        }

        // Mendapatkan data profil pengguna
        val token = "Bearer ${SharedPreferencesManager(this).getToken()}"
        getProfileData(token, etNama, etNip, etJabatanSekarang, etUnitKerjaSekarang)

        // Tombol Submit
        btnSubmit.setOnClickListener {
            val nama = etNama.text.toString()
            val nip = etNip.text.toString()
            val jabatanSekarang = etJabatanSekarang.text.toString()
            val unitKerjaSekarang = etUnitKerjaSekarang.text.toString()
            val provinsiTujuan = etProvinsi.text.toString()
            val kabupatenTujuan = etKabupaten.text.toString()
            val jabatanTujuan = etJabatanTujuan.text.toString()
            val unitKerjaTujuan = etUnitKerjaTujuan.text.toString()

            if (nama.isEmpty() || nip.isEmpty() || jabatanSekarang.isEmpty() || unitKerjaSekarang.isEmpty()
                || provinsiTujuan.isEmpty() || kabupatenTujuan.isEmpty()
                || jabatanTujuan.isEmpty() || unitKerjaTujuan.isEmpty()
            ) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Membuat request dengan status default "PENDING"
            val mutationRequest = MutationRequestRequest(
                provinsiTujuan = provinsiTujuan,
                kabupatenTujuan = kabupatenTujuan,
                jabatanTujuan = jabatanTujuan,
                unitKerjaTujuan = unitKerjaTujuan,
                status = "PENDING" // Set status default PENDING
            )

            createMutationRequest(token, mutationRequest)
        }
    }

    private fun getProfileData(token: String, etNama: TextInputEditText, etNip: TextInputEditText,
                               etJabatanSekarang: TextInputEditText, etUnitKerjaSekarang: TextInputEditText) {
        // Membuat instance Retrofit dan memanggil API
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Memanggil API untuk mendapatkan profil pengguna
                val response: Response<User> = apiService.getProfile(token)

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Isi data profil ke dalam form
                        etNama.setText(user.name)
                        etNip.setText(user.nip)
                        etJabatanSekarang.setText(user.jabatan)
                        etUnitKerjaSekarang.setText(user.unitKerja)

                        // Set input fields sebagai read-only
                        etNama.isEnabled = false
                        etNip.isEnabled = false
                        etJabatanSekarang.isEnabled = false
                        etUnitKerjaSekarang.isEnabled = false
                    } else {
                        Toast.makeText(this@CreateMutationRequestActivity, "Gagal memuat data profil", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CreateMutationRequestActivity, "Gagal mengambil data profil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CreateMutationRequestActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createMutationRequest(token: String, mutationRequest: MutationRequestRequest) {
        // Menampilkan loading
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        // Membuat instance Retrofit dan memanggil API
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Memanggil API untuk mengirim pengajuan mutasi
                val response: Response<MutationRequest> = apiService.createMutationRequest(token, mutationRequest)
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@CreateMutationRequestActivity, "Pengajuan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    finish()  // Kembali setelah sukses
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@CreateMutationRequestActivity, "Gagal mengirim pengajuan. Coba lagi.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CreateMutationRequestActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
