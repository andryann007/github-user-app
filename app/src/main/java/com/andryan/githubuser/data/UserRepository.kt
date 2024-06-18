package com.andryan.githubuser.data

import android.content.ContentValues.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.andryan.githubuser.data.local.entity.FavoriteUser
import com.andryan.githubuser.data.local.room.FavoriteUserDao
import com.andryan.githubuser.data.remote.response.UserDetail
import com.andryan.githubuser.data.remote.response.UserFollowers
import com.andryan.githubuser.data.remote.response.UserFollowing
import com.andryan.githubuser.data.remote.response.UserList
import com.andryan.githubuser.data.remote.retrofit.ApiService
import com.andryan.githubuser.utils.Result
import com.andryan.githubuser.data.pref.SettingPreferences
import com.andryan.githubuser.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val settingPreferences: SettingPreferences
) {
    fun searchUsers(q: String): LiveData<Result<UserList>> = liveData {
        wrapEspressoIdlingResource {
            emit(Result.Loading)
            try {
                val users = apiService.searchUsers(q)
                emit(Result.Success(users))
            } catch (e: Exception) {
                Timber.tag(TAG).d("searchUsers: %s", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getUserDetail(username: String): LiveData<Result<UserDetail>> = liveData {
        wrapEspressoIdlingResource {
            emit(Result.Loading)
            try {
                val users = apiService.getDetailUser(username)
                emit(Result.Success(users))
            } catch (e: Exception) {
                Timber.tag(TAG).d("getUserDetail: %s", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getUserFollowing(username: String): LiveData<Result<List<UserFollowing>>> = liveData {
        wrapEspressoIdlingResource {
            emit(Result.Loading)
            try {
                val users = apiService.getUserFollowing(username)
                emit(Result.Success(users))
            } catch (e: Exception) {
                Timber.tag(TAG).d("getUserFollowing: %s", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getUserFollowers(username: String): LiveData<Result<List<UserFollowers>>> = liveData {
        wrapEspressoIdlingResource {
            emit(Result.Loading)
            try {
                val users = apiService.getUserFollowers(username)
                emit(Result.Success(users))
            } catch (e: Exception) {
                Timber.tag(TAG).d("getUserFollowers: %s", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getAllFavoriteUser()
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return favoriteUserDao.getFavoriteUserByUsername(username)
    }

    suspend fun addFavoriteUser(user: FavoriteUser) {
        return favoriteUserDao.insertUser(user)
    }

    suspend fun deleteFavoriteUser(user: FavoriteUser) {
        return favoriteUserDao.deleteUser(user)
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        return settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    fun getThemeSetting(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }
}