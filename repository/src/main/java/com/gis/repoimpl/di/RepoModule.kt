package com.gis.repoimpl.di

import android.content.Context.MODE_PRIVATE
import com.gis.repoimpl.data.remote.api.GISCurrenciesApiProvider
import com.gis.repoimpl.data.remote.datasource.CurrenciesRemoteSource
import com.gis.repoimpl.data.repository.CurrenciesRepositoryImpl
import com.gis.repoimpl.domain.datasource.CurrenciesDataSource
import com.gis.repoimpl.domain.interactors.GetCurrenciesUseCase
import com.gis.repoimpl.domain.repository.CurrenciesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val repoModule = module {

  single { GISCurrenciesApiProvider.createApi() }

  single { androidContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE) }

  //Currencies

  single<CurrenciesDataSource>("remote") { CurrenciesRemoteSource(get()) }

  single<CurrenciesRepository> { CurrenciesRepositoryImpl(get("remote")) }

  factory { GetCurrenciesUseCase(get()) }
}