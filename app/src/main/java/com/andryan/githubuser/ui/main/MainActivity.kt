package com.andryan.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andryan.githubuser.R
import com.andryan.githubuser.adapter.UserListAdapter
import com.andryan.githubuser.data.remote.response.UserInfo
import com.andryan.githubuser.utils.Result
import com.andryan.githubuser.databinding.ActivityMainBinding
import com.andryan.githubuser.ui.favorite.FavoriteUserActivity
import com.andryan.githubuser.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private var username: String? = ""
    private lateinit var searchAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar(binding.toolbarMain)
    }

    private fun showSearchResult(username: String) {
        mainViewModel.searchGithubUser(username).observe(this@MainActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                    showError(false)

                    showUserData(false)
                    showNoData(false)
                }

                is Result.Success -> {
                    showLoading(false)
                    showError(false)

                    val users = result.data.items

                    if (users.isEmpty()) {
                        showNoData(true)
                    } else {
                        showNoData(false)
                        showUserData(true)
                        setSearchAdapter(users)
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    showError(true)

                    Toast.makeText(
                        this@MainActivity,
                        "Terjadi Kesalahan: " + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setSearchAdapter(listUser: List<UserInfo>) {
        searchAdapter = UserListAdapter(listUser)
        val searchLayoutManager = LinearLayoutManager(this@MainActivity)
        val searchItemDecoration =
            DividerItemDecoration(this@MainActivity, searchLayoutManager.orientation)

        binding.rvUserList.apply {
            layoutManager = searchLayoutManager
            adapter = searchAdapter
            addItemDecoration(searchItemDecoration)
            setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingUser.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserData(showData: Boolean) {
        binding.rvUserList.visibility = if (showData) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        binding.lottieError.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showNoData(isEmpty: Boolean) {
        binding.lottieSearch.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun setActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.apply {
            queryHint = "Search User..."
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    username = query.toString()
                    clearFocus()
                    showSearchResult(username ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }

            R.id.action_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}