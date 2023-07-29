package api
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.Response


import model.PokemonDetailResponse

interface PokemonApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ArrayList<PokemonDetailResponse>>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): Response<PokemonDetailResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(@Path("id") id: Int): Response<PokemonDetailResponse>

}