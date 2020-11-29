package com.meliksahcakir.spacedelivery.stations

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.data.Station
import kotlinx.android.synthetic.main.favorite_planet_item.view.*

class FavoriteStationListAdapter(private val listener: FavoriteStationListAdapterListener) :
    ListAdapter<Station, FavoriteStationViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Station>() {
        override fun areItemsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Station, newItem: Station): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStationViewHolder {
        return FavoriteStationViewHolder(parent, listener)
    }

    override fun onBindViewHolder(holder: FavoriteStationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FavoriteStationViewHolder(
    private val parent: ViewGroup,
    private val listener: FavoriteStationListAdapterListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.favorite_planet_item, parent, false)
) {
    private val stockTextView: TextView by lazy { itemView.stockTextView }
    private val eusTextView: TextView by lazy { itemView.eusTextView }
    private val planetTextView: TextView by lazy { itemView.planetTextView }
    private val favoriteImageView: ImageView by lazy { itemView.favoriteImageView }

    fun bind(station: Station) {
        val stockText = "${station.currentStock} / ${station.capacity}"
        stockTextView.text = stockText
        val eusText = "${station.calculateEus(Station.EARTH)} EUS"
        eusTextView.text = eusText
        planetTextView.text = station.name
        favoriteImageView.setOnClickListener {
            val favorite = !station.favorite
            listener.onFavoriteChanged(station, favorite)
        }
    }
}

interface FavoriteStationListAdapterListener {
    fun onFavoriteChanged(station: Station, favorite: Boolean)
}