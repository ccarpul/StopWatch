package com.example.stopwatch

import android.os.Handler
import android.os.SystemClock
import android.widget.Chronometer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Runnable

class HomeViewModel : ViewModel() {

    lateinit var stopWatch: Chronometer
    private var laps = arrayListOf<String>()
    private var laps2 = arrayListOf<String>("Laps")


    private val _stateStopwatch = MutableLiveData<StateLiveData>()
    val stateStopwatch: LiveData<StateLiveData>
        get() {
            if (_stateStopwatch.value == StateLiveData.Pause) {
                stopWatch.text = "${if(min<10) "0$min" else min}:${if(sec<10) "0$sec" else sec}:" +
                        "${if(milliSec<100) "0$milliSec" else milliSec}"

            }
            return _stateStopwatch
        }

    var tMilliSec = 0L
    var tStar = 0L
    var tBuff = 0L
    var tUpdate = 0L
    var sec = 0
    var min = 0
    var milliSec = 0

    private val mHandler = Handler()

    private val runnable: Runnable = object : Runnable {
        override fun run() {

            tMilliSec = SystemClock.uptimeMillis() - tStar
            tUpdate = tBuff + tMilliSec
            sec = (tUpdate / 1000).toInt()
            min = sec / 60
            sec %= 60
            milliSec = (tUpdate % 1000).toInt()
            stopWatch.text = "${if(min<10) "0$min" else min}:${if(sec<10) "0$sec" else sec}:" +
                    "${if(milliSec<100) "0$milliSec" else milliSec}"
            mHandler.postDelayed(this, 60)
        }
    }

    sealed class StateLiveData {
        object Stop : StateLiveData()
        object Running : StateLiveData()
        object Pause : StateLiveData()
        object Lap : StateLiveData()
    }

    fun stopWatchStop() {

        tMilliSec = 0L
        tStar = 0L
        tBuff = 0L
        tUpdate = 0L
        sec = 0
        min = 0
        milliSec = 0
        stopWatch.text = "00:00:000"
        mHandler.removeCallbacks(runnable)
        stopWatch.stop()
        laps2 = arrayListOf("Laps")
        laps = arrayListOf()
        _stateStopwatch.value = StateLiveData.Lap
        _stateStopwatch.value = StateLiveData.Stop
    }

    fun stopWatchStartPause() {

        if (_stateStopwatch.value == StateLiveData.Running) {
            tBuff += tMilliSec
            mHandler.removeCallbacks(runnable)
            stopWatch.stop()
            _stateStopwatch.value = StateLiveData.Pause

        } else {
            tStar = SystemClock.uptimeMillis()
            mHandler.postDelayed(runnable, 0)
            stopWatch.start()
            _stateStopwatch.value = StateLiveData.Running
        }
    }

    fun stopWatchLap() {
        if (_stateStopwatch.value == StateLiveData.Running) {
            laps2 = arrayListOf()
            laps.add(stopWatch.text.toString())
            for (i in (0..laps.lastIndex).reversed()) {
                laps2.add("Lap ${i + 1}: ${laps[i]}")

                _stateStopwatch.value = StateLiveData.Lap
                _stateStopwatch.value = StateLiveData.Running
            }
        }
    }

    fun getLaps(): ArrayList<String> = laps2
}