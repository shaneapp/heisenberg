package com.appleby.pinner.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appleby.pinner.R
import com.appleby.pinner.model.Collection
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.collection_listitem.view.*

class CollectionsListAdapter(private val context: Context,
                             private val itemClick: ((collection: Collection) -> Unit),
                             private val longItemClick: ((index: Int) -> Boolean)) : RecyclerView.Adapter<CollectionsListAdapter.ViewHolder>() {

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
        val collection = collectionData[position]

        if (collection.pinnedimages.count() > 0) {
            Glide.with(context)
                .load(collection.pinnedimages.first().imageUrl)
                .into(holder.ivIconPreview)

            holder.ivIconPreview.clipToOutline = true
        }

        holder.tvCollectionName.text = collection.name
        holder.tvCollectionMeta.text = "${collection.pinnedimages.count()} Pins"

        holder.view.setOnClickListener { itemClick(collection) }
        holder.view.setOnLongClickListener { longItemClick(position) }
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