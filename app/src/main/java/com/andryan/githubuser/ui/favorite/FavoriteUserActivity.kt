package com.andryan.githubuser.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andryan.githubuser.adapter.UserFavoriteAdapter
import com.andryan.githubuser.data.local.entity.FavoriteUser
import com.andryan.githubuser.databinding.ActivityFavoriteUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding

    private val favoriteUserViewModel: FavoriteUserViewModel by viewModels()
    private lateinit var favUserAdapter: UserFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar(binding.toolbarFavorite)

        showLoading(true)
        showUserData(false)
        getAllFavoriteUser()
    }

    private fun setActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getAllFavoriteUser() {
        favoriteUserViewModel.getAllFavoriteUser().observe(this) {
            showLoading(false)
            showUserData(true)
            setFavoriteAdapter(it)
        }
    }

    private fun setFavoriteAdapter(favoriteUser: List<FavoriteUser>) {
        favUserAdapter = UserFavoriteAdapter(favoriteUser)
        val favUserLayoutManager = LinearLayoutManager(this@FavoriteUserActivity)
        val favUserItemDecoration =
            DividerItemDecoration(this@FavoriteUserActivity, favUserLayoutManager.orientation)

        binding.rvUserFavorite.apply {
            layoutManager = favUserLayoutManager
            adapter = favUserAdapter
            addItemDecoration(favUserItemDecoration)
            setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingFavorite.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserData(showData: Boolean) {
        binding.rvUserFavorite.visibility = if (showData) View.VISIBLE else View.GONE
    }
}