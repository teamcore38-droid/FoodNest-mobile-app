package com.umai.foodnest.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.umai.foodnest.data.FirebaseSeeder
import com.umai.foodnest.databinding.ActivitySplashBinding
import com.umai.foodnest.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Seed Firestore + Auth users on first launch
        FirebaseSeeder.seedIfNeeded()

        Log.d("FoodNest", """
            ┌─────────────────────────────────────┐
            │        SEED USER CREDENTIALS        │
            ├─────────────────────────────────────┤
            │ Customer: ${FirebaseSeeder.CUSTOMER_EMAIL}  │
            │ Password: ${FirebaseSeeder.CUSTOMER_PASSWORD}                  │
            ├─────────────────────────────────────┤
            │ Admin:    ${FirebaseSeeder.ADMIN_EMAIL}     │
            │ Password: ${FirebaseSeeder.ADMIN_PASSWORD}                  │
            └─────────────────────────────────────┘
        """.trimIndent())

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2500)
    }
}
