package com.example.dejoyafinalassessment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dejoyafinalassessment.databinding.ActivityMainBinding

// this Activity's only job is to host the nav graph - it just holds the
// NavHostFragment in its layout and hands off the keypass extra. All the
// actual screen logic (dashboard, details) lives in the Fragments themselves.
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }
}
