package com.example.loversdiary.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.loversdiary.data.Photo
import com.example.loversdiary.databinding.HomePhotoItemFragmentBinding
import com.squareup.picasso.Picasso

class HomePhotoAdapter
    : ListAdapter<Photo, HomePhotoAdapter.HomePhotoViewHolder>(DiffCallback()) {

    private var photosList: List<Photo> = ArrayList()

    fun setItems(photos: List<Photo>) {
        this.photosList = photos
        submitList(this.photosList)
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): HomePhotoViewHolder {
        val binding = HomePhotoItemFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomePhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomePhotoAdapter.HomePhotoViewHolder, position: Int) {
        holder.bind(photosList[position])
    }

    override fun getItemCount() = photosList.size

    inner class HomePhotoViewHolder(private val binding: HomePhotoItemFragmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) = with(itemView) {
            binding.apply {
                Picasso.get()
                    .load(photo.uri)
                    .into(photoItemFrgImageView)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
            oldItem == newItem
    }
}