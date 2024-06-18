package com.andryan.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andryan.githubuser.data.remote.response.UserFollowers
import com.andryan.githubuser.databinding.ContainerUserBinding
import com.squareup.picasso.Picasso

class UserFollowersAdapter(private val userFollowers: List<UserFollowers>) :
    RecyclerView.Adapter<UserFollowersAdapter.UserFollowersHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowersHolder {
        val binding =
            ContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserFollowersHolder(binding)
    }

    override fun getItemCount(): Int {
        return userFollowers.size
    }

    override fun onBindViewHolder(holder: UserFollowersHolder, position: Int) {
        holder.bind(userFollowers[position])
    }

    inner class UserFollowersHolder(private val binding: ContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFollowers) {
            binding.userFullname.text = user.login
            binding.userUrl.text = user.url
            Picasso.get().load(user.avatarUrl).noFade().into(binding.userProfileImage)
        }
    }
}