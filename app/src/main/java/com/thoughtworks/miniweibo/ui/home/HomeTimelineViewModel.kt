package com.thoughtworks.miniweibo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoughtworks.miniweibo.api.WeiboApi
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeTimelineViewModel : ViewModel() {
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
}