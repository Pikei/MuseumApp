package com.example.museumapp.ui.details
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.museumapp.R
import com.example.museumapp.database.DatabaseHelper
import com.example.museumapp.databinding.FragmentDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Calendar

class DetailsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var theme: String
    private var seen: Boolean = false
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val museumName = arguments?.getString("name")
        val dbHelper = DatabaseHelper(requireContext())
        val details = dbHelper.getDetails(museumName.toString())

        name = details["name"].toString()
        address = details["address"].toString()
        theme = details["theme"].toString()
        seen = details["seen"] == "1"
        longitude = details["longitude"]?.toDouble() ?: 0.0
        latitude = details["latitude"]?.toDouble() ?: 0.0

        setTextViews()
        setDetailsDate(dbHelper)
        setImage(dbHelper)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return root
    }

    private fun setTextViews() {
        binding.detailsName.text = name
        binding.detailsAddress.text = "Address: $address"
        binding.detailsTheme.text = "Theme: $theme"
        binding.detailsSeen.isChecked = seen
    }

    private fun setDetailsDate(dbHelper: DatabaseHelper) {
        if (seen) {
            binding.detailsDate.visibility = View.VISIBLE
            binding.detailsDate.text = "Visited Date: ${dbHelper.getVisitedDate(name)}"
        } else {
            binding.detailsDate.visibility = View.GONE
        }

        binding.detailsSeen.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showDatePickerDialog(dbHelper)
            } else {
                dbHelper.setMuseumAsUnvisited(name)
                binding.detailsDate.visibility = View.GONE
            }
        }
    }

    private fun showDatePickerDialog(dbHelper: DatabaseHelper) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
                dbHelper.setMuseumAsVisitedWithDate(name, selectedDate)
                binding.detailsDate.text = "Visited Date: $selectedDate"
                binding.detailsDate.visibility = View.VISIBLE
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun setImage(dbHelper: DatabaseHelper) {
        val imgName = dbHelper.getImgName(name)
        val imgId = resources.getIdentifier(imgName, "drawable", requireContext().packageName)
        binding.imageView.setImageResource(imgId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val location = LatLng(latitude, longitude)
        map.addMarker(MarkerOptions().position(location).title("Museum Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}