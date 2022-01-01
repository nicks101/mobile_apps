package com.goel.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.goel.travelblog.databinding.ActivityBlogDetailsBinding
import com.goel.travelblog.http.Blog
import com.goel.travelblog.http.BlogHttpClient
import com.google.android.material.snackbar.Snackbar

class BlogDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener { finish() }

        loadData()
    }

    private fun loadData() {
        BlogHttpClient.loadBlogArticles(onSuccess = { list: List<Blog> ->
            runOnUiThread {
                showData(list[0])
            }
        }, onError = {
            runOnUiThread {
                showErrorSnackbar()
            }
        })
    }

    private fun showErrorSnackbar() {
        Snackbar.make(
            binding.root,
            "Error during loading blog articles",
            Snackbar.LENGTH_INDEFINITE
        ).run {
            setActionTextColor(resources.getColor(R.color.orange500))
            setAction("Retry") {
                loadData()
                dismiss()
            }
        }.show()
    }

    private fun showData(blog: Blog) {
        binding.progressBar.visibility = View.GONE
        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date
        binding.textAuthor.text = blog.author.name
        binding.textRating.text = blog.rating.toString()
        binding.textViews.text = String.format("(%d views)", blog.views)
//        binding.textDescription.text = blog.description
        binding.textDescription.text = Html.fromHtml(blog.description)
        binding.ratingBar.rating = blog.rating
        binding.ratingBar.visibility = View.VISIBLE

        Glide.with(this)
            .load(blog.image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMain)

        Glide.with(this)
            .load(blog.author.avatar)
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)
    }
}