package ui.pokedex

import Network.PokeAPIClient
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast

//Manejo de errores
import java.lang.Exception
import java.io.IOException

//Manejo de hilos
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

import com.example.mypokedex.R
import model.Pokemon

class PokedexListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
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
        progressBar = findViewById(R.id.progressBar)
        pokemonAdapter = PokemonDetailsAdapter(this, pokemonList)
        listView.adapter = pokemonAdapter

        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if ((!isLoading && totalItemCount <= (firstVisibleItem + VISIBLE_THRESHOLD )) && offset != 0) {
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
        progressBar.visibility = View.VISIBLE
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
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
                    Toast.makeText(
                        this@PokedexListActivity,
                        "Error en el servidor, inténtelo de nuevo más tarde.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }catch (e: IOException) {
                    // Error de conexión de red
                    Toast.makeText(this@PokedexListActivity, "Error de conexión de red", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this@PokedexListActivity, "Error en el servidor, inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
            }

            isLoading = false
            progressBar.visibility = View.GONE
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