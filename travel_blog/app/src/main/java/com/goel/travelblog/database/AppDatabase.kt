package com.goel.travelblog.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.goel.travelblog.http.Blog

@Database(entities = [Blog::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun blogDOA(): BlogDOA
}