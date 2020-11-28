package com.meliksahcakir.spacedelivery.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.shuttle.ShuttleFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        host = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(host.navController)
        host.navController.addOnDestinationChangedListener(this)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.shuttleFragment -> bottomNavigationView.isVisible = false
            else -> bottomNavigationView.isVisible = true
        }
    }

    override fun onBackPressed() {
        val fragment = host.childFragmentManager.fragments[0]
        if (fragment != null && fragment !is ShuttleFragment) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.do_you_want_to_start_over))
            builder.setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.cancel()
                host.navController.navigateUp()
            }
            builder.setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        } else {
            super.onBackPressed()
        }
    }
}