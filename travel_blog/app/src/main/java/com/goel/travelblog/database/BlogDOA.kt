package com.goel.travelblog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.goel.travelblog.http.Blog

@Dao
interface BlogDOA {

    @Query("SELECT * FROM blog")
    fun getAll(): List<Blog>

    @Insert
    fun insertAll(blogList: List<Blog>)

    @Query("DELETE FROM blog")
    fun deleteAll()
}