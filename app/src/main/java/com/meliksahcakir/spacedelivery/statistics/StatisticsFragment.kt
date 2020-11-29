package com.meliksahcakir.spacedelivery.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory
import kotlinx.android.synthetic.main.statistics_fragment.*


class StatisticsFragment : Fragment() {

    private val viewModel by viewModels<StatisticsViewModel> {
        ViewModelFactory((requireActivity().application as SpaceDeliveryApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    private lateinit var mAdapter: StatisticsListAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(StatisticsFragmentDirections.actionStatisticsFragmentToShuttleFragment())
            }
        }
        mAdapter = StatisticsListAdapter()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        statisticsRecyclerView.adapter = mAdapter
        viewModel.statisticsList.observe(viewLifecycleOwner) {
            progressBar.isVisible = false
            if (it.isEmpty()) {
                emptyGroup.isVisible = true
                statisticsRecyclerView.isVisible = false
            } else {
                emptyGroup.isVisible = false
                statisticsRecyclerView.isVisible = true
            }
            mAdapter.submitList(it)
        }
    }

}