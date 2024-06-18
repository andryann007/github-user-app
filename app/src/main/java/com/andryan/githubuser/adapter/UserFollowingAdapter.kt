package com.andryan.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andryan.githubuser.data.remote.response.UserFollowing
import com.andryan.githubuser.databinding.ContainerUserBinding
import com.squareup.picasso.Picasso

class UserFollowingAdapter(private val userFollowing: List<UserFollowing>) :
    RecyclerView.Adapter<UserFollowingAdapter.UserFollowingHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowingHolder {
        val binding =
            ContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserFollowingHolder(binding)
    }

    override fun getItemCount(): Int {
        return userFollowing.size
    }

    override fun onBindViewHolder(holder: UserFollowingHolder, position: Int) {
        holder.bind(userFollowing[position])
    }

    class UserFollowingHolder(private val binding: ContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserFollowing) {
            binding.userFullname.text = user.login
            binding.userUrl.text = user.url
            Picasso.get().load(user.avatarUrl).noFade().into(binding.userProfileImage)
        }
    }
}