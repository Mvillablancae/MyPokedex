
package ui.pokemonList

import Network.PokeAPIClient
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.view.MenuInflater
import android.widget.AdapterView
import android.widget.SearchView
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


import com.example.mypokedex.R
import model.Pokemon

import ui.pokemonDetails.PokemonDetailsActivity


class PokedexListActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar

    private lateinit var pokemonAdapter: PokemonListAdapter


    private val pokemonList = mutableListOf<Pokemon>()
    private val originalPokemonList = mutableListOf<Pokemon>()
    private var isLoading = false
    private var offset = 0
    private var isLastPage : Boolean = false
    private var job: Job? = null


    private val coroutineScope: CoroutineScope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        listView = findViewById(R.id.listView)
        progressBar = findViewById(R.id.progressBar)

        pokemonAdapter = PokemonListAdapter(this, pokemonList)

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
                    if (pokemonList.size < VISIBLE_THRESHOLD) {
                        isLastPage = true
                    } else {
                        // Cargar más elementos cuando el usuario llega al final de la lista
                        isLastPage = false
                        loadMorePokemon()
                    }
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }
        })


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedName = pokemonList[position].name

            val intent = Intent(this, PokemonDetailsActivity::class.java)
            intent.putExtra("name", selectedName)
            startActivity(intent)
        }

        loadMorePokemon()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.layout.menu_search , menu)

        // Configurar la funcionalidad de la barra de búsqueda
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // Agregar el listener para la búsqueda
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Cancelar el job anterior si aún está en progreso
                if(job != null){
                    job!!.cancel()
                }
                // Iniciar una nueva búsqueda con un pequeño retraso para evitar búsquedas frecuentes mientras el usuario escribe
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(500) // Esperar 800 milisegundos antes de realizar la búsqueda
                    if (newText.isEmpty()) {
                        // Si el texto de búsqueda está vacío, restaurar la lista original
                        pokemonList.clear()
                        pokemonList.addAll(originalPokemonList)
                        pokemonAdapter.notifyDataSetChanged()
                    } else {
                        filterPokemonList(newText)
                    }
                }
                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Manejar el evento del botón de regreso en la barra de acción
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



    private fun filterPokemonList(filterText: String) {
        // Cancelar la búsqueda anterior si aún está en progreso
        if(job != null){
            job!!.cancel()
        }
        Toast.makeText(this@PokedexListActivity, "Buscando: $filterText", Toast.LENGTH_SHORT).show()
        // Iniciar una nueva búsqueda
        job = CoroutineScope(Dispatchers.IO).launch {
            val filteredList = mutableListOf<Pokemon>()

            for (pokemon in pokemonList) {
                if (pokemon.name?.startsWith(filterText, ignoreCase = true) == true) {
                    filteredList.add(pokemon)
                }
            }

            // Actualizar el ListView con los elementos filtrados en el hilo principal
            withContext(Dispatchers.Main) {
                pokemonAdapter.clear()
                pokemonAdapter.addAll(filteredList)
                pokemonAdapter.notifyDataSetChanged()
            }
        }
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
                    originalPokemonList.addAll(newPokemonList)
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

                Toast.makeText(this@PokedexListActivity, "Error desconocido, inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
            }
            pokemonList.clear()
            pokemonList.addAll(originalPokemonList)
            isLoading = false
            progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val VISIBLE_THRESHOLD = 15
        const val LIMIT = 20
    }


    // Llamar a cancel en onDestroy para cancelar el scope y liberar recursos
    override fun onDestroy() {
        super.onDestroy()

        coroutineScope.cancel()
    }
}