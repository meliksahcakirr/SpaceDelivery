package com.meliksahcakir.spacedelivery.stations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.hideKeyboard
import kotlinx.android.synthetic.main.planet_item.view.*

class StationListAdapter(private val listener: StationListAdapterListener) :
    ListAdapter<Station, StationViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }
    }

    var currentStation = Station.EARTH
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        return StationViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(getItem(position), currentStation)
    }
}

class StationViewHolder(
    private val parent: ViewGroup,
    private val listener: StationListAdapterListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.planet_item, parent, false)
) {
    private val stockTextView: TextView by lazy { itemView.stockTextView }
    private val distanceTextView: TextView by lazy { itemView.distanceTextView }
    private val planetTextView: TextView by lazy { itemView.planetTextView }
    private val favoriteImageView: ImageView by lazy { itemView.favoriteImageView }
    private val travelButton: MaterialButton by lazy { itemView.travelButton }

    fun bind(station: Station, currentStation: Station) {
        val stockText = "${station.currentStock} / ${station.capacity}"
        stockTextView.text = stockText
        val eusText = "${station.calculateEus(currentStation)} EUS"
        distanceTextView.text = eusText
        if (station.favorite) {
            favoriteImageView.setImageResource(R.drawable.ic_star_on)
        } else {
            favoriteImageView.setImageResource(R.drawable.ic_star_off)
        }
        planetTextView.text = station.name
        if (station.completed) {
            travelButton.isEnabled = false
            travelButton.text = parent.context.getString(R.string.completed)
        } else {
            travelButton.isEnabled = true
            travelButton.text = parent.context.getString(R.string.travel)
        }
        favoriteImageView.setOnClickListener {
            val favorite = !station.favorite
            listener.onFavoriteChanged(station, favorite)
        }
        travelButton.setOnClickListener {
            it.hideKeyboard()
            listener.onTravelClicked(station)
        }
    }
}

interface StationListAdapterListener {
    fun onTravelClicked(station: Station)
    fun onFavoriteChanged(station: Station, favorite: Boolean)
}