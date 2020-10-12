package com.product.headlines.headlines.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.product.headlines.core.application.Applications
import com.product.headlines.core.constants.Constant
import com.product.headlines.headlines.models.request.HeadLineRequest
import com.product.headlines.headlines.models.response.HeadlineResponse
import com.product.headlines.headlines.networkUtils.NetworkApis
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class MainViewModel @Inject constructor(var pApplication: Application) :
    AndroidViewModel(pApplication) {

    private var mHeadlineResponse = MutableLiveData<HeadlineResponse>()

    fun getHeadLineList() = mHeadlineResponse

    @Inject
    lateinit var mRetrofit: Retrofit

    fun downloadHeadLines(pHeadLineRequest: HeadLineRequest) {
        when ((pApplication as Applications).checkConnection()) {
            false -> {
                getHeadLineList().postValue(HeadlineResponse())
                return
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val type: Type = object : TypeToken<Map<String?, String?>?>() {}.type
                val requestMap: Map<String, String> =
                    Gson().fromJson(Gson().toJson(pHeadLineRequest), type)
                val response =
                    mRetrofit.create(NetworkApis::class.java).getHeadLine(requestMap)
                when (response.stat) {
                    Constant.mSTATUS_OK -> {
                        getHeadLineList().postValue(response)
                    }
                    else -> {
                        getHeadLineList().postValue(HeadlineResponse())
                    }
                }

            } catch (e: Exception) {
                getHeadLineList().postValue(HeadlineResponse())
                Log.e(MainViewModel::class.java.simpleName, e.message!!)
            }
        }
    }
}