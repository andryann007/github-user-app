package com.andryan.githubuser.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andryan.githubuser.utils.Result
import com.andryan.githubuser.R
import com.andryan.githubuser.adapter.UserFollowersAdapter
import com.andryan.githubuser.adapter.UserFollowingAdapter
import com.andryan.githubuser.data.remote.response.UserFollowers
import com.andryan.githubuser.data.remote.response.UserFollowing
import com.andryan.githubuser.databinding.FragmentFollowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding

    private val followViewModel: FollowViewModel by viewModels()

    private lateinit var userFollowersAdapter: UserFollowersAdapter
    private lateinit var userFollowingAdapter: UserFollowingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_NAME).toString()

        if (position == 1) {
            showFollowersResult(username)
        } else {
            showFollowingResult(username)
        }
    }

    private fun showFollowersResult(username: String) {
        followViewModel.getUserFollowers(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        showFollowData(false)
                        showTextNoFollow(false)
                    }

                    is Result.Success -> {
                        showLoading(false)

                        val followers = result.data
                        if (followers.isEmpty()) {
                            binding?.tvNoFollow?.text =
                                resources.getString(R.string.tv_no_followers)
                            showTextNoFollow(true)
                        } else {
                            showFollowData(true)
                            setFollowersAdapter(followers)
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showFollowData(false)
                        showTextNoFollow(false)

                        Toast.makeText(
                            context,
                            "Terjadi Kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setFollowersAdapter(followers: List<UserFollowers>) {
        userFollowersAdapter = UserFollowersAdapter(followers)
        val followersLayoutManager = LinearLayoutManager(requireActivity())
        val followersItemDecoration =
            DividerItemDecoration(requireActivity(), followersLayoutManager.orientation)

        binding?.rvUserFollow?.apply {
            layoutManager = followersLayoutManager
            adapter = userFollowersAdapter
            addItemDecoration(followersItemDecoration)
            setHasFixedSize(true)
        }
    }

    private fun showFollowingResult(username: String) {
        followViewModel.getUserFollowing(username).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                        showFollowData(false)
                        showTextNoFollow(false)
                    }

                    is Result.Success -> {
                        showLoading(false)

                        val following = result.data
                        if (following.isEmpty()) {
                            binding?.tvNoFollow?.text =
                                resources.getString(R.string.tv_no_following)
                            showTextNoFollow(true)
                        } else {
                            showFollowData(true)
                            setFollowingAdapter(following)
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showFollowData(false)
                        showTextNoFollow(false)

                        Toast.makeText(
                            context,
                            "Terjadi Kesalahan: " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setFollowingAdapter(following: List<UserFollowing>) {
        userFollowingAdapter = UserFollowingAdapter(following)
        val followingLayoutManager = LinearLayoutManager(requireActivity())
        val followingItemDecoration =
            DividerItemDecoration(requireActivity(), followingLayoutManager.orientation)

        binding?.rvUserFollow?.apply {
            layoutManager = followingLayoutManager
            adapter = userFollowingAdapter
            addItemDecoration(followingItemDecoration)
            setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.loadingFollow?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFollowData(showData: Boolean) {
        binding?.rvUserFollow?.visibility = if (showData) View.VISIBLE else View.GONE
    }

    private fun showTextNoFollow(isNoFollowers: Boolean) {
        binding?.tvNoFollow?.visibility = if (isNoFollowers) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_NAME = "username"
    }
}