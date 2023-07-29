package model

import com.google.gson.annotations.SerializedName


data class PokemonDetailResponse (

  @SerializedName("id"                       ) var id                     : Int?                   = null,
  @SerializedName("name"                     ) var name                   : String?                = null,
  @SerializedName("base_experience"          ) var baseExperience         : Int?                   = null,
  @SerializedName("height"                   ) var height                 : Int?                   = null,
  @SerializedName("is_default"               ) var isDefault              : Boolean?               = null,
  @SerializedName("order"                    ) var order                  : Int?                   = null,
  @SerializedName("weight"                   ) var weight                 : Int?                   = null,
  @SerializedName("abilities"                ) var abilities              : ArrayList<Abilities>   = arrayListOf(),
  @SerializedName("forms"                    ) var forms                  : ArrayList<Forms>       = arrayListOf(),
  @SerializedName("game_indices"             ) var gameIndices            : ArrayList<GameIndices> = arrayListOf(),
  @SerializedName("held_items"               ) var heldItems              : ArrayList<HeldItems>   = arrayListOf(),
  @SerializedName("location_area_encounters" ) var locationAreaEncounters : String?                = null,
  @SerializedName("moves"                    ) var moves                  : ArrayList<Moves>       = arrayListOf(),
  @SerializedName("species"                  ) var species                : Species?               = Species(),
  @SerializedName("sprites"                  ) var sprites                : Sprites?               = Sprites(),
  @SerializedName("stats"                    ) var stats                  : ArrayList<Stats>       = arrayListOf(),
  @SerializedName("types"                    ) var types                  : ArrayList<Types>       = arrayListOf(),
  @SerializedName("past_types"               ) var pastTypes              : ArrayList<PastTypes>   = arrayListOf()

)