package com.gis.giscurrenciesapp.application

import android.app.Application
import com.gis.featurecurrencies.di.currenciesModule
import com.gis.navigation.di.navigationModule
import com.gis.repoimpl.di.repoModule
import org.koin.android.ext.android.startKoin

class GISCurrenciesApp : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin(this, listOf(currenciesModule, navigationModule, repoModule))
  }
}