package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.MutationRequest
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.ui.adapters.MutationRequestAdapter
import com.polstat.mutasibps.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MutationListActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MutationRequestAdapter
    private var userRole: String? = null
    private var token: String? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mutation_list)

        // Inisialisasi UI
        recyclerView = findViewById(R.id.rvMutationRequests)
        bottomNavigationView = findViewById(R.id.bottomNavigationMutation)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager(this)
        userRole = sharedPreferencesManager.getRole()
        token = sharedPreferencesManager.getToken()

        if (token == null) {
            Toast.makeText(this, "Token tidak ditemukan, silakan login ulang.", Toast.LENGTH_SHORT).show()
            return
        }

        // Inisialisasi adapter
        adapter = MutationRequestAdapter(emptyList()) { mutationRequest ->
            navigateToDetail(mutationRequest)
        }
        recyclerView.adapter = adapter

        // Ambil data mutasi berdasarkan role
        loadMutationRequests()

        // Menangani pemilihan menu dari BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_user_home -> {
                    if (userRole == "ROLE_ADMIN") {
                        val intent = Intent(this, HomeAdminActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.nav_user_mutation_list -> {
                    // Stay in MutationListActivity
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

    private fun loadMutationRequests() {
        token?.let {
            if (userRole == "ROLE_ADMIN") {
                fetchMutations(it, true)  // Admin requests all mutations
            } else {
                fetchMutations(it, false)  // User requests own mutations
            }
        }
    }

    private fun fetchMutations(token: String, isAdmin: Boolean) {
        // Membuat instance Retrofit dan memanggil API
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.9:8080/") // Ganti dengan URL API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response: Response<List<MutationRequest>> = if (isAdmin) {
                    apiService.getAllMutationRequests("Bearer $token")
                } else {
                    apiService.getUserMutationRequests("Bearer $token")
                }

                if (response.isSuccessful) {
                    val mutationList = response.body() ?: emptyList()
                    adapter.updateData(mutationList)
                } else {
                    Toast.makeText(this@MutationListActivity, "Gagal mengambil data mutasi", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MutationListActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDetail(mutationRequest: MutationRequest) {
        val intent = if (userRole == "ROLE_ADMIN") {
            Intent(this, MutationRequestDetailAdminActivity::class.java)
        } else {
            Intent(this, MutationRequestDetailActivity::class.java)
        }
        intent.putExtra("mutation_request_id", mutationRequest.id)
        startActivity(intent)
    }
}
