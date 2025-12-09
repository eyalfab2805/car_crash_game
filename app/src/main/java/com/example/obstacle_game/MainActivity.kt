package com.example.obstacle_game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    companion object {
        private const val ROWS = 7
        private const val COLS = 3
        private const val TICK_MS = 400L
    }

    // 7x3 grid
    private lateinit var cells: Array<Array<ImageView>>
    private var gameReady = false

    // Hearts
    private lateinit var heartViews: List<ImageView>

    private var lives = 3

    // Player
    private val playerRow = ROWS - 1
    private var playerCol = 1 // middle

    // Obstacles
    data class Obstacle(var row: Int, var col: Int)

    private val obstacles = mutableListOf<Obstacle>()

    // Timer
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val ok = bindHearts() && bindGrid() && setupButtons()
        if (!ok) {
            Toast.makeText(this, "Initialization failed – check XML IDs", Toast.LENGTH_LONG).show()
            gameReady = false
            return
        }

        gameReady = true

        spawnObstacles(2)
        redrawGrid()
    }

    override fun onStart() {
        super.onStart()
        if (gameReady) {
            startGameLoop()
        }
    }

    override fun onStop() {
        super.onStop()
        stopGameLoop()
    }

    // ------------------------
    // Bind views safely
    // ------------------------

    private fun bindHearts(): Boolean {
        val h1 = findViewById<ImageView?>(R.id.heart1)
        val h2 = findViewById<ImageView?>(R.id.heart2)
        val h3 = findViewById<ImageView?>(R.id.heart3)

        if (h1 == null || h2 == null || h3 == null) {
            Toast.makeText(this, "Heart views not found – check XML IDs", Toast.LENGTH_LONG).show()
            return false
        }

        heartViews = listOf(h1, h2, h3)
        return true
    }

    private fun bindGrid(): Boolean {
        fun cell(id: Int, name: String): ImageView? {
            val v = findViewById<ImageView?>(id)
            if (v == null) {
                Toast.makeText(this, "Missing cell $name in XML", Toast.LENGTH_LONG).show()
            }
            return v
        }

        val c00 = cell(R.id.cell_0_0, "cell_0_0") ?: return false
        val c01 = cell(R.id.cell_0_1, "cell_0_1") ?: return false
        val c02 = cell(R.id.cell_0_2, "cell_0_2") ?: return false

        val c10 = cell(R.id.cell_1_0, "cell_1_0") ?: return false
        val c11 = cell(R.id.cell_1_1, "cell_1_1") ?: return false
        val c12 = cell(R.id.cell_1_2, "cell_1_2") ?: return false

        val c20 = cell(R.id.cell_2_0, "cell_2_0") ?: return false
        val c21 = cell(R.id.cell_2_1, "cell_2_1") ?: return false
        val c22 = cell(R.id.cell_2_2, "cell_2_2") ?: return false

        val c30 = cell(R.id.cell_3_0, "cell_3_0") ?: return false
        val c31 = cell(R.id.cell_3_1, "cell_3_1") ?: return false
        val c32 = cell(R.id.cell_3_2, "cell_3_2") ?: return false

        val c40 = cell(R.id.cell_4_0, "cell_4_0") ?: return false
        val c41 = cell(R.id.cell_4_1, "cell_4_1") ?: return false
        val c42 = cell(R.id.cell_4_2, "cell_4_2") ?: return false

        val c50 = cell(R.id.cell_5_0, "cell_5_0") ?: return false
        val c51 = cell(R.id.cell_5_1, "cell_5_1") ?: return false
        val c52 = cell(R.id.cell_5_2, "cell_5_2") ?: return false

        val c60 = cell(R.id.cell_6_0, "cell_6_0") ?: return false
        val c61 = cell(R.id.cell_6_1, "cell_6_1") ?: return false
        val c62 = cell(R.id.cell_6_2, "cell_6_2") ?: return false

        cells = arrayOf(
            arrayOf(c00, c01, c02),
            arrayOf(c10, c11, c12),
            arrayOf(c20, c21, c22),
            arrayOf(c30, c31, c32),
            arrayOf(c40, c41, c42),
            arrayOf(c50, c51, c52),
            arrayOf(c60, c61, c62)
        )

        return true
    }

    private fun setupButtons(): Boolean {
        val btnLeft = findViewById<ImageView?>(R.id.btnLeft)
        val btnRight = findViewById<ImageView?>(R.id.btnRight)

        if (btnLeft == null || btnRight == null) {
            Toast.makeText(this, "Buttons not found – check XML IDs", Toast.LENGTH_LONG).show()
            return false
        }

        btnLeft.setOnClickListener {
            if (lives <= 0) return@setOnClickListener
            if (playerCol > 0) {
                playerCol--
                redrawGrid()
            }
        }

        btnRight.setOnClickListener {
            if (lives <= 0) return@setOnClickListener
            if (playerCol < COLS - 1) {
                playerCol++
                redrawGrid()
            }
        }

        return true
    }

    // ------------------------
    // Timer loop
    // ------------------------

    private fun startGameLoop() {
        stopGameLoop()
        timer = Timer()
        scheduleNextTick()
    }

    private fun scheduleNextTick() {
        timer?.schedule(object : TimerTask() {
            override fun run() {
                this@MainActivity.runOnUiThread {
                    updateObstacles()
                    scheduleNextTick()
                }
            }
        }, TICK_MS)
    }

    private fun stopGameLoop() {
        timer?.cancel()
        timer = null
    }

    // ------------------------
    // Obstacles
    // ------------------------

    private fun spawnObstacles(count: Int) {
        obstacles.clear()
        repeat(count) {
            obstacles += Obstacle(
                row = Random.nextInt(0, 3),
                col = Random.nextInt(0, COLS)
            )
        }
    }

    private fun updateObstacles() {
        if (lives <= 0) return

        var hit = false

        for (obs in obstacles) {
            obs.row++

            if (obs.row == playerRow && obs.col == playerCol) {
                hit = true
            }

            if (obs.row > playerRow) {
                obs.row = 0
                obs.col = Random.nextInt(0, COLS)
            }
        }

        if (hit) {
            onHit()
        } else {
            redrawGrid()
        }
    }

    // ------------------------
    // Drawing
    // ------------------------

    private fun redrawGrid() {
        // 1) Hide everything
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                cells[r][c].visibility = View.INVISIBLE
            }
        }

        // 2) Draw obstacles (tyres)
        for (obs in obstacles) {
            if (obs.row in 0 until ROWS) {
                val cell = cells[obs.row][obs.col]
                cell.visibility = View.VISIBLE
                cell.setImageResource(R.drawable.tyre)
            }
        }

        // 3) ALWAYS draw player car, even with 0 lives
        val playerCell = cells[playerRow][playerCol]
        playerCell.visibility = View.VISIBLE
        playerCell.setImageResource(R.drawable.car)
    }


    // ------------------------
    // Hit / lives
    // ------------------------

    private fun onHit() {
        if (lives <= 0) return  // already game over, ignore

        lives--
        heartViews.getOrNull(lives)?.visibility = View.INVISIBLE

        if (lives <= 0) {
            // Game over on this hit
            stopGameLoop()          // stop further movement
            redrawGrid()            // car is STILL drawn because redrawGrid() always draws it
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show()
            vibrate()
        } else {
            // Just a crash, still alive
            Toast.makeText(this, "Crash!", Toast.LENGTH_SHORT).show()
            vibrate()
            redrawGrid()
        }
    }
    @Suppress("DEPRECATION")
    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as android.os.Vibrator
        vibrator.vibrate(150)
       }

}



