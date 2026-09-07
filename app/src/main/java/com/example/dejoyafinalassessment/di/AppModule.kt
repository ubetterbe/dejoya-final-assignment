package com.example.dejoyafinalassessment.di

import com.example.dejoyafinalassessment.data.remote.ApiService
import com.example.dejoyafinalassessment.data.remote.NetworkConfig
import com.example.dejoyafinalassessment.ui.login.LoginViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// everything network/DI related lives here so viewmodels can just ask koin for
// an ApiService instead of building retrofit themselves every time
val appModule = module {

    single {
        HttpLoggingInterceptor().apply {
            // logs full request/response bodies - handy for checking what the
            // API actually sent back when something looks off
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    // LoginActivity just asks koin for this via `by viewModel()` instead of building it itself
    viewModel { LoginViewModel(get()) }
}
