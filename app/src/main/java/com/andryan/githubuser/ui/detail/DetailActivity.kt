package com.andryan.githubuser.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.andryan.githubuser.R
import com.andryan.githubuser.data.local.entity.FavoriteUser
import com.andryan.githubuser.data.remote.response.UserDetail
import com.andryan.githubuser.databinding.ActivityDetailBinding
import com.andryan.githubuser.utils.Result
import com.andryan.githubuser.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite: Boolean? = false
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(resources.getString(R.string.extra_username)).toString()

        setActionBar(binding.toolbarDetail, username)
        setViewPager(this@DetailActivity)

        showDetailResult(username)
        setFabFavoriteIcon(username)
    }

    private fun setViewPager(activity: AppCompatActivity) {
        val sectionsPagerAdapter = SectionPagerAdapter(activity)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        sectionsPagerAdapter.username = username
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun showDetailResult(username: String) {
        detailViewModel.getUserDetail(username).observe(this@DetailActivity) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        showDetailData(false)
                    }

                    is Result.Success -> {
                        showLoading(false)
                        showDetailData(true)

                        val details = result.data
                        setUserDetail(details)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showDetailData(false)

                        Toast.makeText(
                            this,
                            "Terjadi Kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setFabFavoriteIcon(username: String) {
        detailViewModel.getFavoriteUserByUsername(username).observe(this) { result ->
            if (result != null) {
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_fill)
            } else {
                isFavorite = false
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_outline)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserDetail(userDetail: UserDetail) {
        Picasso.get().load(userDetail.avatarUrl).noFade().into(binding.imgUserProfile)
        binding.tvUserFullname.text = "Full Name : ${userDetail.name}"
        binding.tvUserUsername.text = "Username : ${userDetail.login}"
        binding.tvUserUrl.text = "URL : ${userDetail.url}"
        binding.tvUserFollowers.text = "${userDetail.followers} Followers"
        binding.tvUserFollowing.text = "${userDetail.following} Following"

        val favoriteUser = FavoriteUser(
            userDetail.id,
            userDetail.login,
            userDetail.url,
            userDetail.avatarUrl
        )

        binding.fabFavorite.setOnClickListener {
            if (isFavorite == false) {
                detailViewModel.saveUser(favoriteUser)
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_fill)
                Toast.makeText(
                    this,
                    "Anda Sukses Menambah User Favorit !!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                detailViewModel.deleteUser(favoriteUser)
                isFavorite = false
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_outline)
                Toast.makeText(
                    this,
                    "Anda Sukses Menghapus User Favorit !!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadUserDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDetailData(showData: Boolean) {
        binding.cvUserDetail.visibility = if (showData) View.VISIBLE else View.GONE
        binding.tabLayout.visibility = if (showData) View.VISIBLE else View.GONE
        binding.viewPager.visibility = if (showData) View.VISIBLE else View.GONE
        binding.fabFavorite.visibility = if (showData) View.VISIBLE else View.GONE
    }

    private fun setActionBar(toolbar: Toolbar, username: String) {
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            subtitle = "Username : ${
                username.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                }
            }"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/${username}")

                val chooser = Intent.createChooser(intent, "Share this github user with :")
                startActivity(chooser)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}