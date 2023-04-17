package com.openpay.android.ui.home

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openpay.android.databinding.ItemReviewBinding
import com.openpay.android.model.people.KnownFor

class ReviewViewHolder(private val binding: ItemReviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(knownFor: KnownFor) {
        binding.title.text = knownFor.title
        binding.review.text = knownFor.overview
        Glide.with(binding.root.context)
            .load("https://image.tmdb.org/t/p/w500/" + knownFor.poster_path).into(binding.imageView)
    }
}
