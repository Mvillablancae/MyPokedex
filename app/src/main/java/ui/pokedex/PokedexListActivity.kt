package ui.pokedex

import Network.PokeAPIClient
import android.app.Activity
import android.os.Bundle
import android.widget.AbsListView
import android.widget.ListView
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

//
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

import model.PokemonDetailResponse

import com.example.mypokedex.R
import model.Pokemon

class PokedexListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var pokemonAdapter: PokemonDetailsAdapter
    private val pokemonList = mutableListOf<Pokemon>()
    private var isLoading = false
    private var offset = 0

    // Declarar una propiedad para el CoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        listView = findViewById(R.id.listView)
        pokemonAdapter = PokemonDetailsAdapter(this, pokemonList)
        listView.adapter = pokemonAdapter

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (!isLoading && totalItemCount <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                    // Cargar más elementos cuando el usuario llega al final de la lista
                    loadMorePokemon()
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
        })

        loadMorePokemon()
    }


    private fun loadMorePokemon() {
        isLoading = true

        coroutineScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    // Realizar la llamada a la API utilizando Retrofit y coroutines
                    // Aquí debes usar tu propia URL de la API y adaptar el código para obtener los datos

                    val apiService = PokeAPIClient.pokemonApiService
                    apiService.getPokemonList(offset, LIMIT) // Llama a tu función de la API
                }

                if (response.isSuccessful) {
                    val pokemonListAPIResponse = response.body()
                    val newPokemonList = pokemonListAPIResponse?.results ?: emptyList()
                    pokemonList.addAll(newPokemonList)
                    pokemonAdapter.notifyDataSetChanged()
                    offset += newPokemonList.size
                } else {
                    // Manejar el error de la API
                }
            } catch (e: Exception) {
                println(e)
            }

            isLoading = false
        }
    }

    companion object {
        const val VISIBLE_THRESHOLD = 10
        const val LIMIT = 20
    }


    // Llamar a cancel en onDestroy para cancelar el scope y liberar recursos
    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}