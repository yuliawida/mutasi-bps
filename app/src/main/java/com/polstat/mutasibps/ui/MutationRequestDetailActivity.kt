package com.polstat.mutasibps.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.utils.SharedPreferencesManager
import com.polstat.mutasibps.repository.MutationRequestRepository
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.viewmodel.MutationRequestViewModel
import com.polstat.mutasibps.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MutationRequestDetailActivity : AppCompatActivity() {

    private lateinit var mutationViewModel: MutationRequestViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private lateinit var tvStatus: TextView
    private lateinit var tvNama: TextView
    private lateinit var tvNip: TextView
    private lateinit var tvUnitKerjaSekarang: TextView
    private lateinit var tvJabatanSekarang: TextView
    private lateinit var tvUnitKerjaTujuan: TextView
    private lateinit var tvProvinsiTujuan: TextView
    private lateinit var tvKabupatenTujuan: TextView
    private lateinit var tvJabatanTujuan: TextView
    private lateinit var backButton: ImageButton
    private lateinit var btnDelete: Button

    private var requestId: Long = -1
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mutation_request_detail)

        // Inisialisasi UI
        backButton = findViewById(R.id.backButton)
        tvStatus = findViewById(R.id.tvStatus)
        tvNama = findViewById(R.id.tvNama)
        tvNip = findViewById(R.id.tvNip)
        tvUnitKerjaSekarang = findViewById(R.id.tvUnitKerjaSekarang)
        tvJabatanSekarang = findViewById(R.id.tvJabatanSekarang)
        tvUnitKerjaTujuan = findViewById(R.id.tvUnitKerjaTujuan)
        tvProvinsiTujuan = findViewById(R.id.tvProvinsiTujuan)
        tvKabupatenTujuan = findViewById(R.id.tvKabupatenTujuan)
        tvJabatanTujuan = findViewById(R.id.tvJabatanTujuan)
        btnDelete = findViewById(R.id.btnDelete)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)
        token = sharedPreferencesManager.getToken()

        // Inisialisasi ViewModel
        val repository = MutationRequestRepository(this)
        mutationViewModel = ViewModelProvider(
            this, ViewModelFactory(mutationRequestRepository = repository)
        )[MutationRequestViewModel::class.java]

        // Ambil request_id dari intent
        requestId = intent.getLongExtra("mutation_request_id", -1)
        if (requestId != -1L && token != null) {
            mutationViewModel.getMutationDetail(token!!, requestId)
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
        }

        CoroutineScope(Dispatchers.Main).launch {
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
                    tvNama.text = "Nama: ${user.name}"
                    tvNip.text = "NIP: ${user.nip}"
                    tvUnitKerjaSekarang.text = "Unit Kerja: ${user.unitKerja}"
                    tvJabatanSekarang.text = "Jabatan: ${user.jabatan}"
                }
            }
        }
                mutationViewModel.mutationDetail.observe(this) { response ->
                    if (response.isSuccessful && response.body() != null) {
                        val mutation = response.body()!!
                        tvStatus.text = "Status: ${mutation.status}"
                        tvProvinsiTujuan.text = "Provinsi Tujuan: ${mutation.provinsiTujuan}"
                        tvKabupatenTujuan.text = "Provinsi Tujuan: ${mutation.kabupatenTujuan}"
                        tvUnitKerjaTujuan.text = "Unit Kerja Tujuan: ${mutation.unitKerjaTujuan}"
                        tvJabatanTujuan.text = "Jabatan Tujuan: ${mutation.jabatanTujuan}"

                        // Jika status sudah di-approve atau rejected, sembunyikan tombol hapus
                        if (mutation.status.lowercase() != "pending") {
                            btnDelete.visibility = android.view.View.GONE
                        }
                    }
                }

                // Tombol kembali
                backButton.setOnClickListener {
                    finish()
                }

                // Tombol hapus mutasi
                btnDelete.setOnClickListener {
                    showDeleteConfirmation()
                }
            }

            private fun showDeleteConfirmation() {
                AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus pengajuan mutasi ini? Tindakan ini tidak dapat dibatalkan.")
                    .setPositiveButton("Ya") { _, _ -> deleteMutationRequest() }
                    .setNegativeButton("Batal", null)
                    .show()
            }

            private fun deleteMutationRequest() {
                if (token != null && requestId != -1L) {
                    mutationViewModel.deleteMutationRequest(token!!, requestId)
                    mutationViewModel.deleteMutationResult.observe(this) { response ->
                        if (response.isSuccessful) {
                            Toast.makeText(this, "Pengajuan berhasil dihapus!", Toast.LENGTH_SHORT)
                                .show()
                            finish() // Kembali ke daftar mutasi
                        } else {
                            Toast.makeText(this, "Gagal menghapus pengajuan.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
}
