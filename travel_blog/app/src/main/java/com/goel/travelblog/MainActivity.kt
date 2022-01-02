package com.goel.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.goel.travelblog.adapter.MainAdapter
import com.goel.travelblog.databinding.ActivityMainBinding
import com.goel.travelblog.http.Blog
import com.goel.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter { blog ->
        BlogDetailsActivity.start(this, blog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.refresh.setOnRefreshListener {
            loadData()
        }

        loadData()
    }

    private fun loadData() {
        binding.refresh.isRefreshing = true
        BlogHttpClient.loadBlogArticles(
            onSuccess = { blogList: List<Blog> ->
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    adapter.submitList(blogList)
                }
            },
            onError = {
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    showErrorSnackbar()
                }
            }
        )
    }

    private fun showErrorSnackbar() {
        Snackbar.make(
            binding.root,
            "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE
        ).run {
            setActionTextColor(resources.getColor(R.color.orange500))
            setAction("Retry") {
                loadData()
                dismiss()
            }
        }.show()
    }
}