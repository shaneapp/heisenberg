package com.appleby.breakingbad.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.appleby.breakingbad.R
import com.appleby.breakingbad.networkmodel.Items
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.character_listitem.view.*

class ImageResultsListAdapter(private val context: Context, private val itemClick: ((index: Int, target: ImageView) -> Unit)) : RecyclerView.Adapter<ImageResultsListAdapter.ViewHolder>() {

    private val characterData = mutableListOf<Items>()

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
            .load(characterItem.link)
            .into(holder.ivCharacterImage)

        holder.ivCharacterImage.clipToOutline = true

        //holder.ivCharacterName.text = characterItem.name

        holder.view.setOnClickListener { itemClick(position, holder.ivCharacterImage) }
    }

    override fun getItemCount(): Int {
        return characterData.count()
    }

    fun updateData(data: List<Items>) {
        characterData.clear()
        characterData.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCharacterImage = itemView.ivImage
//        var ivCharacterName = itemView.tvName
        var view = itemView
    }

}