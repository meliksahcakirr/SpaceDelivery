package com.meliksahcakir.spacedelivery.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.SpaceDeliveryApplication
import com.meliksahcakir.spacedelivery.main.MainActivity
import com.meliksahcakir.spacedelivery.utils.EventObserver
import com.meliksahcakir.spacedelivery.utils.ViewModelFactory

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory((application as SpaceDeliveryApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.navigateToNextScreen.observe(this, EventObserver {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}