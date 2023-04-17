package com.openpay.android.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.openpay.android.databinding.ItemReviewBinding
import com.openpay.android.model.people.KnownFor

class ReviewListAdapter() : ListAdapter<KnownFor, ReviewViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) =
        holder.bind(getItem(position))

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<KnownFor>() {
            override fun areItemsTheSame(oldItem: KnownFor, newItem: KnownFor): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: KnownFor, newItem: KnownFor): Boolean =
                oldItem == newItem
        }
    }
}