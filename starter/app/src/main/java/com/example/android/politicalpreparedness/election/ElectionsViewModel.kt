package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.ProgressState
import com.example.android.politicalpreparedness.utils.ProgressState.*
import kotlinx.coroutines.launch

class ElectionsViewModel(private val dataSource: ElectionDao) : ViewModel() {

    companion object {
        const val TAG = "ElectionsViewModel"
    }

    // The internal MutableLiveData that holds the data to be displayed on the UI
    private val _upcomingElectionsList = MutableLiveData<List<Election>>()

    // The external immutable LiveData
    val upcomingElectionsList: LiveData<List<Election>>
        get() = _upcomingElectionsList

    // The internal MutableLiveData that holds the data to be displayed on the UI
    private val _savedElectionsList = MutableLiveData<List<Election>>()

    // The external immutable LiveData
    val savedElectionsList: LiveData<List<Election>>
        get() = _savedElectionsList

    // The internal MutableLiveData to store state value that changes when API call is complete.
    // Used for progress bar and recycler view visibility
    private val _upcomingElectionsLoadingState = MutableLiveData<ProgressState>()

    // The external immutable LiveData
    val upcomingElectionsLoadingState: LiveData<ProgressState>
        get() = _upcomingElectionsLoadingState

    // The internal MutableLiveData to store state value that changes when data is fetched from local db.
    // Used for progress bar, info text and recycler view visibility
    private val _savedElectionsLoadingState = MutableLiveData<ProgressState>()

    // The external immutable LiveData
    val savedElectionsLoadingState: LiveData<ProgressState>
        get() = _savedElectionsLoadingState

    // The internal MutableLiveData to store election data and to handle navigation to the
    // selected election list item
    private val _navigateToVoterInfo = MutableLiveData<Election>()

    // The external immutable LiveData for the navigation item
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    private val electionsRepository = ElectionsRepository(dataSource)

    /**
     * init{} is called immediately after view model is created.
     */
    init {
        _savedElectionsLoadingState.value = LOADING_NO_DATA
        getElectionsFromApi()
    }

    private fun getElectionsFromApi() {
        viewModelScope.launch {
            _upcomingElectionsLoadingState.value = LOADING_ACTIVE
            try {
                _upcomingElectionsList.value = electionsRepository.getElections()
                _upcomingElectionsLoadingState.value = LOADING_SUCCESS
            } catch (e: Exception) {
                _upcomingElectionsLoadingState.value = LOADING_FAILURE
                Log.d(TAG, e.printStackTrace().toString())
            }

        }
    }

    fun loadSavedElections() {
        viewModelScope.launch {
            _savedElectionsLoadingState.value = LOADING_ACTIVE
            try {
                val savedElections = electionsRepository.getSavedElections()
                _savedElectionsList.value = savedElections
                if (savedElections == null || savedElections.isEmpty()) {
                    _savedElectionsLoadingState.value = LOADING_NO_DATA
                } else {
                    _savedElectionsLoadingState.value = LOADING_SUCCESS
                }
            } catch (e: Exception) {
                _upcomingElectionsLoadingState.value = LOADING_FAILURE
                Log.d(TAG, e.printStackTrace().toString())
            }

        }
    }

    /**
     * When the Election list item clicked, set the [_navigateToVoterInfo] [MutableLiveData]
     * @param election The [Election] that was clicked on.
     */
    fun displayVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    /**
     * After the navigation has taken place, make sure navigateToVoterInfo is set to null
     */
    fun displayVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }
}