package com.andryan.githubuser.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andryan.githubuser.data.UserRepository
import com.andryan.githubuser.data.local.entity.FavoriteUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    fun getUserDetail(username: String) = userRepository.getUserDetail(username)

    fun getFavoriteUserByUsername(username: String) =
        userRepository.getFavoriteUserByUsername(username)

    fun saveUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            userRepository.addFavoriteUser(favoriteUser)
        }
    }

    fun deleteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            userRepository.deleteFavoriteUser(favoriteUser)
        }
    }
}