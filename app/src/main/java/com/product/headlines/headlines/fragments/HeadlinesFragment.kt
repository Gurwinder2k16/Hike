package com.product.headlines.headlines.fragments

import android.app.Application
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.product.headlines.R
import com.product.headlines.core.constants.Constant
import com.product.headlines.headlines.adapters.HeadlineRecyclerViewAdapter
import com.product.headlines.headlines.models.request.HeadLineRequest
import com.product.headlines.headlines.models.response.Photo
import com.product.headlines.headlines.viewmodels.MainViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject


class HeadlinesFragment : DaggerFragment() {

    companion object {
        fun newInstance() = HeadlinesFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var mArticlesItem = ArrayList<Photo>()
    private lateinit var mHeadlineRecyclerViewAdapter: HeadlineRecyclerViewAdapter

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mApplication: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setInitialRequest()
        viewModel = ViewModelProvider(this, mViewModelFactory).get(MainViewModel::class.java)
        viewModel.getHeadLineList().observe(viewLifecycleOwner, Observer {
            when (!it.stat.isNullOrEmpty()) {
                true -> {
                    noAvail.visibility = View.GONE
                    rvHeadLines.visibility = View.VISIBLE
                    mArticlesItem.addAll(it.photos?.photo!!)
                    setRecycleView()
                }
            }
        })
        svItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                when(svItems.query.toString().isNullOrEmpty()){
                    true->mArticlesItem.clear()
                }
                mHandler.removeCallbacks(mRunnable)
                mHandler.postDelayed(mRunnable, 400)
                return true
            }
        })
    }

    private val mHandler: Handler = Handler()

    private val mRunnable: Runnable = Runnable {
        when (::viewModel.isInitialized) {
            true -> {
                mHeadLineRequest.pSearchText=svItems.query.toString()
                viewModel.downloadHeadLines(mHeadLineRequest)
            }
        }
    }


    private fun setRecycleView() {
        when (::mHeadlineRecyclerViewAdapter.isInitialized) {
            false -> {
                mHeadlineRecyclerViewAdapter = HeadlineRecyclerViewAdapter(mArticlesItem)
                val gridManger = GridLayoutManager(context, 3)
                rvHeadLines.layoutManager = gridManger
                val divider: ItemDecoration = object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.setEmpty()
                    }
                }
                rvHeadLines.addItemDecoration(divider)
                setEndDetectListener()
                rvHeadLines.adapter = mHeadlineRecyclerViewAdapter
            }
        }
        mHeadlineRecyclerViewAdapter.notifyDataSetChanged()
        when (mArticlesItem.isEmpty()) {
            true -> {
                rvHeadLines.visibility = View.GONE
                noAvail.visibility = View.VISIBLE
            }
            else -> {
                rvHeadLines.visibility = View.VISIBLE
                noAvail.visibility = View.GONE
            }
        }
    }

    private fun setEndDetectListener() {
        rvHeadLines.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mHeadLineRequest.noOfCallbacks=mHeadLineRequest.noOfCallbacks+1
                    viewModel.downloadHeadLines(mHeadLineRequest)
                }
            }
        })
    }

    private lateinit var mHeadLineRequest: HeadLineRequest

    private fun setInitialRequest() {
        mHeadLineRequest = HeadLineRequest(
            pSearchText = "",
            apiKey = Constant.mAPIKey
        )
    }
}