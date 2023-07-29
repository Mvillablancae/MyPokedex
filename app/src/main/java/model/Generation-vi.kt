package model

import com.google.gson.annotations.SerializedName


data class GenerationVi (

  @SerializedName("omegaruby-alphasapphire" ) var omegarubyAlphasapphire : OmegarubyAlphasapphire? = OmegarubyAlphasapphire(),
  @SerializedName("x-y"                     ) var xy                     : Xy?                     = Xy()

)