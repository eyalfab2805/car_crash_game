package com.example.obstacle_game.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacle_game.R
import com.example.obstacle_game.data.ScoreEntry

class TopTenAdapter(
    private val onClick: (ScoreEntry) -> Unit
) : RecyclerView.Adapter<TopTenAdapter.VH>() {

    private val items = mutableListOf<ScoreEntry>()

    fun submitList(list: List<ScoreEntry>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtRank: TextView = itemView.findViewById(R.id.txtRank)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtScore: TextView = itemView.findViewById(R.id.txtScore)
        val txtMeta: TextView = itemView.findViewById(R.id.txtMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_score, parent, false)
        return VH(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        val e = items[position]
        holder.txtRank.text = "${position + 1}"
        holder.txtName.text = e.name
        holder.txtScore.text = "Distance: ${e.score}"
        holder.txtMeta.text = "Coins: ${e.coins}"
        holder.itemView.setOnClickListener { onClick(e) }
    }


    override fun getItemCount(): Int = items.size
}
