package com.openpay.android.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openpay.android.data.repository.PeopleRepository
import com.openpay.android.data.repository.Resource
import com.openpay.android.model.State
import com.openpay.android.model.people.PeopleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val peopleRepository: PeopleRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<State<PeopleResult>>()
    var profileState: LiveData<State<PeopleResult>> = _profileState

    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            peopleRepository.getPeople()
                .onStart { _profileState.postValue(State.loading()) }
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> resource.data?.results?.let { State.success(it.first()) }
                        is Resource.Failed -> State.error(resource.message)
                    }
                }
                .collect { state -> _profileState.postValue(state) }
        }
    }
}
