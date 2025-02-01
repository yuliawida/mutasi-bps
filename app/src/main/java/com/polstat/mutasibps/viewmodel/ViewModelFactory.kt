package com.polstat.mutasibps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.polstat.mutasibps.repository.*

class ViewModelFactory(
    private val authRepository: AuthRepository? = null,
    private val userRepository: UserRepository? = null,
    private val mutationRequestRepository: MutationRequestRepository? = null,
    private val approvalLetterRepository: ApprovalLetterRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                if (authRepository == null) throw IllegalArgumentException("AuthRepository must be provided")
                AuthViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                if (userRepository == null) throw IllegalArgumentException("UserRepository must be provided")
                UserViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MutationRequestViewModel::class.java) -> {
                if (mutationRequestRepository == null) throw IllegalArgumentException("MutationRequestRepository must be provided")
                MutationRequestViewModel(mutationRequestRepository) as T
            }
            modelClass.isAssignableFrom(ApprovalLetterViewModel::class.java) -> {
                if (approvalLetterRepository == null) throw IllegalArgumentException("ApprovalLetterRepository must be provided")
                ApprovalLetterViewModel(approvalLetterRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
