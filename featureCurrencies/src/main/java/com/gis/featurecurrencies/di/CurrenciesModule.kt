package com.gis.featurecurrencies.di

import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val currenciesModule = module {

  viewModel { CurrenciesViewModel(get()) }
}