package com.example.musicplayerapp

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = 0

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var timeText: TextView
    lateinit var seekBar: SeekBar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        timeText = findViewById<TextView>(R.id.time_left)
        seekBar  = findViewById<SeekBar>(R.id.seek_bar)
        val playButton = findViewById<Button>(R.id.play_btn)
        val pauseButton = findViewById<Button>(R.id.pause_btn)
        val forwardButton = findViewById<Button>(R.id.forward_btn)
        val backwardButton = findViewById<Button>(R.id.back_btn)
        val titleText = findViewById<TextView>(R.id.title)


        mediaPlayer = MediaPlayer.create(
            this, R.raw.luniz_i_got_5_on_it
        )
        seekBar.isClickable = false

        playButton.setOnClickListener(View.OnClickListener{
            mediaPlayer.start()

            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()
            if (oneTimeOnly == 0){
                seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            timeText.text = startTime.toString()
            seekBar.setProgress(startTime.toInt())

            handler.postDelayed(updateSongTime, 100)
        })

        titleText.text = "" + resources.getResourceEntryName(R.raw.luniz_i_got_5_on_it)

        pauseButton.setOnClickListener(View.OnClickListener {
            mediaPlayer.pause()
        })

        forwardButton.setOnClickListener(View.OnClickListener {
            var temp = startTime
            if (temp + forwardTime <= finalTime){
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this,"Cannot jump forward", Toast.LENGTH_SHORT).show()
            }
        })
        backwardButton.setOnClickListener(View.OnClickListener {
            var temp = startTime
            if (temp - backwardTime >= 0){
                startTime -= backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }else{
                Toast.makeText(this,"Cannot jump backward", Toast.LENGTH_SHORT).show()
            }
        })

    }

    val updateSongTime: Runnable = object : Runnable{
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            timeText.text = ""+
                    String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime.toLong() - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(
                            startTime.toLong()
                        )
                    ))
                    )
            seekBar.progress = startTime.toInt()
            handler.postDelayed(this,100)
        }

    }

}