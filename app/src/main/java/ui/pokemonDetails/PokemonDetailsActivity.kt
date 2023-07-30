package ui.pokemonDetails

import Network.PokeAPIClient
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import kotlinx.android.synthetic.main.pokemon_details.*
import model.Pokemon
import model.PokemonDetailResponse

class PokemonDetailsActivity : Activity() {
    private lateinit var progressBar: ProgressBar
    private var isLoading = false
    private var offset = 0
    private var pokemonDetails : PokemonDetailResponse? = null
    // Declarar una propiedad para el CoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_details)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        //progressBar = findViewById(R.id.progressBar)
        val intent = intent

        var nombre : String = intent.getStringExtra("name") ?: "Unknown"


        val textViewNombre = findViewById<TextView>(R.id.textViewNameValue)
        val textViewTipo = findViewById<TextView>(R.id.textViewTypeValue)
        val textViewPokedexId = findViewById<TextView>(R.id.textViewIdPokedexValue)

        // Mostrar el nombre en el TextView
        title = nombre
        textViewNombre.text = nombre
        loadPokemonDetails(nombre)
    }


    private fun loadPokemonDetails(name : String) {
        isLoading = true
        //progressBar.visibility = View.VISIBLE
        coroutineScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    val apiService = PokeAPIClient.pokemonApiService
                    apiService.getPokemonDetail(name)
                }
                if (response.isSuccessful) {
                    pokemonDetails = response.body()

                    if(pokemonDetails == null){
                        throw Exception("Pokemon no válido")
                    }else{
                        textViewIdPokedexValue.text = pokemonDetails?.id.toString()
                        textViewTypeValue.text = pokemonDetails?.types!!.first().type?.name
                    }

                } else {
                    Toast.makeText(
                        this@PokemonDetailsActivity,
                        "Error en el servidor, inténtelo de nuevo más tarde.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }catch (e: IOException) {
                // Error de conexión de red
                Toast.makeText(this@PokemonDetailsActivity, "Error de conexión de red", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(this@PokemonDetailsActivity, "Error desconocido, inténtelo de nuevo más tarde.", Toast.LENGTH_SHORT).show()
            }
            isLoading = false
            //progressBar.visibility = View.GONE
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