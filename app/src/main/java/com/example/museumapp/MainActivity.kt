package com.example.museumapp

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.ActivityMainBinding
import com.example.museumapp.ui.utils.SearchableFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeDatabase()
        setContentViewWithBinding()
        setSupportActionBarWithBinding()
        setupNavigationDrawerAndView()
        setupSearchViewListener()
        setupDestinationChangedListener()
    }

    private fun initializeDatabase() {
        val dbHelper = DatabaseHelper(this)
        dbHelper.init()
    }

    private fun setContentViewWithBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setSupportActionBarWithBinding() {
        setSupportActionBar(binding.appBarMain.toolbar)
    }

    private fun setupNavigationDrawerAndView() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_unvisited, R.id.nav_visited
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupSearchViewListener() {
        searchView = binding.appBarMain.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val currentFragment = supportFragmentManager
                    .primaryNavigationFragment?.childFragmentManager?.fragments?.firstOrNull()
                if (currentFragment is SearchableFragment) {
                    currentFragment.filterData(newText.orEmpty())
                }
                return true
            }
        })
    }

    private fun setupDestinationChangedListener() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_details) {
                searchView.visibility = View.GONE
            } else {
                searchView.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}