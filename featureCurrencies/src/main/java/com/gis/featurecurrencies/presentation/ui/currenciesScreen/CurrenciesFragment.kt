package com.gis.featurecurrencies.presentation.ui.currenciesScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.featurecurrencies.R
import com.gis.featurecurrencies.databinding.FragmentCurrenciesBinding
import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesIntent.*
import com.gis.repoimpl.domain.entitiy.Currency
import com.gis.utils.BaseView
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class CurrenciesFragment : Fragment(), BaseView<CurrenciesState> {

  private val eventsPublisher: PublishSubject<CurrenciesIntent> = PublishSubject.create()

  private var currentState: CurrenciesState? = null

  private var binding: FragmentCurrenciesBinding? = null
  private var viewSubscriptions: Disposable? = null
  private val vmCurrencies: CurrenciesViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initRecyclerView()
    initIntents()

    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    viewSubscriptions?.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_currencies, container, false)
  }

  private fun initRecyclerView() {
    binding!!.rvCurrencies.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = CurrenciesAdapter(eventsPublisher)
    }
  }

  override fun initIntents() {
    viewSubscriptions = Observable.merge(listOf(

      Observable.interval(600, TimeUnit.MILLISECONDS)
        .map {
          GetCurrencies(
            if (currentState != null && currentState!!.currencies.isNotEmpty())
              currentState!!.currencies[0].currency
            else Currency.AUD.name)
        },

      eventsPublisher.ofType(ChangeBase::class.java),

      eventsPublisher.ofType(ChangeAmount::class.java)
    ))
      .debounce { intent ->
        if (intent is GetCurrencies)
          Observable.empty<CurrenciesIntent>()
            .delay(400, TimeUnit.MILLISECONDS)
        else
          Observable.empty()
      }
      .subscribe(vmCurrencies.viewIntentsConsumer())
  }

  override fun handleStates() {
    vmCurrencies.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: CurrenciesState) {
    Log.d("CURRENCIES", state.currencies.toString())

    binding!!.loading = state.loading

    val shouldScrollToTop = currentState != null &&
      currentState!!.currencies.isNotEmpty() &&
      state.currencies.isNotEmpty() &&
      currentState!!.currencies[0].currency != state.currencies[0].currency

    currentState = state

    (binding!!.rvCurrencies.adapter as CurrenciesAdapter).submitList(state.currencies)

    if (shouldScrollToTop)
      binding!!.rvCurrencies.postDelayed({
        (binding!!.rvCurrencies.layoutManager as LinearLayoutManager).smoothScrollToPosition(binding!!.rvCurrencies, null, 0)
      }, 300)

    if (state.error != null)
      Snackbar.make(binding!!.root, state.error.message!!, Snackbar.LENGTH_SHORT).show()
  }
}