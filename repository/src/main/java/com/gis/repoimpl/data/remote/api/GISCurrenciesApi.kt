package com.gis.repoimpl.data.remote.api

import com.gis.repoimpl.data.remote.entity.response.CurrenciesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GISCurrenciesApi {

  @GET("latest")
  fun getCurrencies(@Query("base") base: String): Observable<CurrenciesResponse>
}