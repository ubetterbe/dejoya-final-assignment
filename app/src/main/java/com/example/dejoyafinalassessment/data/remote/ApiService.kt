package com.example.dejoyafinalassessment.data.remote

import com.example.dejoyafinalassessment.data.model.DashboardResponse
import com.example.dejoyafinalassessment.data.model.LoginRequest
import com.example.dejoyafinalassessment.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // "footscray" is our assigned campus/cohort path, not something generic
    @POST("footscray/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
