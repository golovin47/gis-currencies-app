package com.gis.repoimpl.domain.repository

import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import io.reactivex.Observable

interface CurrenciesRepository {

  fun getCurrencies(base:String): Observable<CurrenciesDTO>
}