package com.example.obstacle_game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton

class MenuActivity : AppCompatActivity() {

    private val TAG = "MenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContentView(R.layout.activity_menu)
        } catch (t: Throwable) {
            Log.e(TAG, "CRASH in setContentView(activity_menu)", t)
            Toast.makeText(this, "Crash inflating activity_menu: ${t.message}", Toast.LENGTH_LONG).show()
            return
        }

        val switchFast = findViewById<View?>(R.id.switchFast) as? SwitchCompat
        val btnButtonMode = findViewById<View?>(R.id.btnButtonMode) as? MaterialButton
        val btnSensorMode = findViewById<View?>(R.id.btnSensorMode) as? MaterialButton
        val btnTopTen = findViewById<View?>(R.id.btnTopTen) as? MaterialButton

        if (switchFast == null || btnButtonMode == null || btnSensorMode == null || btnTopTen == null) {
            val msg =
                "activity_menu.xml types mismatch:\n" +
                        "switchFast is SwitchCompat? ${switchFast != null}\n" +
                        "btnButtonMode is MaterialButton? ${btnButtonMode != null}\n" +
                        "btnSensorMode is MaterialButton? ${btnSensorMode != null}\n" +
                        "btnTopTen is MaterialButton? ${btnTopTen != null}"
            Log.e(TAG, msg)
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            return
        }

        btnButtonMode.setOnClickListener {
            startGame(controlMode = "BUTTONS", isFast = switchFast.isChecked)
        }

        btnSensorMode.setOnClickListener {
            startGame(controlMode = "SENSORS", isFast = switchFast.isChecked)
        }

        btnTopTen.setOnClickListener {
            startActivity(Intent(this, HighScoresActivity::class.java))
        }

        forceRedraw()
    }

    override fun onResume() {
        super.onResume()
        forceRedraw()
    }

    private fun startGame(controlMode: String, isFast: Boolean) {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("CONTROL_MODE", controlMode)
        i.putExtra("IS_FAST", isFast)
        startActivity(i)
    }

    private fun forceRedraw() {
        val root = window.decorView
        root.post {
            root.invalidate()
            root.requestLayout()
        }
    }
}
