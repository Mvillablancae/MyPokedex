package model

import com.google.gson.annotations.SerializedName


data class GenerationVii (

  @SerializedName("icons"                ) var icons                : Icons?                = Icons(),
  @SerializedName("ultra-sun-ultra-moon" ) var ultraSunUltraMoon    : UltraSunUltraMoon ? = UltraSunUltraMoon()

)