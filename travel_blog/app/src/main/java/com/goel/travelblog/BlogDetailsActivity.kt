package com.goel.travelblog

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.goel.travelblog.databinding.ActivityBlogDetailsBinding
import com.goel.travelblog.http.Blog

class BlogDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRAS_BLOG = "EXTRAS_BLOG"

        fun start(activity: Activity, blog: Blog) {
            val intent = Intent(activity, BlogDetailsActivity::class.java)
            intent.putExtra(EXTRAS_BLOG, blog)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityBlogDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageBack.setOnClickListener { finish() }

        intent.extras?.getParcelable<Blog>(EXTRAS_BLOG)?.let { blog ->
            showData(blog)
        }
    }


    private fun showData(blog: Blog) {
        binding.progressBar.visibility = View.GONE
        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date
        binding.textAuthor.text = blog.author.name
        binding.textRating.text = blog.rating.toString()
        binding.textViews.text = String.format("(%d views)", blog.views)
        binding.textDescription.text =
            HtmlCompat.fromHtml(blog.description, Html.FROM_HTML_MODE_LEGACY)
        binding.ratingBar.rating = blog.rating
        binding.ratingBar.visibility = View.VISIBLE

        Glide.with(this)
            .load(blog.getImageUrl())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageMain)

        Glide.with(this)
            .load(blog.author.getAvatarUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)
    }
}