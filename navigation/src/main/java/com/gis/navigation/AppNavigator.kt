package com.gis.navigation

import androidx.navigation.NavController

class AppNavigator {

  private lateinit var navController: NavController

  fun setNavController(navController: NavController) {
    this.navController = navController
  }
}