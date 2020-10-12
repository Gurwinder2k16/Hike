package com.product.headlines.headlines.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.product.headlines.R
import com.product.headlines.headlines.models.response.Photo
import com.product.headlines.headlines.models.response.PhotoItems
import kotlinx.android.synthetic.main.detail_fragment.*

class HeadlinesDetailFragment : Fragment() {

    companion object {
        fun newInstance() = HeadlinesDetailFragment()
    }

    private lateinit var mPhotoItems: Photo

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDetailView()
    }

    fun setSelectedArticleItem(pPhotoItems: Photo) {
        mPhotoItems = pPhotoItems
    }

    private fun setDetailView() {
        when (::mPhotoItems.isInitialized) {
            true -> {
                tvHeadLineTitle.text = mPhotoItems.title
                tvSubtitle.text = mPhotoItems.owner
                Glide.with(context!!)
                    .load(getImageUrl(mPhotoItems,context!!))
                    .centerCrop()
                    .into(ivHeadLineImage)
            }
        }
        ivBack.setOnClickListener { activity.let { it?.onBackPressed() } }
    }

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