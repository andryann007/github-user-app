package com.andryan.githubuser.data.remote.retrofit

import com.andryan.githubuser.data.remote.response.UserDetail
import com.andryan.githubuser.data.remote.response.UserFollowers
import com.andryan.githubuser.data.remote.response.UserFollowing
import com.andryan.githubuser.data.remote.response.UserList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): UserList

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): UserDetail

    @GET("users/{username}/followers")
    suspend fun getUserFollowers(@Path("username") username: String): List<UserFollowers>

    @GET("users/{username}/following")
    suspend fun getUserFollowing(@Path("username") username: String): List<UserFollowing>
}