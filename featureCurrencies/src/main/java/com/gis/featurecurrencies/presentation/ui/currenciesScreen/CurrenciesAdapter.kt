package com.gis.featurecurrencies.presentation.ui.currenciesScreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gis.featurecurrencies.R
import com.gis.featurecurrencies.databinding.ItemCurrenciesListBinding
import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesIntent.ChangeAmount
import com.gis.featurecurrencies.presentation.ui.currenciesScreen.CurrenciesIntent.ChangeBase
import com.gis.repoimpl.domain.entitiy.Currency
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.subjects.Subject
import java.text.NumberFormat
import java.util.*

const val KEY_AMOUNT_CHANGED = "KEY_AMOUNT_CHANGED"

class CurrenciesAdapter(private val eventsPublisher: Subject<CurrenciesIntent>) :
  ListAdapter<CurrencyListItem, CurrencyViewHolder>(object : DiffUtil.ItemCallback<CurrencyListItem>() {

    override fun areItemsTheSame(oldItem: CurrencyListItem, newItem: CurrencyListItem): Boolean =
      oldItem.currency == newItem.currency

    override fun areContentsTheSame(oldItem: CurrencyListItem, newItem: CurrencyListItem): Boolean =
      oldItem.rate == newItem.rate

    override fun getChangePayload(oldItem: CurrencyListItem, newItem: CurrencyListItem): Any? {
      return Bundle().apply {
        if (oldItem.amount != newItem.amount)
          putDouble(KEY_AMOUNT_CHANGED, newItem.amount)
      }
    }

  }) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
    CurrencyViewHolder(
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.item_currencies_list,
        parent,
        false
      )
    )

  override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
    holder.bind(getItem(position), eventsPublisher)
  }

  override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
    if (payloads.isEmpty()) {
      holder.bind(getItem(position), eventsPublisher)
      return
    }

    val payload = payloads[0]
    holder.updateAmount((payload as Bundle).getDouble(KEY_AMOUNT_CHANGED))
  }
}


class CurrencyViewHolder(private val binding: ItemCurrenciesListBinding) : RecyclerView.ViewHolder(binding.root) {

  @SuppressLint("ClickableViewAccessibility")
  fun bind(item: CurrencyListItem, eventsPublisher: Subject<CurrenciesIntent>) {
    binding.tvCurrencyName.text = item.currency
    binding.etCurrencyAmount.setText(String.format("%.2f", item.amount))
    binding.ivCurrencyFlag.setImageResource(
      when (item.currency) {
        Currency.AUD.name -> R.drawable.flag_au
        Currency.BGN.name -> R.drawable.flag_bg
        Currency.BRL.name -> R.drawable.flag_br
        Currency.CAD.name -> R.drawable.flag_ca
        Currency.CHF.name -> R.drawable.flag_ch
        Currency.CNY.name -> R.drawable.flag_cn
        Currency.CZK.name -> R.drawable.flag_cz
        Currency.DKK.name -> R.drawable.flag_dk
        Currency.EUR.name -> R.drawable.flag_eu
        Currency.GBP.name -> R.drawable.flag_gb
        Currency.HKD.name -> R.drawable.flag_hk
        Currency.HRK.name -> R.drawable.flag_cr
        Currency.HUF.name -> R.drawable.flag_hu
        Currency.IDR.name -> R.drawable.flag_id
        Currency.ILS.name -> R.drawable.flag_il
        Currency.INR.name -> R.drawable.flag_in
        Currency.ISK.name -> R.drawable.flag_is
        Currency.JPY.name -> R.drawable.flag_jp
        Currency.KRW.name -> R.drawable.flag_kr
        Currency.MXN.name -> R.drawable.flag_mx
        Currency.MYR.name -> R.drawable.flag_my
        Currency.NOK.name -> R.drawable.flag_no
        Currency.NZD.name -> R.drawable.flag_nz
        Currency.PHP.name -> R.drawable.flag_ph
        Currency.PLN.name -> R.drawable.flag_pl
        Currency.RON.name -> R.drawable.flag_ro
        Currency.RUB.name -> R.drawable.flag_ru
        Currency.SEK.name -> R.drawable.flag_se
        Currency.SGD.name -> R.drawable.flag_sg
        Currency.THB.name -> R.drawable.flag_th
        Currency.TRY.name -> R.drawable.flag_tr
        Currency.USD.name -> R.drawable.flag_us
        Currency.ZAR.name -> R.drawable.flag_za
        else -> R.drawable.flag_au
      }
    )

    RxTextView.textChanges(binding.etCurrencyAmount)
      .filter { adapterPosition == 0 }
      .doOnNext { if (it.isBlank()) binding.etCurrencyAmount.setText("0") }
      .filter { it.isNotBlank() }
      .map { amount ->
        val nf = NumberFormat.getInstance(Locale.getDefault())
        val amountNumber = nf.parse(amount.toString())
        ChangeAmount(amountNumber.toDouble()) }
      .subscribe(eventsPublisher)

    binding.currencyItemRoot.setOnTouchListener { v, event ->
      binding.etCurrencyAmount.dispatchTouchEvent(event)
      return@setOnTouchListener true
    }

    binding.etCurrencyAmount.setOnTouchListener { v, event ->
      if (event.action == ACTION_UP && adapterPosition != 0) {
        eventsPublisher.onNext(ChangeBase(item.currency))
      }
      return@setOnTouchListener false
    }

    binding.etCurrencyAmount.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
      if (adapterPosition == 0) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!hasFocus) {
          imm.hideSoftInputFromWindow(v.windowToken, 0)
        } else
          imm.showSoftInput(v, 0)
      }
    }
  }

  fun updateAmount(amount: Double) {
    binding.etCurrencyAmount.setText(String.format("%.2f", amount))
  }
}