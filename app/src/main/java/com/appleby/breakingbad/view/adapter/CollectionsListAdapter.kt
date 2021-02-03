package com.appleby.breakingbad.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appleby.breakingbad.R
import com.appleby.breakingbad.model.Collection
import com.appleby.breakingbad.networkmodel.Items
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.character_listitem.view.*
import kotlinx.android.synthetic.main.collection_listitem.view.*

class CollectionsListAdapter(private val context: Context, private val itemClick: ((collection: Collection) -> Unit)) : RecyclerView.Adapter<CollectionsListAdapter.ViewHolder>() {

    private val collectionData = mutableListOf<Collection>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                R.layout.collection_listitem,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val characterItem = collectionData[position]

//        Glide.with(context)
//            .load(characterItem.link)
//            .into(holder.ivCharacterImage)

//        holder.ivCharacterImage.clipToOutline = true

        holder.tvCollectionName.text = characterItem.name
        holder.tvCollectionMeta.text = "${characterItem.pinnedimages.count()} Pins"

        holder.view.setOnClickListener { itemClick(characterItem) }
    }

    override fun getItemCount(): Int {
        return collectionData.count()
    }

    fun updateData(data: List<Collection>) {
        collectionData.clear()
        collectionData.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivIconPreview = itemView.ivIconPreview
        var tvCollectionName = itemView.tvCollectionName
        var tvCollectionMeta = itemView.tvCollectionMeta
        var view = itemView
    }

}