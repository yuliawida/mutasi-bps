package com.polstat.mutasibps.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.mutasibps.model.User
import com.polstat.mutasibps.model.UserRequest
import com.polstat.mutasibps.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    private val _userProfile = MutableLiveData<User?>()
    val userProfile: LiveData<User?> = _userProfile

    private val _passwordChangeResult = MutableLiveData<Response<Unit>>()
    val passwordChangeResult: LiveData<Response<Unit>> = _passwordChangeResult

    fun changePassword(token: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = repository.changePassword(token, newPassword)
                _passwordChangeResult.value = response
            } catch (e: Exception) {
                Log.e("UserViewModel", "Password change failed: ${e.message}")
            }
        }
    }

    fun getProfile(token: String) {
        viewModelScope.launch {
            try {
                // Memanggil getProfile dari repository
                val response = repository.getProfile(token)
                if (response.isSuccessful) {
                    _userProfile.value = response.body()  // Update LiveData dengan data profil yang berhasil diterima
                } else {
                    Log.e("UserViewModel", "Failed to load profile: ${response.message()}")
                    _userProfile.value = null  // Jika gagal, set LiveData ke null
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error: ${e.message}")
                _userProfile.value = null  // Set null jika ada error jaringan
            }
        }
    }



    fun updateProfile(token: String, userRequest: UserRequest) {
        viewModelScope.launch {
            try {
                val response = repository.updateProfile(token, userRequest)
                if (response.isSuccessful) {
                    getProfile(token) // Refresh data setelah update profil
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Profile update failed: ${e.message}")
            }
        }
    }

    fun deleteAccount(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteAccount(token)
                if (!response.isSuccessful) {
                    Log.e("UserViewModel", "Failed to delete account: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error deleting account: ${e.message}")
            }
        }
    }
}
