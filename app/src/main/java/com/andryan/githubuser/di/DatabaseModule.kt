package com.andryan.githubuser.di

import android.content.Context
import androidx.room.Room
import com.andryan.githubuser.data.local.room.FavoriteUserDao
import com.andryan.githubuser.data.local.room.FavoriteUserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideFavoriteUserDatabase(@ApplicationContext context: Context): FavoriteUserDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FavoriteUserDatabase::class.java,
            "db_github_user.db"
        ).build()
    }

    @Provides
    fun provideFavoriteUserDao(githubUserDatabase: FavoriteUserDatabase): FavoriteUserDao =
        githubUserDatabase.favoriteUserDao()
}