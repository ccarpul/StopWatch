package com.example.stopwatch

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.extensions.customDrawable
import com.example.extensions.customText
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var adapterListLaps: ArrayAdapter<String>
    private lateinit var blink: Animation
    private lateinit var blinkOff: Animation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        viewModel.stopWatch = stopWatch
        viewModel.stateStopwatch.observe(this, Observer(::upDateUi))
        buttonStop.setOnClickListener(this)
        buttonStarPause.setOnClickListener(this)
        buttonLap.setOnClickListener(this)
        adapterListLaps = ArrayAdapter(this, R.layout.laps, viewModel.getLaps())
        listViewLaps.adapter = adapterListLaps

        blinkOff = AlphaAnimation(0.0f, 1.0f)
        blinkOff.duration = 0


        blink = AlphaAnimation(0.0f, 1.0f)
        blink.apply {
            duration = 200
            startOffset = 100
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
    }

    private fun upDateUi(state: HomeViewModel.StateLiveData) {
        when (state) {
            HomeViewModel.StateLiveData.Stop -> {
                buttonStarPause.customText(R.string.buttonNameStart)
                wrapperStopWatch.customDrawable(R.drawable.progressbar_red_custom)
                stopWatch.startAnimation(blinkOff)

            }
            HomeViewModel.StateLiveData.Running -> {
                buttonStarPause.customText(R.string.buttonNamePause)
                wrapperStopWatch.customDrawable(R.drawable.progressbar_green_custom)
                stopWatch.startAnimation(blinkOff)

            }
            HomeViewModel.StateLiveData.Pause -> {
                buttonStarPause.customText(R.string.buttonNameStart)
                wrapperStopWatch.customDrawable(R.drawable.progressbar_yellow_custom)
                stopWatch.startAnimation(blink)

            }
            HomeViewModel.StateLiveData.Lap -> {
                adapterListLaps = ArrayAdapter(this, R.layout.laps, viewModel.getLaps())
                listViewLaps.adapter = adapterListLaps
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonStop -> viewModel.stopWatchStop()
            R.id.buttonStarPause -> {
                viewModel.stopWatchStartPause()
            }
            R.id.buttonLap -> {
                viewModel.stopWatchLap()
            }
        }
    }
}