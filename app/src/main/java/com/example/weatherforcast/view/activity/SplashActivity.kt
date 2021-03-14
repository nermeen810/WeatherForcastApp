package com.example.weatherforcast.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherforcast.R
import com.example.weatherforcast.databinding.ActivitySplashBinding
import com.example.weatherforcast.databinding.ActivityWeatherDetailsBinding

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000L
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.part1Txt.setAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.left_to_right_anim
            )
        )
        binding.part2Txt.setAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.down_to_up_anim
            )
        )
        binding.part3Txt.setAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.right_to_left_anim
            )
        )


        Handler().postDelayed(
            {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()
            }, SPLASH_TIME_OUT
        )
    }
}
