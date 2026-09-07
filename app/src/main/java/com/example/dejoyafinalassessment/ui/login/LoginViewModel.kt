package com.example.dejoyafinalassessment.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dejoyafinalassessment.data.model.LoginRequest
import com.example.dejoyafinalassessment.data.remote.ApiService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// whatever the login screen is doing at a given moment - Activity just renders
// whichever one of these it gets handed, no need for it to know about retrofit/coroutines
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val keypass: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(private val apiService: ApiService) : ViewModel() {

    private val _uiState = MutableLiveData<LoginUiState>(LoginUiState.Idle)
    val uiState: LiveData<LoginUiState> = _uiState

    fun login(studentId: String, firstName: String) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(studentId, firstName))
                _uiState.value = LoginUiState.Success(response.keypass)
            } catch (e: HttpException) {
                // Confirmed this against the real API with curl: wrong credentials come
                // back as a plain 404, not the 401/403 you'd normally expect for a bad
                // login. So a 404 here specifically means "bad student ID/first name",
                // not "route doesn't exist" - flagging this since it's non-standard and
                // easy to misread later.
                _uiState.value = if (e.code() == 404) {
                    LoginUiState.Error("Login failed, check your student ID and first name")
                } else {
                    LoginUiState.Error("Something went wrong (server said ${e.code()}), try again")
                }
            } catch (e: IOException) {
                // no HTTP response at all - either no internet, or (more likely for us)
                // Render's free tier waking the API back up from a cold start, which is
                // also why NetworkConfig's timeouts are as generous as they are
                _uiState.value = LoginUiState.Error(
                    "Couldn't connect, the server might be waking up - try again in a moment"
                )
            }
        }
    }
}
