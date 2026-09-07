package com.example.dejoyafinalassessment.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dejoyafinalassessment.databinding.ActivityLoginBinding
import com.example.dejoyafinalassessment.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { onLoginClicked() }
    }

    private fun onLoginClicked() {
        val studentId = binding.editStudentId.text.toString()
        val firstName = binding.editFirstName.text.toString()

        // TEMP: just checking the fields aren't blank so we can test the nav flow.
        // Real validation + the actual API call (and proper error/loading states
        // using textError/progressLogin) get wired up in the next step.
        if (studentId.isBlank() || firstName.isBlank()) {
            return
        }

        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
