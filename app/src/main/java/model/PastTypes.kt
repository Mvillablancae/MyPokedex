package model

import com.google.gson.annotations.SerializedName


data class PastTypes (

  @SerializedName("generation" ) var generation : Generation?      = Generation(),
  @SerializedName("types"      ) var types      : ArrayList<Types> = arrayListOf()

)