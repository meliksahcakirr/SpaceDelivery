package com.meliksahcakir.spacedelivery.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.main.MainActivity
import com.meliksahcakir.spacedelivery.utils.EventObserver

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.navigateToNextScreen.observe(this, EventObserver {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}