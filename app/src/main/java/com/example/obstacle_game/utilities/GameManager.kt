package com.example.obstacle_game.utilities

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.example.obstacle_game.R
import kotlin.random.Random

class GameManager(
    private val context: Context,
    private val cells: Array<Array<ImageView>>,
    private val heartViews: List<ImageView>,
    private val onCrash: () -> Unit,
    private val onCoinsChanged: (Int) -> Unit
) {

    companion object {
        const val ROWS = 8
        const val COLS = 5
    }

    private var lives = 3
    private var coins = 0

    private val playerRow = ROWS - 1
    private var playerCol = COLS / 2

    data class Obstacle(var row: Int, var col: Int)
    data class Coin(var row: Int, var col: Int)

    private val obstacles = mutableListOf<Obstacle>()
    private var coin = Coin(row = -2, col = COLS / 2)

    init {
        spawnObstacles(3)
        respawnCoinAbove()
        redrawGrid()
        onCoinsChanged(coins)
    }

    fun movePlayer(direction: String) {
        if (lives <= 0) return

        when (direction) {
            "LEFT" -> if (playerCol > 0) playerCol--
            "RIGHT" -> if (playerCol < COLS - 1) playerCol++
        }

        if (coin.row == playerRow && coin.col == playerCol) {
            collectCoin()
        }

        redrawGrid()
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

    private fun respawnCoinAbove() {
        coin.row = -Random.nextInt(2, ROWS)
        coin.col = randomFreeColForRow0to2()
    }

    private fun randomFreeColForRow0to2(): Int {
        while (true) {
            val c = Random.nextInt(0, COLS)
            val r = Random.nextInt(0, 3)
            val conflictsObstacle = obstacles.any { it.row == r && it.col == c }
            val conflictsPlayer = (r == playerRow && c == playerCol)
            if (!conflictsObstacle && !conflictsPlayer) return c
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

        coin.row++

        if (coin.row == playerRow && coin.col == playerCol) {
            collectCoin()
        }

        if (coin.row > playerRow) {
            respawnCoinAbove()
        }

        if (hit) onHit() else redrawGrid()

        return lives > 0
    }

    private fun collectCoin() {
        coins++
        onCoinsChanged(coins)
        respawnCoinAbove()
        redrawGrid()
    }

    private fun redrawGrid() {
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                cells[r][c].visibility = View.INVISIBLE
            }
        }

        if (coin.row in 0 until ROWS) {
            val cell = cells[coin.row][coin.col]
            cell.visibility = View.VISIBLE
            cell.setImageResource(R.drawable.coin)
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
        if (lives in 0 until heartViews.size) {
            heartViews[lives].visibility = View.INVISIBLE
        }

        onCrash()

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
