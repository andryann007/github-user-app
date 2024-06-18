package com.andryan.githubuser.ui.main

import androidx.lifecycle.ViewModel
import com.andryan.githubuser.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    fun searchGithubUser(query: String) = userRepository.searchUsers(query)
}