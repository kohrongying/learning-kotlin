package com.thoughtworks.miniweibo.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoughtworks.miniweibo.api.WeiboApi
import kotlinx.coroutines.*

class HomeTimelineViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>()

    val score : LiveData<Int>
        get() = _score

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel destroyed")
        coroutineJob?.cancel()
    }

    private val _response = MutableLiveData<String>()

    val response : LiveData<String>
        get() = _response


    init {
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel created")
        _score.value = 0
        getComments()
    }


    private var coroutineJob : Job? = null

    private fun getComments() {
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            var commentList = WeiboApi.retrofitService.getComments()
            withContext(Dispatchers.Main) {
                try {
                    _response.value = "Success: ${commentList.size} comments retrieved!"

                } catch (t:Throwable) {
                    _response.value = "Failure: " + t.message
                }
            }
        }
    }

    fun addOne() {
        _score.value = (_score.value)?.plus(1)
    }

}