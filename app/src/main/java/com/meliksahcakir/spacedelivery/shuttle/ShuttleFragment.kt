package com.meliksahcakir.spacedelivery.shuttle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.utils.EventObserver
import kotlinx.android.synthetic.main.shuttle_fragment.*

class ShuttleFragment : Fragment() {

    private val shuttleViewModel by activityViewModels<ShuttleViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shuttle_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        durabilitySlider.addOnChangeListener(durabilityListener)
        velocitySlider.addOnChangeListener(velocityListener)
        capacitySlider.addOnChangeListener(capacityListener)
        shuttleViewModel.durability.observe(viewLifecycleOwner) {
            durabilityTextView.text = it.toString()
            if (it != durabilitySlider.value.toInt()) {
                durabilitySlider.value = it.toFloat()
            }
        }
        shuttleViewModel.velocity.observe(viewLifecycleOwner) {
            velocityTextView.text = it.toString()
            if (it != velocitySlider.value.toInt()) {
                velocitySlider.value = it.toFloat()
            }
        }
        shuttleViewModel.capacity.observe(viewLifecycleOwner) {
            capacityTextView.text = it.toString()
            if (it != capacitySlider.value.toInt()) {
                capacitySlider.value = it.toFloat()
            }
        }
        shuttleViewModel.availablePoints.observe(viewLifecycleOwner) {
            availablePointsTextView.text = it.toString()
        }
        shuttleViewModel.navigateToMainScreenEvent.observe(viewLifecycleOwner, EventObserver {
            mainViewModel.onShuttleSelected(it)
            Toast.makeText(requireContext(), "Have Fun ${it.name}", Toast.LENGTH_SHORT).show()
        })
        shuttleViewModel.snackBarMessage.observe(viewLifecycleOwner, EventObserver {
            val snackBar = Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
            snackBar.show()
        })
        shuttleNameEditText.doAfterTextChanged {
            shuttleViewModel.onNameChanged(it.toString())
        }
        startButton.setOnClickListener {
            shuttleViewModel.onStartButtonClicked()
        }
    }

    private val durabilityListener = Slider.OnChangeListener { _, value, _ ->
        val min = 1
        val max = shuttleViewModel.getMaxDurabilityAllowed()
        val durability = when {
            value < min -> min
            value > max -> max
            else -> value.toInt()
        }
        shuttleViewModel.onDurabilityChanged(durability)
    }

    private val velocityListener = Slider.OnChangeListener { _, value, _ ->
        val min = 1
        val max = shuttleViewModel.getMaxVelocityAllowed()
        val velocity = when {
            value < min -> min
            value > max -> max
            else -> value.toInt()
        }
        shuttleViewModel.onVelocityChanged(velocity)
    }

    private val capacityListener = Slider.OnChangeListener { _, value, _ ->
        val min = 1
        val max = shuttleViewModel.getMaxCapacityAllowed()
        val capacity = when {
            value < min -> min
            value > max -> max
            else -> value.toInt()
        }
        shuttleViewModel.onCapacityChanged(capacity)
    }
}