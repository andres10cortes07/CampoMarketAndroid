package com.example.campomarket.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.campomarket.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_main)

        val startAtHomeAdmin = intent.getBooleanExtra("startAtHomeAdmin", false)

        if (startAtHomeAdmin) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_main) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.layoutFragmentHomeAdmin)
        }
    }

}