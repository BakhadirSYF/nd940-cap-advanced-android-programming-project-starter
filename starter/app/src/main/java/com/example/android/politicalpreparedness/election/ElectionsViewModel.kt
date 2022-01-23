package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import java.util.*

class ElectionsViewModel(private val dataSource: ElectionDao) : ViewModel() {
    private val TAG = "ElectionsViewModel"

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

    // The internal MutableLiveData to store boolean value that changes when list of elections
    // displayed to the user. Used for progress bar visibility
    private val _electionsDisplayed = MutableLiveData<Boolean>()

    // The external immutable LiveData used for progress bar visibility via BindingsAdapters
    val electionsDisplayed: LiveData<Boolean>
        get() = _electionsDisplayed

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
        viewModelScope.launch {
            _electionsDisplayed.value = false
            try {
                _upcomingElectionsList.value = electionsRepository.getElections()
            } catch (e: Exception) {
                Log.d(TAG, e.printStackTrace().toString())
            }

        }
    }

    fun loadSavedElections() {
        val dataList = mutableListOf<Election>()

        dataList.add(
            Election(
                2,
                "Wisconsin Presidential Primary Election",
                Date(),
                Division("id2", "US", "Wisconsin")
            )
        )

        _savedElectionsList.value = dataList
    }

    /**
     * After elections list displayed to the user, set [_electionsDisplayed] to true
     */
    fun displayElectionListComplete() {
        // TODO: need to use for ProgressBar
        _electionsDisplayed.value = true
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


    //TODO: Create live data val for upcoming elections

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    /**********/

    /*fun loadUpcomingElections() {
        val dataList = mutableListOf<Election>()

        dataList.add(Election(1, "VIP Test Election", Date(), Division("id1", "US", "Washington")))
        dataList.add(
            Election(
                2,
                "Wisconsin Presidential Primary Election",
                Date(),
                Division("id2", "US", "Wisconsin")
            )
        )
        dataList.add(
            Election(
                3,
                "Michigan Consolidated Election",
                Date(),
                Division("id3", "US", "Michigan")
            )
        )
        dataList.add(
            Election(
                4,
                "DC State Primary Election",
                Date(),
                Division("id4", "US", "DC Columbia")
            )
        )
        _upcomingElectionsList.value = dataList
    }*/

}