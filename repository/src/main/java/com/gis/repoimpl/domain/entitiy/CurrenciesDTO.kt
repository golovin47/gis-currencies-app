package com.gis.repoimpl.domain.entitiy

import androidx.collection.ArrayMap

data class CurrenciesDTO(
  val base: String = "",
  val rates: HashMap<String, Double> = HashMap()
)