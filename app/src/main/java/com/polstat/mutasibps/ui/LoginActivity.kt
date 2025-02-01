package com.polstat.mutasibps.ui


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.polstat.mutasibps.R
import com.polstat.mutasibps.utils.SharedPreferencesManager
import com.polstat.mutasibps.model.LoginRequest
import com.polstat.mutasibps.repository.AuthRepository
import com.polstat.mutasibps.viewmodel.AuthViewModel
import com.polstat.mutasibps.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferencesManager
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
        setContentView(R.layout.activity_login)

        sharedPref = SharedPreferencesManager(this)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edtUsername = findViewById<EditText>(R.id.etUsername)
        val edtPassword = findViewById<EditText>(R.id.etPassword)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(LoginRequest(username, password))
            } else {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.loginResult.observe(this, Observer { response ->
            if (response.isSuccessful) {
                val token = response.body()?.token
                val role = response.body()?.user?.role // Ambil role dari response
                val username = response.body()?.user?.username
                token?.let {
                    sharedPref.saveToken(it) // Simpan token
                    role?.let {
                        sharedPref.saveRole(it) // Simpan role
                    }
                    username?.let {
                        sharedPref.saveUsername(it)
                    }

                    // Periksa role dan arahkan ke halaman yang sesuai
                    val intent = when (sharedPref.getRole()) {
                        "ADMIN" -> Intent(this, HomeAdminActivity::class.java) // Halaman Admin
                        else -> Intent(this, HomeActivity::class.java) // Halaman User
                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()  // Menutup LoginActivity
                }
            } else {
                Toast.makeText(this, "Username atau Password Salah", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
