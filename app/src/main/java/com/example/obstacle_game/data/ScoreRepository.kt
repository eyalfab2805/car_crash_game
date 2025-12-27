package com.example.obstacle_game.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object ScoreRepository {

    private const val PREFS = "scores_prefs"
    private const val KEY = "top_scores"

    fun getTop10(context: Context): List<ScoreEntry> {
        val sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = sp.getString(KEY, "[]") ?: "[]"
        val arr = JSONArray(raw)

        val list = mutableListOf<ScoreEntry>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list += ScoreEntry(
                name = o.optString("name", "Player"),
                score = o.optInt("score", 0),
                coins = o.optInt("coins", 0),
                distance = o.optInt("distance", 0),
                timeMillis = o.optLong("timeMillis", 0L),
                lat = o.optDouble("lat", 0.0),
                lng = o.optDouble("lng", 0.0)
            )
        }

        return list.sortedWith(
            compareByDescending<ScoreEntry> { it.score }
                .thenByDescending { it.distance }
                .thenByDescending { it.coins }
                .thenByDescending { it.timeMillis }
        ).take(10)
    }

    fun addScore(context: Context, entry: ScoreEntry) {
        val current = getTop10(context).toMutableList()
        current += entry

        val top = current.sortedWith(
            compareByDescending<ScoreEntry> { it.score }
                .thenByDescending { it.distance }
                .thenByDescending { it.coins }
                .thenByDescending { it.timeMillis }
        ).take(10)

        val arr = JSONArray()
        for (e in top) {
            val o = JSONObject()
            o.put("name", e.name)
            o.put("score", e.score)
            o.put("coins", e.coins)
            o.put("distance", e.distance)
            o.put("timeMillis", e.timeMillis)
            o.put("lat", e.lat)
            o.put("lng", e.lng)
            arr.put(o)
        }

        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, arr.toString())
            .apply()
    }

}
