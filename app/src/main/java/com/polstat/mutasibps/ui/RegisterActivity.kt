package com.polstat.mutasibps.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.UserRequest
import com.polstat.mutasibps.model.Role
import com.polstat.mutasibps.repository.AuthRepository
import com.polstat.mutasibps.viewmodel.AuthViewModel
import com.polstat.mutasibps.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory(
            authRepository = AuthRepository(this),
            userRepository = null,
            mutationRequestRepository = null,
            approvalLetterRepository = null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Bind UI Elements
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etNIP = findViewById<TextInputEditText>(R.id.etNIP)
        val etUnitKerja = findViewById<TextInputEditText>(R.id.etUnitKerja)
        val etJabatan = findViewById<TextInputEditText>(R.id.etJabatan)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val etRole = findViewById<AutoCompleteTextView>(R.id.etRole)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        // Setup Role Dropdown
        val roles = listOf("USER", "ADMIN")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, roles)
        etRole.setAdapter(adapter)
        etRole.setOnClickListener { etRole.showDropDown() }

        // Pindah ke LoginActivity saat tvLogin diklik
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Button Register
        btnRegister.setOnClickListener {
            val username = etUsername.text.toString()
            val name = etName.text.toString()
            val nip = etNIP.text.toString()
            val unitKerja = etUnitKerja.text.toString()
            val jabatan = etJabatan.text.toString()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val role = etRole.text.toString()

            // Validasi input
            if (username.isEmpty() || name.isEmpty() || nip.isEmpty() || unitKerja.isEmpty() ||
                jabatan.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()
            ) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat permintaan registrasi
            val userRequest = UserRequest(
                username = username,
                password = password,
                name = name,
                nip = nip,
                jabatan = jabatan,
                unitKerja = unitKerja,
                role = Role.valueOf(role)
            )

            // Kirim request registrasi ke ViewModel
            authViewModel.register(userRequest)
        }

        // Observe hasil registrasi
        authViewModel.registerResult.observe(this) { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registrasi gagal, coba lagi.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
