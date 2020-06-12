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

    private val _comments = MutableLiveData<String>()
    val getComments : LiveData<String>
        get() = _comments

    private val _timeline = MutableLiveData<String>()
    val getTimeline : LiveData<String>
        get() = _timeline

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel destroyed")
        coroutineJob?.cancel()
    }

    init {
        Log.i("HomeTimelineViewModel", "HomeTimeline ViewModel created")
        _score.value = 0
        getTimeline()
//        getComments()
    }



    private var coroutineJob : Job? = null

    private fun getComments() {
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            var commentList = WeiboApi.retrofitService.getComments()
            withContext(Dispatchers.Main) {
                try {
                    _comments.value = "Success: ${commentList.size} comments retrieved!"

                } catch (t:Throwable) {
                    _comments.value = "Failure: " + t.message
                }
            }
        }
    }

    private fun getTimeline() {
        coroutineJob = CoroutineScope(Dispatchers.IO).launch {
            var timelineList = WeiboApi.retrofitService.getTimeline()
            withContext(Dispatchers.Main) {
                try {
                    _timeline.value = "Success: ${timelineList.size} comments retrieved!"

                } catch (t:Throwable) {
                    _timeline.value = "Failure: " + t.message
                }
            }
        }
    }

    fun addOne() {
        _score.value = (_score.value)?.plus(1)
    }

}