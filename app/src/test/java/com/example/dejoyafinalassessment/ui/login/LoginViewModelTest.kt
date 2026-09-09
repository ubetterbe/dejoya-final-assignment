package com.example.dejoyafinalassessment.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dejoyafinalassessment.data.model.LoginRequest
import com.example.dejoyafinalassessment.data.model.LoginResponse
import com.example.dejoyafinalassessment.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class LoginViewModelTest {

    // LiveData posts through an Android main-thread executor that doesn't exist on
    // the JVM - this rule swaps it for one that runs tasks synchronously instead
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: ApiService
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        // viewModelScope launches on Dispatchers.Main, which isn't available in a plain
        // JVM unit test - StandardTestDispatcher lets us control that coroutine by hand
        Dispatchers.setMain(StandardTestDispatcher())
        apiService = mockk()
        viewModel = LoginViewModel(apiService)
    }

    @After
    fun tearDown() {
        // undo setMain so this test doesn't leak a dispatcher into whatever runs next
        Dispatchers.resetMain()
    }

    @Test
    fun `login succeeds and exposes keypass`() = runTest {
        coEvery { apiService.login(any()) } returns LoginResponse(keypass = "sports")

        viewModel.login("123456", "Chris")
        advanceUntilIdle()

        assertEquals(LoginUiState.Success("sports"), viewModel.uiState.value)
    }

    @Test
    fun `login fails with 404 and surfaces the bad-credentials message`() = runTest {
        // the real API returns a plain 404 for wrong student ID/first name (not 401/403),
        // so this is the exact shape LoginViewModel's catch block is built to handle
        val notFound = HttpException(
            Response.error<LoginResponse>(404, "".toResponseBody("application/json".toMediaTypeOrNull()))
        )
        coEvery { apiService.login(any()) } throws notFound

        viewModel.login("000000", "Nobody")
        advanceUntilIdle()

        assertEquals(
            LoginUiState.Error("Login failed, check your student ID and first name"),
            viewModel.uiState.value
        )
    }
}
