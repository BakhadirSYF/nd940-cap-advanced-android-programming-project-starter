package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.launch
import java.util.*

class VoterInfoViewModel(/*private val dataSource: ElectionDao*/) : ViewModel() {

    private val TAG = "VoterInfoViewModel"

    // The internal MutableLiveData to store VoterInfoResponse
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()

    // The external immutable LiveData for the VoterInfoResponse
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    fun loadVoterInfo(electionId: Int, division: Division) {

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


    }

    //TODO: Add live data to hold voter info

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}