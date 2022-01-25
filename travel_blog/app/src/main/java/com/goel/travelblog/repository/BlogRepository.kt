package com.goel.travelblog.repository

import android.content.Context
import com.goel.travelblog.database.DatabaseProvider
import com.goel.travelblog.http.Blog
import com.goel.travelblog.http.BlogHttpClient
import java.util.concurrent.Executors

class BlogRepository(context: Context) {
    private val httpClient = BlogHttpClient
    private val database = DatabaseProvider.getInstance(context.applicationContext)
    private val executor = Executors.newSingleThreadExecutor()


    // Unit is equivalent to void data type in Java
    /**
     * callback: (List<Blog>) -> Unit
     * is
     * void callback(List<Blog>)
     */
    fun loadFromDatabase(callback: (List<Blog>) -> Unit) {

        // Work on the background thread
        executor.execute {
            callback(database.blogDoa().getAll())
        }
    }

    fun loadFromNetwork(onSuccess: (List<Blog>) -> Unit, onError: () -> Unit) {

        // [execute] method's last (and only in this case) parameter is a function.
        // Hence we can use it directly instead of inside parenthesis ()
        /***
         * executor.execute({})
         */
        executor.execute {
            val blogList = httpClient.loadBlogArticles()
            if (blogList == null) onError()
            else {
                val blogDOA = database.blogDoa()
                blogDOA.deleteAll()
                blogDOA.insertAll(blogList)
                onSuccess(blogList)
            }
        }
    }
}