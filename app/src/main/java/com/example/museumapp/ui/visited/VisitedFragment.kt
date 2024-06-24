package com.example.museumapp.ui.visited

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentVisitedBinding
import com.example.museumapp.ui.utils.MuseumAdapter
import com.example.museumapp.ui.utils.SearchableFragment

class VisitedFragment : Fragment(), SearchableFragment {

    private var _binding: FragmentVisitedBinding? = null
    private val binding get() = _binding!!
    private lateinit var museumAdapter: MuseumAdapter
    private lateinit var allMuseums: List<Map<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dbHelper = DatabaseHelper(requireContext())
        allMuseums = dbHelper.getMuseums(dbHelper.VISITED)

        museumAdapter = MuseumAdapter(allMuseums) { name -> onSectionClick(name) }
        binding.visitedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.visitedRecyclerView.adapter = museumAdapter

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