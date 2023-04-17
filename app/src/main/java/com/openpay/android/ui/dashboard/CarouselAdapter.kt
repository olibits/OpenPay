package com.openpay.android.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openpay.android.databinding.ItemCarouselBinding
import com.openpay.android.model.movie.Results

class CarouselAdapter(private val carouselDataList: List<Results>) :
    RecyclerView.Adapter<CarouselItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCarouselBinding =
            ItemCarouselBinding.inflate(layoutInflater, parent, false)

        return CarouselItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {

        holder.setItem(carouselDataList[position])

    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }
}