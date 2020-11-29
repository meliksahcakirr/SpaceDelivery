package com.meliksahcakir.spacedelivery.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.utils.GameInterface
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.game_over_dialog.view.*

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory((application as SpaceDeliveryApplication).repository)
    }
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        host = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(host.navController)
        host.navController.addOnDestinationChangedListener(this)

        viewModel.gameOver.observe(this) {
            if (it != null) {
                val mView =
                    LayoutInflater.from(this).inflate(R.layout.game_over_dialog, null)
                mView.gameOverTextView.text = getString(R.string.game_over_shuttle_name, it.name)
                mView.gameOverReasonTextView.text = getString(it.gameOverReason)
                mView.deliveredUgsTextView.text = it.deliveredUgs.toString()
                mView.totalEusTextView.text = it.spentEus.toString()
                mView.stationTextView.text = it.numberOfDestination.toString()
                val builder = AlertDialog.Builder(this).setView(mView).setCancelable(false)
                val dialog = builder.show()
                mView.statisticsButton.setOnClickListener {
                    host.navController.navigate(R.id.statisticsFragment)
                    viewModel.gameOverHandled()
                    dialog.dismiss()
                }
                mView.startOverButton.setOnClickListener {
                    viewModel.gameOverHandled()
                    dialog.dismiss()
                    host.navController.navigateUp()
                }
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when (destination.id) {
            R.id.shuttleFragment, R.id.statisticsFragment -> bottomNavigationView.isVisible = false
            else -> bottomNavigationView.isVisible = true
        }
    }

    override fun onBackPressed() {
        val fragment = host.childFragmentManager.fragments[0]
        if (fragment != null && fragment is GameInterface) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.do_you_want_to_start_over))
            builder.setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.cancel()
                viewModel.resetTimer()
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