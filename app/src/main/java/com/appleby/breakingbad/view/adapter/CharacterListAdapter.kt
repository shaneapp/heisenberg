package com.appleby.breakingbad.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appleby.breakingbad.networkmodel.Character
import com.appleby.breakingbad.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.character_listitem.view.*

class CharacterListAdapter(private val context: Context, private val itemClick: ((characterId: Int) -> Unit)) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    private val characterData = mutableListOf<Character>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.character_listitem,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val characterItem = characterData[position]

        Glide.with(context)
            .load(characterItem.img)
            .into(holder.ivCharacterImage)

        holder.ivCharacterName.text = characterItem.name

        holder.view.setOnClickListener { itemClick(characterItem.char_id) }
    }

    override fun getItemCount(): Int {
        return characterData.count()
    }

    fun updateData(data: List<Character>) {
        characterData.clear()
        characterData.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCharacterImage = itemView.ivImage
        var ivCharacterName = itemView.tvName
        var view = itemView
    }

}