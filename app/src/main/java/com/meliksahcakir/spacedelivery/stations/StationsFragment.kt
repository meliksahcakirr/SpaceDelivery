package com.meliksahcakir.spacedelivery.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.utils.EventObserver
import kotlinx.android.synthetic.main.stations_fragment.*

class StationsFragment : Fragment() {

    companion object {
        fun newInstance() = StationsFragment()
    }

    private val viewModel by viewModels<StationsViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

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
        }

        mainViewModel.gameOver.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), "Game Over!", Toast.LENGTH_SHORT).show()
        })
    }
}