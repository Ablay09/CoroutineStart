package com.example.coroutinestart

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    fun method() {
        val childJob1 = coroutineScope.launch {
            delay(3000L)
            Log.d(LOG_TAG, "childJob1 finished")
            val childJob1_1 = launch {
                delay(3000L)
                Log.d(LOG_TAG, "childJob1_1 finished")
            }
            val childJob1_2 = launch {
                delay(4000L)
                childJob1_1.cancel()
                Log.d(LOG_TAG, "childJob1_2 finished")
            }
        }
        val childJob2 = coroutineScope.launch {
            delay(2000L)
            Log.d(LOG_TAG, "childJob2 finished")
            launch {
                delay(2000L)
                Log.d(LOG_TAG, "childJob2_1 finished")
            }
        }

        Log.d(LOG_TAG, parentJob.children.contains(childJob1).toString())
        Log.d(LOG_TAG, parentJob.children.contains(childJob2).toString())
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}