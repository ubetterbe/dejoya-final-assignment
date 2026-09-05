package com.example.dejoyafinalassessment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dejoyafinalassessment.databinding.ActivityMainBinding

// just a placeholder for now so the app has something to launch - this becomes
// (or gets swapped for) the login screen once that's built
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
