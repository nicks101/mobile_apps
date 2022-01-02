package com.goel.travelblog.http

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.Executors

object BlogHttpClient {

    const val BASE_URL = "https://bitbucket.org/dmytrodanylyk/travel-blog-resources"
    const val PATH = "/raw/3eede691af3e8ff795bf6d31effb873d484877be"

    private const val BLOG_ARTICLES_URL = "$BASE_URL$PATH/blog_articles.json"

    private val executor = Executors.newFixedThreadPool(4)
    private val client = OkHttpClient()
    private val gson = Gson()

    fun loadBlogArticles(onSuccess: (List<Blog>) -> Unit, onError: () -> Unit) {
        val request = Request.Builder().get().url(BLOG_ARTICLES_URL).build()


        executor.execute {
            runCatching {
                val response: Response = client.newCall(request).execute()
                response.body?.string()?.let { json ->
                    gson.fromJson(json, BlogData::class.java)?.let { blogData ->
                        return@runCatching blogData.data
                    }
                }
            }.onFailure { e: Throwable ->
                Log.e("BlogHttpClient", "Error loading blog articles", e)
                onError()
            }.onSuccess { value: List<Blog>? ->
                onSuccess(value ?: emptyList())
            }
        }
    }
}