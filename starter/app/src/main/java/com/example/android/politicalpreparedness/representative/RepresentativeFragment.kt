package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import java.util.Locale

class RepresentativeFragment : Fragment() {

    companion object {
        const val TAG = "RepresentativeFragment"
        //TODO: Add Constant for Location request
    }

    private lateinit var binding: FragmentRepresentativeBinding

    /**
     * Lazily initialize [RepresentativeViewModel].
     */
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(
            this,
            RepresentativeViewModelFactory()
        ).get(RepresentativeViewModel::class.java)
    }

    /**
     * Representative RecyclerView adapter.
     */
    private var representativeListAdapter: RepresentativeListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.representativesList.observe(
            viewLifecycleOwner,
            Observer<List<Representative>> { representativesList ->
                representativesList?.apply {
                    representativeListAdapter?.representatives = representativesList
                    viewModel.displayRepresentativesListComplete()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the RepresentativeViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the representative RecyclerView
        representativeListAdapter = RepresentativeListAdapter()

        binding.representativesRecyclerView.adapter = representativeListAdapter

        binding.buttonSearch.setOnClickListener {
            onSearchButtonCLick()
        }

        binding.buttonLocation.setOnClickListener {
            onLocationButtonCLick()
        }

        return binding.root

        //TODO: Establish bindings

        //TODO: Populate Representative adapter

        //TODO: Establish button listeners for field and location search

    }

    private fun onLocationButtonCLick() {
        Log.d(TAG, "buttonLocation clicked")
    }

    private fun onSearchButtonCLick() {
        Log.d(TAG, "buttonSearch clicked")
        viewModel.searchRepresentatives()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}