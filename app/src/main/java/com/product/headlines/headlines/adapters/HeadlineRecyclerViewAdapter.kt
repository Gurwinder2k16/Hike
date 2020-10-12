package com.product.headlines.headlines.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.product.headlines.R
import com.product.headlines.headlines.activity.Headlines
import com.product.headlines.headlines.models.response.Photo
import kotlinx.android.synthetic.main.item_view_headlines.view.*

class HeadlineRecyclerViewAdapter(private val list: List<Photo>) :
    RecyclerView.Adapter<HeadLineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadLineViewHolder {
        return HeadLineViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_headlines, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HeadLineViewHolder, position: Int) {
        Glide.with(holder.mItemView.context)
            .load(getImageUrl(list[position], holder.mItemView.context))
            .centerCrop()
            .into(holder.mItemView.ivHeadLineImage)
        holder.mItemView.setOnClickListener {
            when (holder.mItemView.context) {
                is Headlines -> {
                    (holder.mItemView.context as Headlines).setDetailFragment(
                        list[position],
                        holder.mItemView
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    private fun getImageUrl(pPhoto: Photo, pContext: Context): String {
        return pContext.getString(
            R.string.format_url,
            pPhoto.farm,
            pPhoto.server,
            pPhoto.id,
            pPhoto.secret
        )
    }
}

class HeadLineViewHolder(var mItemView: View) : RecyclerView.ViewHolder(mItemView)


