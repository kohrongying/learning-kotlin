package com.thoughtworks.miniweibo.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoughtworks.miniweibo.api.WeiboApi
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeTimelineViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>()

    val score : LiveData<Int>
        get() = _score

    init {
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel created")
        _score.value = 0
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel destroyed")
    }

    private val _response = MutableLiveData<String>()

    val response : LiveData<String>
        get() = _response

    private fun getComments() {
        WeiboApi.retrofitService.getComments().enqueue( object: Callback,
            retrofit2.Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
            }

        })
    }

    fun addOne() {
        _score.value = (score.value)?.plus(1)
    }

}