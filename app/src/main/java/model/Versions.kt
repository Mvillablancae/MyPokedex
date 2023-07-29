package model

import com.google.gson.annotations.SerializedName


data class Versions (

  @SerializedName("generation-i"    ) var generationI    : GenerationI?    = GenerationI(),
  @SerializedName("generation-ii"   ) var generationIi   : GenerationIi?   = GenerationIi(),
  @SerializedName("generation-iii"  ) var generationIii  : GenerationIii?  = GenerationIii(),
  @SerializedName("generation-iv"   ) var generationIv   : GenerationIv?   = GenerationIv(),
  @SerializedName("generation-v"    ) var generationV    : GenerationV?    = GenerationV(),
  @SerializedName("generation-vi"   ) var generationVi   : GenerationVi?   = GenerationVi(),
  @SerializedName("generation-vii"  ) var generationVii  : GenerationVii?  = GenerationVii(),
  @SerializedName("generation-viii" ) var generationViii : GenerationViii? = GenerationViii()

)