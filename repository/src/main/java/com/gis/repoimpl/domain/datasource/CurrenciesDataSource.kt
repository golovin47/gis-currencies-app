package com.gis.repoimpl.domain.datasource

import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import io.reactivex.Observable

interface CurrenciesDataSource {

  fun getCurrencies(base: String): Observable<CurrenciesDTO>
}