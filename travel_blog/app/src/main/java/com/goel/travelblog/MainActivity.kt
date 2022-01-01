package com.goel.travelblog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.goel.travelblog.databinding.ActivityMainBinding
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainTextView.text = "Hello educative.io"

        startActivity(Intent(this, BlogDetailsActivity::class.java))
    }
}