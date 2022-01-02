package com.goel.travelblog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.goel.travelblog.databinding.ItemMainBinding
import com.goel.travelblog.http.Blog

class MainAdapter(private val onItemClickListener: (Blog) -> Unit) :
    ListAdapter<Blog, MainViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}

class MainViewHolder(
    private val binding: ItemMainBinding,
    private val onItemClickListener: (Blog) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(blog: Blog) {
        binding.root.setOnClickListener { onItemClickListener(blog) }
        binding.textTitle.text = blog.title
        binding.textDate.text = blog.date

        Glide.with(itemView)
            .load(blog.author.getAvatarUrl())
            .transform(CircleCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.imageAvatar)
    }
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Blog> = object : DiffUtil.ItemCallback<Blog>() {
    override fun areItemsTheSame(
        oldData: Blog,
        newData: Blog
    ): Boolean {
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(
        oldData: Blog,
        newData: Blog
    ): Boolean {
        return oldData == newData
    }
}