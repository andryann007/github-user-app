package com.andryan.githubuser.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andryan.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1, exportSchema = false)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao
}