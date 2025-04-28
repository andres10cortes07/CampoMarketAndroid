package com.example.campomarket.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.campomarket.R

class MainActivityComprador : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_main)

        val startAtHomeComprador = intent.getBooleanExtra("startAtHomeComprador", false)

        if (startAtHomeComprador) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_main) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.TodosLosProductosFragment)
        }
    }
}