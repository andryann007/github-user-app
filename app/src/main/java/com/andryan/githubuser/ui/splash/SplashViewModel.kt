package com.andryan.githubuser.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andryan.githubuser.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting().asLiveData()
}