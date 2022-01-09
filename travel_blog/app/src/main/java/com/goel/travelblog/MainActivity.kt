package com.goel.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.goel.travelblog.adapter.MainAdapter
import com.goel.travelblog.databinding.ActivityMainBinding
import com.goel.travelblog.http.Blog
import com.goel.travelblog.http.BlogHttpClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SORT_TITLE = 0
        private const val SORT_DATE = 1
    }

    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter { blog ->
        BlogDetailsActivity.start(this, blog)
    }
    private var currentSort = SORT_DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.refresh.setOnRefreshListener {
            loadData()
        }

        binding.materialToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sort) {
                Log.i("MainActivity", "clicked on sort")
                onSortClicked()
            }
            false
        }

        loadData()
    }

    private fun onSortClicked() {
        val items = arrayOf("Title", "Date")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort Order")
            .setSingleChoiceItems(items, currentSort) { dialog, which ->
                dialog.dismiss()
                currentSort = which
                sortData()
            }.show()
    }

    private fun sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle()
        } else {
            adapter.sortByDate()
        }
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