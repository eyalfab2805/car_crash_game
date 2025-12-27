package com.example.obstacle_game.data

data class ScoreEntry(
    val name: String,
    val score: Int,
    val coins: Int,
    val distance: Int,
    val timeMillis: Long,
    val lat: Double,
    val lng: Double
)
