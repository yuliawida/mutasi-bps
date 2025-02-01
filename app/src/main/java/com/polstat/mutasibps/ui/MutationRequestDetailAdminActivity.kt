package com.polstat.mutasibps.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.polstat.mutasibps.R
import com.polstat.mutasibps.utils.SharedPreferencesManager
import com.polstat.mutasibps.repository.MutationRequestRepository
import com.polstat.mutasibps.viewmodel.MutationRequestViewModel
import com.polstat.mutasibps.viewmodel.ViewModelFactory

class MutationRequestDetailAdminActivity : AppCompatActivity() {

    private lateinit var mutationViewModel: MutationRequestViewModel
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    private lateinit var tvStatus: TextView
    private lateinit var tvNama: TextView
    private lateinit var tvNip: TextView
    private lateinit var tvUnitKerjaSekarang: TextView
    private lateinit var tvUnitKerjaTujuan: TextView
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
        initUI()

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

        if (requestId != -1L) {
            token?.let {
                mutationViewModel.getMutationDetail(it, requestId)
            } ?: run {
                Toast.makeText(this, "Token tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Observasi data mutasi
        mutationViewModel.mutationDetail.observe(this) { response ->
            response.body()?.let { mutation ->
                tvStatus.text = "Status: ${mutation.status}"
                tvNama.text = "Nama: ${mutation.user?.name ?: "Tidak Diketahui"}"
                tvNip.text = "NIP: ${mutation.user?.nip ?: "-"}"
                tvUnitKerjaSekarang.text = "Unit Kerja Sekarang: ${mutation.user?.unitKerja ?: "-"}"
                tvUnitKerjaTujuan.text = "Unit Kerja Tujuan: ${mutation.unitKerjaTujuan}"
                tvJabatanTujuan.text = "Jabatan Tujuan: ${mutation.jabatanTujuan}"

                if (mutation.status.lowercase() != "pending") {
                    btnDelete.visibility = android.view.View.GONE
                }
            } ?: run {
                Toast.makeText(this, "Gagal mengambil data mutasi.", Toast.LENGTH_SHORT).show()
                finish()
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
        token?.let { safeToken ->
            mutationViewModel.deleteMutationRequest(safeToken, requestId)
            mutationViewModel.deleteMutationResult.observe(this) { response ->
                if (response.isSuccessful) {
                    Toast.makeText(this, "Pengajuan berhasil dihapus!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menghapus pengajuan.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initUI() {
        backButton = findViewById(R.id.backButton)
        tvStatus = findViewById(R.id.tvStatus)
        tvNama = findViewById(R.id.tvNama)
        tvNip = findViewById(R.id.tvNip)
        tvUnitKerjaSekarang = findViewById(R.id.tvUnitKerjaSekarang)
        tvUnitKerjaTujuan = findViewById(R.id.tvUnitKerjaTujuan)
        tvJabatanTujuan = findViewById(R.id.tvJabatanTujuan)
        btnDelete = findViewById(R.id.btnDelete)
    }
}
