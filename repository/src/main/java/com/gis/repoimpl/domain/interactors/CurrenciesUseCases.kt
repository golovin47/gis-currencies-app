package com.gis.repoimpl.domain.interactors

import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import com.gis.repoimpl.domain.repository.CurrenciesRepository
import io.reactivex.Observable

class GetCurrenciesUseCase(private val repository: CurrenciesRepository) {

  fun execute(base: String): Observable<CurrenciesDTO> = repository.getCurrencies(base)
}