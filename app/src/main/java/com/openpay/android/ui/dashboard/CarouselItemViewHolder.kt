package com.openpay.android.ui.dashboard

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openpay.android.databinding.ItemCarouselBinding
import com.openpay.android.model.movie.Results

class CarouselItemViewHolder(binding: ItemCarouselBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var binding: ItemCarouselBinding? = null

    init {
        this.binding = binding
    }

    fun setItem(model: Results) {
        binding?.let { view ->
            //view.title = model.title
            Glide.with(view.root.context)
                .load("https://image.tmdb.org/t/p/w500/" + model.poster_path).into(view.imgMovie)
        }
    }
}
