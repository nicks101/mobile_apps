package com.goel.travelblog

import androidx.appcompat.app.AppCompatActivity
import com.goel.travelblog.databinding.ActivityLoginBinding
import android.os.Bundle

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}