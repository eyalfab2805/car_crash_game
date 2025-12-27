package com.example.obstacle_game.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class TiltDetector(
    context: Context,
    private val onTiltLeft: () -> Unit,
    private val onTiltRight: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastMoveTime = 0L
    private var lastDir = 0

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
        lastDir = 0
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val now = System.currentTimeMillis()

        val dir = when {
            x > 3.0f -> -1
            x < -3.0f -> 1
            else -> 0
        }

        if (dir == 0) {
            lastDir = 0
            return
        }

        if (dir == lastDir) return
        if (now - lastMoveTime < 220) return

        lastMoveTime = now
        lastDir = dir

        if (dir == -1) onTiltLeft() else onTiltRight()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
