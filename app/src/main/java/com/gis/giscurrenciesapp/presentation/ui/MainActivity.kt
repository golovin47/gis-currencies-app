package com.gis.giscurrenciesapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.gis.giscurrenciesapp.R
import com.gis.giscurrenciesapp.databinding.ActivityMainBinding
import com.gis.navigation.AppNavigator
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        initBinding()
        initNavController()
    }

    private fun initBinding() {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private fun initNavController() {
        val mainNavController =
            (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment).navController
        val navigator: AppNavigator = get()
        navigator.setNavController(mainNavController)
    }
}
