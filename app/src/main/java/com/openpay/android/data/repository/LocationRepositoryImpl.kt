package com.openpay.android.data.repository

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.openpay.android.data.local.MyLocationManager
import com.openpay.android.model.location.LocationEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LocationRepository {
    fun readLocations()
    fun saveLocations(locationEntity: List<LocationEntity>)
    fun startLocationUpdates()
    fun stopLocationUpdates()
    fun receivingLocationUpdates(): Flow<Boolean>
    fun getFireStoreNotification(): LiveData<String>
    fun getListLocationsFireStore(): LiveData<List<LocationEntity>>
}

@ExperimentalCoroutinesApi
class LocationRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val locationManager: MyLocationManager
) : LocationRepository {

    private var _fireStoreNotification = MutableLiveData<String>()
    private var _listLocationsFireStore = MutableLiveData<List<LocationEntity>>()

    override fun getFireStoreNotification(): LiveData<String> = _fireStoreNotification

    override fun getListLocationsFireStore(): LiveData<List<LocationEntity>> = _listLocationsFireStore

    /**
     * Read locations from Firebase
     */
    override fun readLocations() {
        firebaseFirestore.collection("locations")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val listLocations = arrayListOf<LocationEntity>()
                querySnapshot.documents.forEach {
                    val latitude = it.data?.get("latitude")
                    val longitude = it.data?.get("longitude")
                    if (latitude is Double && longitude is Double) {
                        listLocations.add(
                            LocationEntity(
                                latitude = latitude,
                                longitude = longitude,
                                date = (it.data?.get("date") as Timestamp).toDate(),
                                foreground = it.data?.get("foreground") as Boolean
                            )
                        )
                    }
                }
                _listLocationsFireStore.value = listLocations.toList()
            }
    }

    /**
     * Save locations from firebase
     */
    override fun saveLocations(locationEntity: List<LocationEntity>) {
        firebaseFirestore.collection("locations")
            .add(buildLocationEntity(locationEntity.first()))
            .addOnSuccessListener {
                _fireStoreNotification.value = locationEntity.toString()
            }.addOnFailureListener {
                _fireStoreNotification.value = "Location was not successfully stored in FireStore"
            }
    }

    /**
     * Status of whether the app is actively subscribed to location changes.
     */
    override fun receivingLocationUpdates(): Flow<Boolean> {
        return locationManager.receivingLocationUpdates.asFlow()
    }


    /**
     * Subscribes to location updates.
     */
    @MainThread
    override fun startLocationUpdates() = locationManager.startLocationUpdates()

    /**
     * Un-subscribes from location updates.
     */
    @MainThread
    override fun stopLocationUpdates() = locationManager.stopLocationUpdates()

    private fun buildLocationEntity(locationEntity: LocationEntity): HashMap<String, Any> {
        return hashMapOf(
            "latitude" to locationEntity.latitude,
            "longitude" to locationEntity.longitude,
            "date" to locationEntity.date,
            "foreground" to locationEntity.foreground
        )
    }
}
