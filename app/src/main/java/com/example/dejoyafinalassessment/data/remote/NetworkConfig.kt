package com.example.dejoyafinalassessment.data.remote

// just constants for the network setup, actual OkHttp/Retrofit objects get built in AppModule
object NetworkConfig {
    const val BASE_URL = "https://nit3213apinew.onrender.com/"

    // the API is on Render's free tier, which spins down when idle and can take a
    // while to wake back up on the first request - giving it more time here so
    // we don't get timeout errors on a cold start instead of a real error
    const val CONNECT_TIMEOUT_SECONDS: Long = 20
    const val READ_TIMEOUT_SECONDS: Long = 30
}
