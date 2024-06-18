package com.andryan.githubuser.ui.favorite

import androidx.lifecycle.ViewModel
import com.andryan.githubuser.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteUserViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    fun getAllFavoriteUser() = userRepository.getAllFavoriteUser()
}