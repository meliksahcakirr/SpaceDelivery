package com.meliksahcakir.spacedelivery.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.utils.GameInterface
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory
import kotlinx.android.synthetic.main.favorite_stations_fragment.*

class FavoriteStationsFragment : Fragment(), GameInterface, FavoriteStationListAdapterListener {

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory((requireActivity().application as SpaceDeliveryApplication).repository)
    }

    private lateinit var mAdapter: FavoriteStationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_stations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = FavoriteStationListAdapter(this)
        favoriteStationRecyclerView.adapter = mAdapter
        mainViewModel.favoriteStations.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                emptyGroup.isVisible = true
                favoriteStationRecyclerView.isVisible = false
            } else {
                emptyGroup.isVisible = false
                favoriteStationRecyclerView.isVisible = true
            }
            mAdapter.submitList(it)
        }
    }

    override fun onFavoriteChanged(station: Station, favorite: Boolean) {
        mainViewModel.onFavoriteClicked(station, favorite)
    }
}