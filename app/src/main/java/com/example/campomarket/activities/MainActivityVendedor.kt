package com.example.campomarket.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.campomarket.R

class MainActivityVendedor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_main)

        val startAtHomeVendedor = intent.getBooleanExtra("startAtHomeVendedor", false)

        if (startAtHomeVendedor) {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_main) as NavHostFragment
            val navController = navHostFragment.navController

            navController.navigate(R.id.TodosLosProductosFragment)
        }
    }
}