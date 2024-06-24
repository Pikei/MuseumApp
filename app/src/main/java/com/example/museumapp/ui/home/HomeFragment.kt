package com.example.museumapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentHomeBinding
import com.example.museumapp.ui.MuseumAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dbHelper = DatabaseHelper(requireContext())
        val museums = dbHelper.getMuseums(dbHelper.ALL)

        val recyclerView = binding.homeRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MuseumAdapter(museums)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
