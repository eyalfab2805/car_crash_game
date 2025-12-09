package com.example.obstacle_game

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.obstacle_game.utilities.GameManager
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var cells: Array<Array<ImageView>>
    private lateinit var heartViews: List<ImageView>
    private var gameManager: GameManager? = null
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!bindViews()) {
            Toast.makeText(this, "Initialization failed – check XML IDs", Toast.LENGTH_LONG).show()
            return
        }

        gameManager = GameManager(
            context = this,
            cells = cells,
            heartViews = heartViews
        )

        setupButtons()
    }

    override fun onStart() {
        super.onStart()
        startGameLoop()
    }

    override fun onStop() {
        super.onStop()
        stopGameLoop()
    }

    private fun startGameLoop() {
        stopGameLoop()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val gameRunning = gameManager?.tick() ?: false
                    if (!gameRunning) {
                        stopGameLoop()
                    }
                }
            }
        }, 0, 400L)
    }

    private fun stopGameLoop() {
        timer?.cancel()
        timer = null
    }

    private fun bindViews(): Boolean {
        return bindHearts() && bindGrid()
    }

    private fun bindHearts(): Boolean {
        val h1 = findViewById<ImageView?>(R.id.heart1)
        val h2 = findViewById<ImageView?>(R.id.heart2)
        val h3 = findViewById<ImageView?>(R.id.heart3)

        if (h1 == null || h2 == null || h3 == null) return false

        heartViews = listOf(h1, h2, h3)
        return true
    }

    private fun bindGrid(): Boolean {
        cells = arrayOf(
            arrayOf(findViewById(R.id.cell_0_0), findViewById(R.id.cell_0_1), findViewById(R.id.cell_0_2)),
            arrayOf(findViewById(R.id.cell_1_0), findViewById(R.id.cell_1_1), findViewById(R.id.cell_1_2)),
            arrayOf(findViewById(R.id.cell_2_0), findViewById(R.id.cell_2_1), findViewById(R.id.cell_2_2)),
            arrayOf(findViewById(R.id.cell_3_0), findViewById(R.id.cell_3_1), findViewById(R.id.cell_3_2)),
            arrayOf(findViewById(R.id.cell_4_0), findViewById(R.id.cell_4_1), findViewById(R.id.cell_4_2)),
            arrayOf(findViewById(R.id.cell_5_0), findViewById(R.id.cell_5_1), findViewById(R.id.cell_5_2)),
            arrayOf(findViewById(R.id.cell_6_0), findViewById(R.id.cell_6_1), findViewById(R.id.cell_6_2))
        )
        return true
    }

    private fun setupButtons() {
        findViewById<ImageView>(R.id.btnLeft).setOnClickListener {
            gameManager?.movePlayer("LEFT")
        }

        findViewById<ImageView>(R.id.btnRight).setOnClickListener {
            gameManager?.movePlayer("RIGHT")
        }
    }
}
