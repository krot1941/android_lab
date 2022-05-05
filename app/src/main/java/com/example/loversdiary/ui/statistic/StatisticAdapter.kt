package com.example.loversdiary.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.loversdiary.data.EventStatisticQuery
import com.example.loversdiary.databinding.StatisticItemFragmentBinding

class StatisticAdapter
    : ListAdapter<EventStatisticQuery, StatisticAdapter.EventsViewHolder>(DiffCallback()) {

    private var eventsList: List<EventStatisticQuery> = ArrayList()

    fun setStatistic(events: List<EventStatisticQuery>) {
        eventsList = events
        submitList(eventsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticAdapter.EventsViewHolder {
        val binding = StatisticItemFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticAdapter.EventsViewHolder, position: Int) {
        holder.bind(eventsList[position])
    }

    inner class EventsViewHolder(private val binding: StatisticItemFragmentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(eventStatisticQuery: EventStatisticQuery) = with(itemView) {
            binding.apply {
                statisticItemNameView.text = eventStatisticQuery.name
                statisticItemCountView.text = eventStatisticQuery.count.toString()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<EventStatisticQuery>() {

        override fun areItemsTheSame(oldItem: EventStatisticQuery, newItem: EventStatisticQuery) =
                oldItem == newItem

        override fun areContentsTheSame(oldItem: EventStatisticQuery, newItem: EventStatisticQuery) =
                oldItem == newItem
    }
}