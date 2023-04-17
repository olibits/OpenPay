package com.openpay.android.ui.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openpay.android.databinding.ItemLocationBinding
import com.openpay.android.model.location.LocationEntity

class LocationAdapter(
    private val onItemClicked: (LocationEntity) -> Unit
) : ListAdapter<LocationEntity, LocationAdapter.LocationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LocationViewHolder(
        ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    inner class LocationViewHolder(
        private val binding: ItemLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: LocationEntity, onItemClicked: (LocationEntity) -> Unit) {
            binding.locationTextView.text = location.toString()
            binding.root.setOnClickListener {
                onItemClicked(location)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocationEntity>() {
            override fun areItemsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(
                oldItem: LocationEntity,
                newItem: LocationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
