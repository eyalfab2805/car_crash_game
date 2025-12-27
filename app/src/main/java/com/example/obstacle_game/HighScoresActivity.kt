package com.example.obstacle_game

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.obstacle_game.data.ScoreEntry
import com.example.obstacle_game.ui.LocationMapFragment
import com.example.obstacle_game.ui.TopTenListFragment

class HighScoresActivity : AppCompatActivity(), TopTenListFragment.OnScoreClickListener {

    private var mapFragment: LocationMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        findViewById<Button>(R.id.btnBackToMenu).apply {
            bringToFront()
            setOnClickListener { goToMenu() }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToMenu()
            }
        })

        mapFragment = supportFragmentManager.findFragmentById(R.id.mapContainer) as? LocationMapFragment
        forceRedraw()
    }

    override fun onResume() {
        super.onResume()
        forceRedraw()
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(i)
        finish()
    }

    override fun onScoreClicked(entry: ScoreEntry) {
        mapFragment?.focusOn(entry)
    }

    private fun forceRedraw() {
        val root: View = window.decorView
        root.post {
            root.invalidate()
            root.requestLayout()
        }
    }
}
