package com.polstat.mutasibps.repository

import android.content.Context
import com.polstat.mutasibps.data.ApiClient
import com.polstat.mutasibps.model.MutationRequest
import com.polstat.mutasibps.model.MutationRequestRequest
import retrofit2.Response

class MutationRequestRepository(context: Context) {

    private val apiService = ApiClient.getApiService(context)

    suspend fun createMutationRequest(token: String, request: MutationRequestRequest): Response<MutationRequest> {
        if (token.isEmpty()) {
            throw IllegalArgumentException("Token tidak boleh kosong")
        }
        return apiService.createMutationRequest("Bearer $token", request)
    }

    suspend fun getUserMutationRequests(token: String): Response<List<MutationRequest>> {
        return apiService.getUserMutationRequests("Bearer $token")
    }

    suspend fun getMutationById(token: String, requestId: Long): Response<MutationRequest> {
        return apiService.getMutationById("Bearer $token", requestId)
    }

    suspend fun getAllMutationRequests(token: String): Response<List<MutationRequest>> {
        return apiService.getAllMutationRequests("Bearer $token")
    }

    suspend fun approveMutationRequest(requestId: Long): Response<String> {
        return apiService.approveMutationRequest(requestId)
    }

    suspend fun rejectMutationRequest(requestId: Long): Response<String> {
        return apiService.rejectMutationRequest(requestId)
    }

    suspend fun deleteMutationRequest(requestId: Long): Response<String> {
        return apiService.deleteMutationRequest(requestId)
    }
}
