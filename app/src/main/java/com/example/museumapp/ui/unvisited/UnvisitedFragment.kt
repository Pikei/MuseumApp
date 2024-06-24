package com.example.museumapp.ui.unvisited

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museumapp.R
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentUnvisitedBinding
import com.example.museumapp.ui.MuseumAdapter
import com.example.museumapp.ui.SearchableFragment

class UnvisitedFragment : Fragment(), SearchableFragment {

    private var _binding: FragmentUnvisitedBinding? = null
    private val binding get() = _binding!!
    private lateinit var museumAdapter: MuseumAdapter
    private lateinit var allMuseums: List<Map<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnvisitedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dbHelper = DatabaseHelper(requireContext())
        allMuseums = dbHelper.getMuseums(dbHelper.UNVISITED)

        museumAdapter = MuseumAdapter(allMuseums) { name -> onSectionClick(name) }
        binding.unvisitedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.unvisitedRecyclerView.adapter = museumAdapter

        return root
    }

    override fun filterData(query: String) {
        val filteredMuseums = allMuseums.filter {
            it["name"]?.contains(query, ignoreCase = true) == true
        }
        museumAdapter.updateList(filteredMuseums)
    }

    private fun onSectionClick(name: String) {
        Toast.makeText(requireContext(), "Clicked on: $name", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}