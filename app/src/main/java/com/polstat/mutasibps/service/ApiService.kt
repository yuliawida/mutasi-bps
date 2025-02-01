package com.polstat.mutasibps.service

import com.polstat.mutasibps.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Autentikasi
    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse> // Menggunakan LoginResponse yang baru

    @POST("/auth/register")
    suspend fun register(@Body userRequest: UserRequest): Response<Unit>

    // Profile
    @GET("/users/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<User>

    @PUT("/users/profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body userRequest: UserRequest): Response<User>

    @PUT("/users/password")
    suspend fun changePassword(@Header("Authorization") token: String, @Query("newPassword") newPassword: String): Response<Unit>

    @DELETE("/users/delete")
    suspend fun deleteAccount(@Header("Authorization") token: String): Response<Unit>

    // Mutation Requests
    @POST("/mutations")
    suspend fun createMutationRequest(@Header("Authorization") token: String, @Body mutationRequestRequest: MutationRequestRequest): Response<MutationRequest>

    @GET("/mutations")
    suspend fun getUserMutationRequests(@Header("Authorization") token: String): Response<List<MutationRequest>>

    @GET("/mutations/{id}")
    suspend fun getMutationById(@Header("Authorization") token: String, @Path("id") requestId: Long): Response<MutationRequest>

    @GET("/mutations/all")
    suspend fun getAllMutationRequests(@Header("Authorization") token: String): Response<List<MutationRequest>>

    @POST("/mutations/approve/{id}")
    suspend fun approveMutationRequest(@Path("id") requestId: Long): Response<String>

    @POST("/mutations/reject/{id}")
    suspend fun rejectMutationRequest(@Path("id") requestId: Long): Response<String>

    @DELETE("/mutations/delete/{id}")
    suspend fun deleteMutationRequest(@Path("id") requestId: Long): Response<String>

    // Approval Letters
    @GET("/approval-letters")
    suspend fun getAllApprovalLetters(@Header("Authorization") token: String): Response<List<ApprovalLetter>>

    @POST("/approval-letters/create")
    suspend fun createApprovalLetter(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, String> // Backend menerima Map, bukan ApprovalLetterRequest
    ): Response<ApprovalLetter>

    @GET("/approval-letters/user")
    suspend fun getUserApprovalLetters(@Header("Authorization") token: String): Response<List<ApprovalLetter>>

}
