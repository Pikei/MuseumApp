package com.example.museumapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.museumapp.R
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentHomeBinding
import com.example.museumapp.ui.details.DetailsFragment
import com.example.museumapp.ui.utils.MuseumAdapter
import com.example.museumapp.ui.utils.SearchableFragment

class HomeFragment : Fragment(), SearchableFragment {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var museumAdapter: MuseumAdapter
    private lateinit var allMuseums: List<Map<String, String>>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dbHelper = DatabaseHelper(requireContext())
        allMuseums = dbHelper.getMuseums(dbHelper.ALL)

        museumAdapter = MuseumAdapter(allMuseums) { name -> onSectionClick(name) }
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = museumAdapter
        return root
    }

    override fun filterData(query: String) {
        val filteredMuseums = allMuseums.filter {
            it["name"]?.contains(query, ignoreCase = true) == true
        }
        museumAdapter.updateList(filteredMuseums)
    }

    private fun onSectionClick(name: String) {
        val detailsFragment = DetailsFragment()
        val bundle = Bundle()
        bundle.putString("name", name)
        detailsFragment.arguments = bundle
        findNavController().navigate(R.id.nav_details, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}