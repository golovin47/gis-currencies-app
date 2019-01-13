package com.gis.featurecurrencies.presentation.ui.currenciesScreen

import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesIntent.*
import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesStateChange.*
import com.gis.repoimpl.domain.entitiy.CurrenciesDTO
import com.gis.repoimpl.domain.interactors.GetCurrenciesUseCase
import com.gis.utils.BaseViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class CurrenciesViewModel(private val getCurrenciesUseCase: GetCurrenciesUseCase) : BaseViewModel<CurrenciesState>() {

  override fun initState(): CurrenciesState = CurrenciesState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(GetCurrencies::class.java)
        .switchMap { event ->
          getCurrenciesUseCase.execute(event.base)
            .map { currencies -> CurrenciesReceived(currencies) }
            .cast(CurrenciesStateChange::class.java)
            .onErrorResumeNext { error: Throwable -> handleError(error) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(ChangeBase::class.java)
        .switchMap { event ->
          getCurrenciesUseCase.execute(event.base)
            .map { currencies -> CurrenciesReceived(currencies) }
            .cast(CurrenciesStateChange::class.java)
            .onErrorResumeNext { error: Throwable -> handleError(error) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(ChangeAmount::class.java)
        .map { event -> AmountChanged(event.amount) }
    ))

  private fun handleError(error: Throwable) =
    Observable.just(Error(error), HideError)

  private fun processNewCurrencies(currencies: List<CurrencyListItem>, currenciesDTO: CurrenciesDTO): List<CurrencyListItem> {
    val base = CurrencyListItem(
      currency = currenciesDTO.base,
      amount = if (currencies.isNotEmpty()) currencies[0].amount else 100.0,
      rate = 1.0
    )

    val resultList = mutableListOf<CurrencyListItem>()

    for (currency in currenciesDTO.rates) {
      val secondaryCurrency =
        if (currencies.isNotEmpty())
          currencies
            .find { it.currency == currency.key }!!
            .copy(
              amount = currency.value * base.amount,
              rate = currency.value)
        else
          CurrencyListItem(
            currency = currency.key,
            amount = base.amount * currency.value,
            rate = currency.value)

      resultList.add(secondaryCurrency)
    }

    resultList.sortWith(compareBy { it.currency })
    resultList.add(0, base)

    return resultList
  }

  private fun processChangedAmount(amount: Double, currencies: List<CurrencyListItem>): List<CurrencyListItem> {
    val base = CurrencyListItem(currencies[0].currency, amount)
    val secondaryCurrencies = currencies.toMutableList().apply { removeAt(0) }
    val resultList = mutableListOf<CurrencyListItem>()

    for (item in secondaryCurrencies) {
      val secondaryCurrency = item
        .copy(amount = item.rate * amount)

      resultList.add(secondaryCurrency)
    }

    resultList.sortWith(compareBy { it.currency })
    resultList.add(0, base)

    return resultList
  }

  override fun reduceState(previousState: CurrenciesState, stateChange: Any): CurrenciesState =
    when (stateChange) {

      is CurrenciesReceived -> previousState.copy(
        loading = false,
        currencies = processNewCurrencies(previousState.currencies, stateChange.currencies)
      )

      is AmountChanged -> previousState.copy(
        currencies = processChangedAmount(stateChange.amount, previousState.currencies)
      )

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}