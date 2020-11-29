package com.meliksahcakir.spacedelivery.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.data.Statistics
import kotlinx.android.synthetic.main.statistics_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class StatisticsListAdapter : ListAdapter<Statistics, StatisticsViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Statistics>() {
        override fun areItemsTheSame(oldItem: Statistics, newItem: Statistics): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Statistics, newItem: Statistics): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        return StatisticsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StatisticsViewHolder(private val parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.statistics_item, parent, false)
) {
    private val shuttleNameTextView: TextView by lazy { itemView.shuttleNameTextView }
    private val dateTextView: TextView by lazy { itemView.dateTextView }
    private val eusTextView: TextView by lazy { itemView.eusTextView }
    private val ugsTextView: TextView by lazy { itemView.ugsTextView }
    private val stationTextView: TextView by lazy { itemView.stationTextView }

    fun bind(statistics: Statistics) {
        shuttleNameTextView.text = statistics.name
        val date = Date(statistics.timestamp)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateTextView.text = sdf.format(date)
        ugsTextView.text = statistics.deliveredUgs.toString()
        eusTextView.text = statistics.spentEus.toString()
        stationTextView.text = statistics.numberOfDestination.toString()
    }
}