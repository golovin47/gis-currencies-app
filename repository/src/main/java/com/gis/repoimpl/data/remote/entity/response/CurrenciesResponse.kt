package com.gis.repoimpl.data.remote.entity.response

import androidx.collection.ArrayMap
import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
data class CurrenciesResponse(
  @JsonField(name = ["base"]) var base: String? = "",
  @JsonField(name = ["rates"]) var rates: HashMap<String, Double>? = HashMap()
)