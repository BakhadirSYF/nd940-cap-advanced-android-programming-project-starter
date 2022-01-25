package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel.RepresentativeSearchState.*

class RepresentativeViewModel : ViewModel() {

    companion object {
        const val TAG = "RepresentativeViewModel"
    }

    enum class RepresentativeSearchState {
        INITIAL, LOADING_ACTIVE, LOADING_SUCCESS, LOADING_FAILURE
    }

    // The internal MutableLiveData that holds the data to be displayed on the UI
    private val _representativesList = MutableLiveData<List<Representative>>()

    // The external immutable LiveData
    val representativesList: LiveData<List<Representative>>
        get() = _representativesList

    // The internal MutableLiveData to store boolean value that changes when list of representatives
    // displayed to the user. Used for progress bar visibility
    private val _representativesDisplayed = MutableLiveData<Boolean>()

    // The external immutable LiveData used for progress bar visibility via BindingsAdapters
    val representativesDisplayed: LiveData<Boolean>
        get() = _representativesDisplayed

    // The internal MutableLiveData to store state value that changes when user clicks on search button,
    // and when API call is complete. Used for progress bar, text info and recycler view visibility
    private val _currentSearchState = MutableLiveData<RepresentativeSearchState>()

    // The external immutable LiveData
    val currentSearchState: LiveData<RepresentativeSearchState>
        get() = _currentSearchState

    private val electionsRepository = ElectionsRepository(null)

    init {
        _currentSearchState.value = INITIAL
    }

    /**
     * After representatives list displayed to the user, set [_representativesDisplayed] to true
     */
    fun displayRepresentativesListComplete() {
        _representativesDisplayed.value = true
    }

    fun searchRepresentatives(address: String) {
        viewModelScope.launch {
            _representativesDisplayed.value = false
            _currentSearchState.value = LOADING_ACTIVE
            try {
                val representativeResponse = electionsRepository.getRepresentatives(address)
                _representativesList.value = representativeResponse.offices.flatMap { office ->
                    office.getRepresentatives(representativeResponse.officials)
                }
                _currentSearchState.value = LOADING_SUCCESS
            } catch (e: Exception) {
                _currentSearchState.value = LOADING_FAILURE
                Log.d(TAG, e.printStackTrace().toString())
            }

        }
    }
}
