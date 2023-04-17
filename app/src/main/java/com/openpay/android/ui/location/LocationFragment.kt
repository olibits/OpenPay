package com.openpay.android.ui.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openpay.android.R
import com.openpay.android.databinding.FragmentLocationBinding
import com.openpay.android.utils.hasPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LocationFragment : Fragment(), PermissionRequestFragment.Callbacks {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private var activityListener: Callbacks? = null
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var map: GoogleMap
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        map = googleMap
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Callbacks) {
            activityListener = context

            // If fine location permission isn't approved, instructs the parent Activity to replace
            // this fragment with the permission request fragment.
            if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                activityListener?.requestFineLocationPermission()
                activityListener?.requestBackgroundLocationPermission()
            }
        } else {
            throw RuntimeException("$context must implement LocationUpdateFragment.Callbacks")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        createNotificationChannel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLocationUI()
    }

    private fun initView() {
        locationAdapter = LocationAdapter {
            val selectedPosition = LatLng(it.latitude, it.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(selectedPosition)
                    .title(it.getStringDate())
            )
            map.moveCamera(CameraUpdateFactory.newLatLng(selectedPosition))
        }
        binding.locationRecyclerView.adapter = locationAdapter
    }

    private fun initObserve() {
        locationViewModel.listLocationFireStoreLiveData.observe(viewLifecycleOwner, Observer {
            locationAdapter.submitList(it)
        })
        locationViewModel.receivingLocationUpdatesLiveData.observe(viewLifecycleOwner, Observer {
            updateStartOrStopButtonState(it)
        })
        locationViewModel.fireStoreNotificationLiveData.observeForever {
            buildNotification(it)
        }
    }

    private fun updateStartOrStopButtonState(receivingLocation: Boolean) {
        if (!receivingLocation) {
            locationViewModel.stopLocationUpdates()
        }
    }

    private fun buildNotification(message: String?) {
        message?.let {
            var icon = 0
            var title = "Notice"
            if (message.contains("Location was not successfully stored in FireStore")) {
                icon = R.drawable.ic_not_added_location_24
                title = "Location Not Added to FireStore"
            } else {
                icon = R.drawable.ic_added_location_24
                title = "Location Added to FireStore"
            }
            val builder = NotificationCompat.Builder(requireContext(), "1234")
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(it)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(requireContext())) {
                notify(1234, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "R.string.channel_name"
            val descriptionText = "R.string.channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1234", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(
                    requireContext(),
                    NotificationManager::class.java
                ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface Callbacks {
        fun requestFineLocationPermission()
        fun requestBackgroundLocationPermission()
    }

    override fun displayLocationUI() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initView()
        initObserve()
        locationViewModel.getLocationsFromFirebase()
        locationViewModel.getLocationUpdates()
        locationViewModel.startLocationUpdates()
    }
}