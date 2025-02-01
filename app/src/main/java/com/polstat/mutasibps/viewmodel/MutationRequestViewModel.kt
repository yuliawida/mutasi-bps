package com.polstat.mutasibps.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.mutasibps.model.MutationRequest
import com.polstat.mutasibps.model.MutationRequestRequest
import com.polstat.mutasibps.repository.MutationRequestRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MutationRequestViewModel(private val repository: MutationRequestRepository) : ViewModel() {

    private val _mutationList = MutableLiveData<Response<List<MutationRequest>>>()
    val mutationList: LiveData<Response<List<MutationRequest>>> get() = _mutationList

    private val _mutationDetail = MutableLiveData<Response<MutationRequest>>()
    val mutationDetail: LiveData<Response<MutationRequest>> get() = _mutationDetail

    private val _createMutationResult = MutableLiveData<Response<MutationRequest>>()
    val createMutationResult: LiveData<Response<MutationRequest>> get() = _createMutationResult

    private val _deleteMutationResult = MutableLiveData<Response<String>>()
    val deleteMutationResult: LiveData<Response<String>> get() = _deleteMutationResult

    private val _approveMutationResult = MutableLiveData<Response<String>>()
    val approveMutationResult: LiveData<Response<String>> get() = _approveMutationResult

    private val _rejectMutationResult = MutableLiveData<Response<String>>()
    val rejectMutationResult: LiveData<Response<String>> get() = _rejectMutationResult

    private val _updateMutationResult = MutableLiveData<Response<MutationRequest>>()
    val updateMutationResult: LiveData<Response<MutationRequest>> get() = _updateMutationResult

    fun getUserMutations(token: String) {
        viewModelScope.launch {
            val response = repository.getUserMutationRequests(token)
            if (response.isSuccessful) {
                _mutationList.postValue(response)
            }
        }
    }

    fun getAllMutations(token: String) {
        viewModelScope.launch {
            val response = repository.getAllMutationRequests(token)
            if (response.isSuccessful) {
                _mutationList.postValue(response)
            }
        }
    }

    fun getMutationDetail(token: String, requestId: Long) {
        viewModelScope.launch {
            val response = repository.getMutationById(token, requestId)
            if (response.isSuccessful) {
                _mutationDetail.postValue(response)
            } else {
                _mutationDetail.postValue(Response.error(404, okhttp3.ResponseBody.create(null, "Not Found")))
            }
        }
    }


    fun createMutationRequest(token: String, request: MutationRequestRequest) {
        viewModelScope.launch {
            val response = repository.createMutationRequest(token, request)
            if (response.isSuccessful) {
                _createMutationResult.postValue(response)
                getUserMutations(token) // Refresh daftar setelah membuat permohonan baru
            }
        }
    }

    fun deleteMutationRequest(token: String, requestId: Long) {
        viewModelScope.launch {
            val response = repository.deleteMutationRequest(requestId)
            if (response.isSuccessful) {
                _deleteMutationResult.postValue(response)
                getUserMutations(token) // Refresh daftar setelah mutasi dihapus
            }
        }
    }

    fun approveMutationRequest(token: String, requestId: Long) {
        viewModelScope.launch {
            val response = repository.approveMutationRequest(requestId)
            if (response.isSuccessful) {
                _approveMutationResult.postValue(response)
                getAllMutations(token) // Refresh daftar setelah persetujuan
            }
        }
    }

    fun rejectMutationRequest(token: String, requestId: Long) {
        viewModelScope.launch {
            val response = repository.rejectMutationRequest(requestId)
            if (response.isSuccessful) {
                _rejectMutationResult.postValue(response)
                getAllMutations(token) // Refresh daftar setelah penolakan
            }
        }
    }
}
