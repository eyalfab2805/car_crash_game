package com.example.obstacle_game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacle_game.R
import com.example.obstacle_game.data.ScoreEntry
import com.example.obstacle_game.data.ScoreRepository

class TopTenListFragment : Fragment() {

    interface OnScoreClickListener {
        fun onScoreClicked(entry: ScoreEntry)
    }

    private var listener: OnScoreClickListener? = null
    private val adapter = TopTenAdapter { entry -> listener?.onScoreClicked(entry) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = activity as? OnScoreClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_top_ten, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.recyclerTopTen)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter
        return root
    }

    override fun onResume() {
        super.onResume()
        val top = ScoreRepository.getTop10(requireContext())
        adapter.submitList(top)
        if (top.isNotEmpty()) {
            listener?.onScoreClicked(top[0])
        }
    }
}
