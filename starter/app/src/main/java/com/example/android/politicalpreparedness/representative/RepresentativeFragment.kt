package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale
import com.example.android.politicalpreparedness.R

class RepresentativeFragment : Fragment() {

    companion object {
        const val TAG = "RepresentativeFragment"
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
        const val LOCATION_PERMISSION_INDEX = 0
        const val MOTION_LAYOUT_STATE = "motion_layout_state"
    }

    private lateinit var binding: FragmentRepresentativeBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var connectivityManager: ConnectivityManager? = null

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
                }
            })

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        savedInstanceState?.run {
            binding.representativeMotionLayout.transitionState = this.getBundle(MOTION_LAYOUT_STATE)
        }
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

        connectivityManager =
            getSystemService(requireContext(), ConnectivityManager::class.java)

        binding.buttonSearch.setOnClickListener {
            if (connectivityManager?.activeNetwork == null) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.no_network_snack_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                onSearchButtonCLick()
            }
        }

        binding.buttonLocation.setOnClickListener {
            onLocationButtonCLick()
        }

        return binding.root
    }

    /*
     *  Requests ACCESS_FINE_LOCATION
     */
    @TargetApi(29)
    private fun requestForegroundLocationPermission() {
        if (foregroundLocationPermissionApproved())
            return
        val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        requestPermissions(permissionsArray, REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { lastKnownLocation: Location? ->
            if (lastKnownLocation != null) {
                val address = geoCodeLocation(lastKnownLocation)
                viewModel.fillInAddressForm(address)
                viewModel.searchRepresentatives(address.toFormattedString())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (
            grantResults.isEmpty() ||
            grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED
        ) {
            Snackbar.make(
                binding.root,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()

        } else {
            getCurrentLocation()
        }
    }

    private fun onLocationButtonCLick() {
        hideKeyboard()

        if (foregroundLocationPermissionApproved()) {
            getCurrentLocation()
        } else {
            requestForegroundLocationPermission()
        }
    }

    private fun onSearchButtonCLick() {
        hideKeyboard()

        val address = Address(
            binding.addressLine1.text.toString(),
            binding.addressLine2.text.toString(),
            binding.city.text.toString(),
            binding.state.selectedItem.toString(),
            binding.zip.text.toString()
        )

        viewModel.searchRepresentatives(address.toFormattedString())
    }

    /*
     *  Determines whether the app has the appropriate permissions across Android 10+ and all other
     *  Android versions.
     */
    @TargetApi(29)
    private fun foregroundLocationPermissionApproved(): Boolean {
        return (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                && PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))

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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putBundle(MOTION_LAYOUT_STATE, binding.representativeMotionLayout.transitionState)
        }
        super.onSaveInstanceState(outState)
    }
}