package com.andryan.githubuser.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.andryan.githubuser.R
import com.andryan.githubuser.data.local.entity.FavoriteUser
import com.andryan.githubuser.databinding.ContainerUserBinding
import com.andryan.githubuser.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

class UserFavoriteAdapter(private val userFavorites: List<FavoriteUser>) :
    RecyclerView.Adapter<UserFavoriteAdapter.FavoriteUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteUserViewHolder {
        val binding =
            ContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(holder.itemView.context, userFavorites[position])
    }

    override fun getItemCount(): Int {
        return userFavorites.size
    }

    inner class FavoriteUserViewHolder(private val binding: ContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, favoriteUser: FavoriteUser) {
            binding.apply {
                Picasso.get().load(favoriteUser.avatarUrl).noFade().into(userProfileImage)
                userFullname.text = favoriteUser.username
                userUrl.text = favoriteUser.url

                root.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(userProfileImage, "userProfile"),
                            Pair(userFullname, "username"),
                            Pair(userUrl, "url")
                        )

                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(
                        ContextCompat.getString(root.context, R.string.extra_username),
                        favoriteUser.username
                    )

                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}