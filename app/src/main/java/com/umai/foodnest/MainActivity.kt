package com.umai.foodnest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.umai.foodnest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setupWithNavController(navController)

        val hideOnScreens = setOf(
            R.id.loginFragment,
            R.id.registerFragment,
            R.id.checkoutFragment,
            R.id.trackingFragment,
            R.id.reviewFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id in hideOnScreens) View.GONE else View.VISIBLE
        }
    }
}