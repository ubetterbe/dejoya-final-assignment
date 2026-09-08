package com.example.dejoyafinalassessment.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dejoyafinalassessment.data.model.Entity
import com.example.dejoyafinalassessment.data.remote.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// same shape as LoginUiState - Fragment just renders whichever of these it gets
sealed class DashboardUiState {
    object Idle : DashboardUiState()
    object Loading : DashboardUiState()
    data class Success(val entities: List<Entity>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableLiveData<DashboardUiState>(DashboardUiState.Idle)
    val uiState: LiveData<DashboardUiState> = _uiState

    fun loadDashboard(keypass: String) {
        _uiState.value = DashboardUiState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getDashboard(keypass)
                _uiState.value = DashboardUiState.Success(response.entities)
            } catch (e: HttpException) {
                // unlike auth, we don't have a specific "this code means X" quirk
                // confirmed for this endpoint yet, so just surface the status
                _uiState.value = DashboardUiState.Error(
                    "Couldn't load your dashboard (server said ${e.code()}), try again"
                )
            } catch (e: IOException) {
                // same Render cold-start story as login - no response at all usually
                // means the free-tier instance is still spinning back up
                _uiState.value = DashboardUiState.Error(
                    "Couldn't connect, the server might be waking up - try again in a moment"
                )
            }
        }
    }
}
