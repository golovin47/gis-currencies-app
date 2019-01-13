package com.gis.featurecurrencies.presentation.ui.currenciesScreen

import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import com.gis.repoimpl.domain.entitiy.Currency

data class CurrenciesState(
  val loading: Boolean = true,
  val currencies: List<CurrencyListItem> = emptyList(),
  val error: Throwable? = null
)


sealed class CurrenciesIntent {
  class GetCurrencies(val base: String) : CurrenciesIntent()
  class ChangeBase(val base: String) : CurrenciesIntent()
  class ChangeAmount(val amount: Double) : CurrenciesIntent()
}


sealed class CurrenciesStateChange {
  class CurrenciesReceived(val currencies: CurrenciesDTO) : CurrenciesStateChange()
  class AmountChanged(val amount: Double) : CurrenciesStateChange()
  class Error(val error: Throwable) : CurrenciesStateChange()
  object HideError : CurrenciesStateChange()
}


data class CurrencyListItem(
  val currency: String = Currency.AUD.name,
  val amount: Double = 100.0,
  val rate: Double = 1.0
)


