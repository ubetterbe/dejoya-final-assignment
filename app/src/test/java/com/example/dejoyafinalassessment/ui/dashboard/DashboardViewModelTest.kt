package com.example.dejoyafinalassessment.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.dejoyafinalassessment.data.model.DashboardResponse
import com.example.dejoyafinalassessment.data.model.Entity
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
import java.io.IOException

class DashboardViewModelTest {

    // same deal as LoginViewModelTest - LiveData.setValue needs a main thread that
    // doesn't exist in a JVM unit test, so this rule fakes one
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var apiService: ApiService
    private lateinit var viewModel: DashboardViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        apiService = mockk()
        viewModel = DashboardViewModel(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDashboard succeeds and exposes the entity list`() = runTest {
        // real shape for my "sports" topic, not the assignment's generic placeholder fields
        val entities = listOf(
            Entity(
                sportName = "Basketball",
                playerCount = 5,
                fieldType = "Indoor court",
                olympicSport = true,
                description = "Team sport played on a hardwood court"
            )
        )
        coEvery { apiService.getDashboard(any()) } returns DashboardResponse(
            entities = entities,
            entityTotal = entities.size
        )

        viewModel.loadDashboard("sports")
        advanceUntilIdle()

        assertEquals(DashboardUiState.Success(entities), viewModel.uiState.value)
    }

    @Test
    fun `loadDashboard fails when there's no response at all`() = runTest {
        // mirrors the Render cold-start case - no HTTP response, just a connection failure
        coEvery { apiService.getDashboard(any()) } throws IOException("timeout")

        viewModel.loadDashboard("sports")
        advanceUntilIdle()

        assertEquals(
            DashboardUiState.Error("Couldn't connect, the server might be waking up - try again in a moment"),
            viewModel.uiState.value
        )
    }

    @Test
    fun `loadDashboard fails on a non-2xx response and surfaces the status code`() = runTest {
        val serverError = HttpException(
            Response.error<DashboardResponse>(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
        )
        coEvery { apiService.getDashboard(any()) } throws serverError

        viewModel.loadDashboard("sports")
        advanceUntilIdle()

        assertEquals(
            DashboardUiState.Error("Couldn't load your dashboard (server said 500), try again"),
            viewModel.uiState.value
        )
    }
}
