package com.polstat.mutasibps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.mutasibps.model.ApprovalLetter
import com.polstat.mutasibps.repository.ApprovalLetterRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ApprovalLetterViewModel(private val repository: ApprovalLetterRepository) : ViewModel() {

    private val _approvalList = MutableLiveData<List<ApprovalLetter>>()
    val approvalList: LiveData<List<ApprovalLetter>> = _approvalList

    private val _createApprovalResult = MutableLiveData<ApprovalLetter?>()
    val createApprovalResult: LiveData<ApprovalLetter?> = _createApprovalResult

    fun createApprovalLetter(token: String?, mutationRequestId: Long?, approvalNumber: String) {
        if (token.isNullOrEmpty() || mutationRequestId == null || mutationRequestId == -1L) {
            _createApprovalResult.postValue(null)
            return
        }

        viewModelScope.launch {
            val response = repository.createApprovalLetter(token, mutationRequestId, approvalNumber)
            if (response.isSuccessful) {
                _createApprovalResult.postValue(response.body())
            } else {
                _createApprovalResult.postValue(null)
            }
        }
    }

    fun getUserApprovalLetters(token: String?) {
        token?.let {
            viewModelScope.launch {
                val response = repository.getUserApprovalLetters(it)
                if (response.isSuccessful) {
                    _approvalList.postValue(response.body())
                }
            }
        }
    }

    fun getAllApprovalLetters(token: String?) {
        token?.let {
            viewModelScope.launch {
                val response = repository.getAllApprovalLetters(it)
                if (response.isSuccessful) {
                    _approvalList.postValue(response.body())
                }
            }
        }
    }
}
