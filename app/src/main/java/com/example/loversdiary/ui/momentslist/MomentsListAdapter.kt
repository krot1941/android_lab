package com.example.loversdiary.ui.momentslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.loversdiary.data.Event
import com.example.loversdiary.data.Moment
import com.example.loversdiary.databinding.MomentItemFragmentBinding

class MomentsListAdapter (private val listener: OnItemClickListener)
    : ListAdapter<Moment, MomentsListAdapter.MomentsListViewHolder>(DiffCallback()) {

    private var momentsList: List<Moment> = ArrayList()
    private var eventsList: List<Event> = ArrayList()

    fun setFilms(moments: List<Moment>) {
        this.momentsList = moments
        submitList(this.momentsList)
    }

    fun setEvents(events: List<Event>) {
        this.eventsList = events
    }

    interface OnItemClickListener{
        fun onItemClick(moment: Moment, event: Event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentsListViewHolder {
        val binding = MomentItemFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MomentsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MomentsListViewHolder, position: Int) {
        holder.bind(momentsList[position], eventsList[position])
    }

    override fun getItemCount() = momentsList.size

    inner class MomentsListViewHolder(private val binding: MomentItemFragmentBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(momentsList[position], eventsList[position])
                    }
                }
            }
        }
        fun bind(moment: Moment, event: Event) = with(itemView) {
            binding.apply {
                momentItemDateView.text = moment.dateFormatted
                momentItemPlaceView.text = moment.place
                momentItemEventView.text = event.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Moment>() {
        override fun areItemsTheSame(oldItem: Moment, newItem: Moment) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Moment, newItem: Moment) =
                oldItem == newItem
    }

}