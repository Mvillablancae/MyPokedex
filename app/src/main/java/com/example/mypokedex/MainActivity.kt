package com.example.mypokedex

import android.content.Intent
import android.app.Activity
import android.os.Bundle

import ui.pokedex.PokedexListActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Agregar el código para iniciar PokedexActivity
        val intent = Intent(this, PokedexListActivity::class.java)
        startActivity(intent)

        // Finalizar MainActivity para que no se quede en el historial
        finish()
        //setContentView(R.layout.activity_pokedex)
    }
}