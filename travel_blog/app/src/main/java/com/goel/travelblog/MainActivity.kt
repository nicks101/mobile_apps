package com.goel.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.goel.travelblog.adapter.MainAdapter
import com.goel.travelblog.databinding.ActivityMainBinding
import com.goel.travelblog.http.Blog
import com.goel.travelblog.http.BlogHttpClient
import com.goel.travelblog.repository.BlogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    // [companion object] is a singleton. Here it's used to serve its members as static members
    companion object {
        private const val SORT_TITLE = 0
        private const val SORT_DATE = 1
    }

    // [lazy] is a delegate that returns the result lazily and remembers it for next calls.
    private val repository by lazy { BlogRepository(applicationContext) }
    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter { blog ->
        BlogDetailsActivity.start(this, blog)
    }
    private var currentSort = SORT_DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sort) {
                Log.i("MainActivity", "clicked on sort")
                onSortClicked()
            }
            false
        }

        val searchItem = binding.materialToolbar.menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return false
                adapter.filter(newText)
                return true
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.refresh.setOnRefreshListener {
            loadDataFromNetwork()
        }

        loadDataFromDatabase()
        loadDataFromNetwork()
    }

    private fun loadDataFromDatabase() {
        repository.loadFromDatabase { blogList: List<Blog> ->
            runOnUiThread {
                adapter.setData(blogList)
                sortData()
            }
        }
    }

    private fun loadDataFromNetwork() {
        binding.refresh.isRefreshing = true
        repository.loadFromNetwork(
            onSuccess = { blogList: List<Blog> ->
                runOnUiThread {
                    binding.refresh.isRefreshing = false
                    adapter.setData(blogList)
                    sortData()
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


    private fun showErrorSnackbar() {
        Snackbar.make(
            binding.root,
            "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE
        ).run {
            setActionTextColor(resources.getColor(R.color.orange500))
            setAction("Retry") {
                loadDataFromNetwork()
                dismiss()
            }
        }.show()
    }
}