package ui.pokemonDetails

import Network.PokeAPIClient
import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide

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
import kotlinx.android.synthetic.main.item_pokemon.*
import kotlinx.android.synthetic.main.pokemon_details.*


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


        // Obtener nombre a traves del intent
        val intent = intent
        var nombre : String = intent.getStringExtra("name") ?: "Unknown"




        // Mostrar el nombre en el TextView
        title = nombre
        loadPokemonDetails(nombre)
    }

    // Opcional: Si deseas manejar el clic en el botón de regreso
    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun loadPokemonDetails(name : String) {
        isLoading = true
        //progressBar.visibility = View.VISIBLE
        val textViewTipo = findViewById<TextView>(R.id.textViewTypeValue)
        val textViewHeight = findViewById<TextView>(R.id.textViewHeightValue)
        val textViewWeight = findViewById<TextView>(R.id.textViewWeightValue)

        val imageView = findViewById<ImageView>(R.id.imageViewPokemonDefaultSprite)
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
                        title = "#${pokemonDetails?.id.toString()} ${pokemonDetails?.name}"
                        textViewTypeValue.text = pokemonDetails?.types!!.first().type?.name
                        textViewHeight.text = "${pokemonDetails?.height.toString()} ft."
                        textViewWeight.text = "${pokemonDetails?.weight.toString()} lbs."
                        Glide.with(this@PokemonDetailsActivity)
                            .load(pokemonDetails!!.sprites!!.frontDefault)
                            .into(imageView)
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
                println("e: ${e.message}")
                Toast.makeText(this@PokemonDetailsActivity, "Error: ${e.message}.", Toast.LENGTH_SHORT).show()
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