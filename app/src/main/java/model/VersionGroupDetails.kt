package model

import com.google.gson.annotations.SerializedName


data class VersionGroupDetails (

  @SerializedName("level_learned_at"  ) var levelLearnedAt  : Int?             = null,
  @SerializedName("version_group"     ) var versionGroup    : VersionGroup?    = VersionGroup(),
  @SerializedName("move_learn_method" ) var moveLearnMethod : MoveLearnMethod? = MoveLearnMethod()

)