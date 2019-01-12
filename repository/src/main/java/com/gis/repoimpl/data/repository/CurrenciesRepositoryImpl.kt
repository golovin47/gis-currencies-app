package com.gis.repoimpl.data.repository

import com.gis.repoimpl.domain.datasource.CurrenciesDataSource
import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import com.gis.repoimpl.domain.repository.CurrenciesRepository
import io.reactivex.Observable

class CurrenciesRepositoryImpl(
  private val remoteSourceCurrencies: CurrenciesDataSource
) : CurrenciesRepository {

  override fun getCurrencies(base: String): Observable<CurrenciesDTO> =
    remoteSourceCurrencies.getCurrencies(base)
}