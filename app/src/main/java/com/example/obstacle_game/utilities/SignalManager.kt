package com.example.obstacle_game.utilities

import android.content.Context
import android.os.Vibrator
import android.widget.Toast

object SignalManager {
    fun toast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @Suppress("DEPRECATION")
    fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(150)
    }
}
