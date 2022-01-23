package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import java.util.*

class VoterInfoViewModel(/*private val dataSource: ElectionDao*/private val electionId: Int,
                                                                private val electionName: String
) : ViewModel() {

    private val TAG = "VoterInfoViewModel"

    // The internal MutableLiveData to store VoterInfoResponse
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()

    // The external immutable LiveData for the VoterInfoResponse
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    // The internal MutableLiveData to store boolean value that changes when voter info
    // displayed to the user. Used for progress bar visibility
    private val _voterInfoDisplayed = MutableLiveData<Boolean>()

    // The external immutable LiveData used for progress bar visibility via BindingsAdapters
    val voterInfoDisplayed: LiveData<Boolean>
        get() = _voterInfoDisplayed

    private val electionsRepository = ElectionsRepository()

    /**
     * init{} is called immediately after view model is created.
     */
    init {
        viewModelScope.launch {
            _voterInfoDisplayed.value = false
            try {
                _voterInfoResponse.value =
                    electionsRepository.getVoterInfo(electionId, electionName)
            } catch (e: Exception) {
                Log.d(TAG, e.printStackTrace().toString())
            }

        }
    }


    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */


    /************/
    /*fun loadVoterInfo(electionId: Int, division: Division) {

        // TODO: use electionId to get VoterInfo from API

//        _voterInfoResponse.value = repository.getVoterInfo

        val date = Date()
        val divisionObj = Division("division_id", "division_country", "division_state")
        val election =
            Election(electionId, "Wisconsin Presidential Primary Election", date, divisionObj)

        val stateList = mutableListOf<State>()
        val administrationBody = AdministrationBody(
            "administrationBody_name", "adminBody_electionInfoUrl",
            "adminBody_votingLocationFinderUrl",
            "adminBody_ballotInfoUrl", null)

        stateList.add(State("state_name", administrationBody))

        val voterInfoTmp = VoterInfoResponse(election, null, null, stateList, null)

        _voterInfoResponse.value = voterInfoTmp


    }*/
}