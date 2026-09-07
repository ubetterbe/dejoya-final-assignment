package com.example.dejoyafinalassessment.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dejoyafinalassessment.databinding.ActivityDashboardBinding

// stub so LoginActivity has somewhere real to navigate to - just proves the
// flow compiles and runs end to end. Actual dashboard (calling the API with
// the keypass, showing results) is a separate step.
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // login already has the keypass from a successful auth call, so it just hands
        // it straight over here - not used yet since this screen's still a stub, but
        // it's ready for the next step (GET /dashboard/{keypass} + showing the sports)
        val keypass = intent.getStringExtra(EXTRA_KEYPASS)
    }

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }
}
