package model

import com.google.gson.annotations.SerializedName


data class GenerationIv (

  @SerializedName("diamond-pearl"        ) var diamondPearl        : DiamondPearl?        = DiamondPearl(),
  @SerializedName("heartgold-soulsilver" ) var heartgoldSoulsilver : HeartgoldSoulsilver? = HeartgoldSoulsilver(),
  @SerializedName("platinum"             ) var platinum             : Platinum?             = Platinum()

)