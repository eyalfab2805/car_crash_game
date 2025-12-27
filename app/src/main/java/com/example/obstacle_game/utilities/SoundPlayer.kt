package com.example.obstacle_game.utilities

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.obstacle_game.R

class SoundPlayer(context: Context) {

    private val soundPool: SoundPool
    private val crashId: Int

    init {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attrs)
            .build()

        crashId = soundPool.load(context, R.raw.crash, 1)
    }

    fun playCrash() {
        soundPool.play(crashId, 1f, 1f, 1, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }
}
