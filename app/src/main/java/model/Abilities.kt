package model

import com.google.gson.annotations.SerializedName


data class Abilities (

  @SerializedName("is_hidden" ) var isHidden : Boolean? = null,
  @SerializedName("slot"      ) var slot     : Int?     = null,
  @SerializedName("ability"   ) var ability  : Ability? = Ability()

)