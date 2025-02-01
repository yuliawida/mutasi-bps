package com.polstat.mutasibps.repository

import android.content.Context
import com.polstat.mutasibps.data.ApiClient
import com.polstat.mutasibps.model.ApprovalLetter
import retrofit2.Response

class ApprovalLetterRepository(context: Context) {

    private val apiService = ApiClient.getApiService(context)

    // Menambahkan penanganan kesalahan jika requestBody tidak valid
    suspend fun createApprovalLetter(token: String, mutationRequestId: Long, approvalNumber: String): Response<ApprovalLetter> {
        val requestBody = mapOf(
            "mutationRequestId" to mutationRequestId.toString(),
            "approvalNumber" to approvalNumber
        )

        if (mutationRequestId == null || approvalNumber.isEmpty()) {
            throw IllegalArgumentException("Mutation Request ID dan Nomor Persetujuan harus disediakan.")
        }

        return apiService.createApprovalLetter("Bearer $token", requestBody)
    }

    suspend fun getUserApprovalLetters(token: String): Response<List<ApprovalLetter>> {
        return apiService.getUserApprovalLetters("Bearer $token")
    }

    suspend fun getAllApprovalLetters(token: String): Response<List<ApprovalLetter>> {
        return apiService.getAllApprovalLetters("Bearer $token")
    }
}
