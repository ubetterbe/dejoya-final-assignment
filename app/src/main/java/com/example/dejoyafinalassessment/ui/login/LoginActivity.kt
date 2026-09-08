package com.example.dejoyafinalassessment.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dejoyafinalassessment.MainActivity
import com.example.dejoyafinalassessment.databinding.ActivityLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { onLoginClicked() }
        viewModel.uiState.observe(this) { state -> render(state) }
    }

    private fun onLoginClicked() {
        // username = student ID with NO leading "s" (e.g. 12345678, not s12345678),
        // password = first name and IS case-sensitive. This is the professor's
        // email correction, not what the original brief PDF says - don't "fix"
        // this back to match the brief.
        val studentId = binding.editStudentId.text.toString()
        val firstName = binding.editFirstName.text.toString()
        viewModel.login(studentId, firstName)
    }

    private fun render(state: LoginUiState) {
        binding.progressLogin.visibility = if (state is LoginUiState.Loading) View.VISIBLE else View.GONE

        when (state) {
            is LoginUiState.Success -> {
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra(MainActivity.EXTRA_KEYPASS, state.keypass)
                }
                startActivity(intent)
                finish()
            }

            is LoginUiState.Error -> {
                binding.textError.text = state.message
                binding.textError.visibility = View.VISIBLE
            }

            LoginUiState.Loading, LoginUiState.Idle -> {
                // nothing extra to show here beyond the progress bar handled above
                binding.textError.visibility = View.GONE
            }
        }
    }
}
