package com.meliksahcakir.spacedelivery.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory

class FavoriteStationsFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteStationsFragment()
    }

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory((requireActivity().application as SpaceDeliveryApplication).repository)
    }

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
    }
}