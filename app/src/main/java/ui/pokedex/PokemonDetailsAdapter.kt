package ui.pokedex

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import model.PokemonDetailResponse
import com.example.mypokedex.R
import model.Pokemon

class PokemonDetailsAdapter(
    context: Activity,
    private val pokemonList: List<Pokemon>
) : ArrayAdapter<Pokemon>(context, R.layout.item_pokemon, pokemonList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val viewHolder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false)
            viewHolder = ViewHolder(itemView)
            itemView?.tag = viewHolder
        } else {
            viewHolder = itemView.tag as ViewHolder
        }

        val pokemon = getItem(position)
        viewHolder.bind(pokemon)

        return itemView!!
    }

    private class ViewHolder(itemView: View) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(pokemon: Pokemon?) {
            pokemon?.let {
                nameTextView.text = it.name
                Glide.with(imageView.context)
                    .load(it.url)
                    .into(imageView)
            }
        }
    }
}