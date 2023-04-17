package com.openpay.android.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openpay.android.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _receivingLocationUpdates = MutableLiveData<Boolean>()
    val receivingLocationUpdatesLiveData: LiveData<Boolean> = _receivingLocationUpdates

    val fireStoreNotificationLiveData = locationRepository.getFireStoreNotification()

    val listLocationFireStoreLiveData = locationRepository.getListLocationsFireStore()


    fun getLocationsFromFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.readLocations()
        }
    }

    fun getLocationUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.receivingLocationUpdates()
                .collect {
                    _receivingLocationUpdates.postValue(it)
                }
        }
    }

    fun startLocationUpdates() = locationRepository.startLocationUpdates()

    fun stopLocationUpdates() = locationRepository.stopLocationUpdates()
}