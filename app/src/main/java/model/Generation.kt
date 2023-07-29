package model

import com.google.gson.annotations.SerializedName


data class Generation (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("url"  ) var url  : String? = null

)