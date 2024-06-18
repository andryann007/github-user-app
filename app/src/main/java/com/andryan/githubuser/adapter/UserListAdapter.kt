package com.andryan.githubuser.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.andryan.githubuser.R
import com.andryan.githubuser.data.remote.response.UserInfo
import com.andryan.githubuser.databinding.ContainerUserBinding
import com.andryan.githubuser.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

class UserListAdapter(private val userLists: List<UserInfo>) :
    RecyclerView.Adapter<UserListAdapter.UserInfoHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserInfoHolder {
        val binding =
            ContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserInfoHolder(binding)
    }

    override fun getItemCount(): Int {
        return userLists.size
    }

    override fun onBindViewHolder(holder: UserInfoHolder, position: Int) {
        holder.bind(holder.itemView.context, userLists[position])
    }

    inner class UserInfoHolder(private val binding: ContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, user: UserInfo) {
            binding.apply {
                Picasso.get().load(user.avatarUrl).noFade().into(binding.userProfileImage)
                binding.userFullname.text = user.login
                binding.userUrl.text = user.url

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(userProfileImage, "userProfile"),
                            Pair(userFullname, "username"),
                            Pair(userUrl, "url")
                        )

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(getString(root.context, R.string.extra_username), user.login)

                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}