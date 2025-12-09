package com.example.obstacle_game.utilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.example.obstacle_game.R
import kotlin.random.Random

class GameManager(
    private val context: Context,
    private val cells: Array<Array<ImageView>>,
    private val heartViews: List<ImageView>
) {

    companion object {
        const val ROWS = 7
        const val COLS = 3
    }

    private var lives = 3


    private val playerRow = ROWS - 1
    private var playerCol = 1

    data class Obstacle(var row: Int, var col: Int)

    private val obstacles = mutableListOf<Obstacle>()

    init {
        spawnObstacles(3)
        redrawGrid()
    }

    fun movePlayer(direction: String) {
        if (lives <= 0) return
        when (direction) {
            "LEFT" -> {
                if (playerCol > 0) {
                    playerCol--
                    redrawGrid()
                }
            }
            "RIGHT" -> {
                if (playerCol < COLS - 1) {
                    playerCol++
                    redrawGrid()
                }
            }
        }
    }

    private fun spawnObstacles(count: Int) {
        obstacles.clear()
        repeat(count) {
            obstacles += Obstacle(
                row = Random.nextInt(0, 3),
                col = Random.nextInt(0, COLS)
            )
        }
    }

    fun tick(): Boolean {
        if (lives <= 0) return false

        var hit = false

        for (obs in obstacles) {
            obs.row++

            if (obs.row == playerRow && obs.col == playerCol) {
                hit = true
            }

            if (obs.row > playerRow) {
                obs.row = -Random.nextInt(0, ROWS / 2)
                obs.col = Random.nextInt(0, COLS)
            }
        }

        if (hit) {
            onHit()
        } else {
            redrawGrid()
        }

        return lives > 0
    }

    private fun redrawGrid() {
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                cells[r][c].visibility = View.INVISIBLE
            }
        }

        for (obs in obstacles) {
            if (obs.row in 0 until ROWS) {
                val cell = cells[obs.row][obs.col]
                cell.visibility = View.VISIBLE
                cell.setImageResource(R.drawable.tyre)
            }
        }

        val playerCell = cells[playerRow][playerCol]
        playerCell.visibility = View.VISIBLE
        playerCell.setImageResource(R.drawable.car)
    }

    private fun onHit() {
        if (lives <= 0) return

        lives--
        if (lives >= 0 && lives < heartViews.size) {
            heartViews[lives].visibility = View.INVISIBLE
        }

        if (lives <= 0) {
            redrawGrid()
            SignalManager.toast(context, "Game Over!")
            SignalManager.vibrate(context)
        } else {
            SignalManager.toast(context, "Crash!")
            SignalManager.vibrate(context)
            redrawGrid()
        }
    }
}
