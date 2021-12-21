package com.antsfamily.biketrainer.ui.profiles.adapter

import androidx.recyclerview.widget.DiffUtil
import com.antsfamily.data.model.profile.Profile

class ProfilesDiffUtil: DiffUtil.ItemCallback<Profile>() {
    override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
        return oldItem == newItem
    }
}
