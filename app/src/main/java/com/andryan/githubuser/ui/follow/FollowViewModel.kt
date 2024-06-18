package com.andryan.githubuser.ui.follow

import androidx.lifecycle.ViewModel
import com.andryan.githubuser.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    fun getUserFollowers(username: String) = userRepository.getUserFollowers(username)

    fun getUserFollowing(username: String) = userRepository.getUserFollowing(username)
}