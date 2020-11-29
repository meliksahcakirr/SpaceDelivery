package com.meliksahcakir.spacedelivery.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.snackbar.Snackbar
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.utils.EventObserver
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory
import kotlinx.android.synthetic.main.game_over_dialog.view.*
import kotlinx.android.synthetic.main.stations_fragment.*

class StationsFragment : Fragment(), StationListAdapterListener {

    private val mainViewModel by activityViewModels<MainViewModel> {
        ViewModelFactory((requireActivity().application as SpaceDeliveryApplication).repository)
    }

    private lateinit var mAdapter: StationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stations_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = StationListAdapter(this)
        stationRecyclerView.apply {
            adapter = mAdapter
            LinearSnapHelper().attachToRecyclerView(this)
        }

        mainViewModel.stations.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                emptyGroup.isVisible = true
                stationRecyclerView.isVisible = false
                currentStationTextView.isVisible = false
            } else {
                emptyGroup.isVisible = false
                stationRecyclerView.isVisible = true
                currentStationTextView.isVisible = true
            }
            mAdapter.submitList(it)
        }

        mainViewModel.foundStation.observe(viewLifecycleOwner, EventObserver {
            val position = mAdapter.currentList.indexOf(it)
            if (position != -1) {
                stationRecyclerView.smoothScrollToPosition(position)
            }
        })

        mainViewModel.shuttleName.observe(viewLifecycleOwner) {
            shuttleNameTextView.text = it
        }

        mainViewModel.ugs.observe(viewLifecycleOwner) {
            ugsTextView.text = it.toString()
        }

        mainViewModel.eus.observe(viewLifecycleOwner) {
            eusTextView.text = it.toString()
        }

        mainViewModel.ds.observe(viewLifecycleOwner) {
            dsTextView.text = it.toString()
        }

        mainViewModel.health.observe(viewLifecycleOwner) {
            healthTextView.text = it.toString()
        }

        mainViewModel.timer.observe(viewLifecycleOwner) {
            timerTextView.text = "$it s"
        }

        mainViewModel.currentStation.observe(viewLifecycleOwner) {
            currentStationTextView.text = it.name
            mAdapter.currentStation = it
        }

        mainViewModel.gameOver.observe(viewLifecycleOwner) {
            if(it != null) {
                val mView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.game_over_dialog, null)
                mView.gameOverTextView.text =
                    requireContext().getString(R.string.game_over_shuttle_name, it.name)
                mView.gameOverReasonTextView.text = requireContext().getString(it.gameOverReason)
                mView.deliveredUgsTextView.text = it.deliveredUgs.toString()
                mView.totalEusTextView.text = it.spentEus.toString()
                mView.stationTextView.text = it.numberOfDestination.toString()
                val builder = AlertDialog.Builder(requireContext()).setView(mView).setCancelable(false)
                val dialog = builder.show()
                mView.statisticsButton.setOnClickListener {
                    //TODO navigate to statistics
                    mainViewModel.gameOverHandled()
                    dialog.dismiss()
                }
                mView.startOverButton.setOnClickListener {
                    //TODO navigate to shuttleFragment
                    mainViewModel.gameOverHandled()
                    dialog.dismiss()
                    findNavController().navigateUp()
                }
            }
        }

        mainViewModel.snackBarMessage.observe(viewLifecycleOwner, EventObserver {
            val snackBar = Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
            snackBar.show()
        })

        searchEditText.doAfterTextChanged {
            val text = it.toString()
            if (text != "") {
                mainViewModel.onSearch(text)
            }
        }
    }

    override fun onTravelClicked(station: Station) {
        mainViewModel.onTravelClicked(station)
    }

    override fun onFavoriteChanged(station: Station, favorite: Boolean) {
        mainViewModel.onFavoriteClicked(station, favorite)
    }
}