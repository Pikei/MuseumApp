package com.example.museumapp.ui.unvisited

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentUnvisitedBinding
import com.example.museumapp.ui.MuseumAdapter

class UnvisitedFragment : Fragment() {

    private var _binding: FragmentUnvisitedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnvisitedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dbHelper = DatabaseHelper(requireContext())
        val museums = dbHelper.getMuseums(dbHelper.UNVISITED)

        val recyclerView = binding.unvisitedRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MuseumAdapter(museums)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}