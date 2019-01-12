package com.gis.repoimpl.data.remote.datasource

import androidx.collection.ArrayMap
import com.gis.repoimpl.data.remote.api.GISCurrenciesApi
import com.gis.repoimpl.data.remote.entity.response.CurrenciesResponse
import com.gis.repoimpl.domain.datasource.CurrenciesDataSource
import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import io.reactivex.Observable

class CurrenciesRemoteSource(private val api: GISCurrenciesApi) : CurrenciesDataSource {

  override fun getCurrencies(base: String): Observable<CurrenciesDTO> = api.getCurrencies(base)
    .map { it.toDTO() }

  private fun CurrenciesResponse.toDTO(): CurrenciesDTO =
    CurrenciesDTO(
      base = base ?: "",
      rates = rates ?: HashMap())
}