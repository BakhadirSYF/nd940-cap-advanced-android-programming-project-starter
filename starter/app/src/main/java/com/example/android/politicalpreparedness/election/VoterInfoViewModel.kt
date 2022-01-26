package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import com.example.android.politicalpreparedness.utils.ProgressState
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val dataSource: ElectionDao, private val electionId: Int,
    private val electionName: String
) : ViewModel() {

    companion object {
        const val TAG = "VoterInfoViewModel"
    }

    private var election: Election? = null

    // The internal MutableLiveData to store VoterInfoResponse
    private val _voterInfoResponse = MutableLiveData<VoterInfoResponse>()

    // The external immutable LiveData for the VoterInfoResponse
    val voterInfoResponse: LiveData<VoterInfoResponse>
        get() = _voterInfoResponse

    // The internal MutableLiveData to store boolean value that represents election data saved state.
    private val _savedState = MutableLiveData<Boolean>()

    // The external immutable LiveData
    val savedState: LiveData<Boolean>
        get() = _savedState

    // The internal MutableLiveData to store state value that changes when API call is complete.
    // Used for progress bar and recycler view visibility
    private val _voterInfoLoadingState = MutableLiveData<ProgressState>()

    // The external immutable LiveData
    val voterInfoLoadingState: LiveData<ProgressState>
        get() = _voterInfoLoadingState

    private val electionsRepository = ElectionsRepository(dataSource)

    /**
     * init{} is called immediately after view model is created.
     */
    init {
        _voterInfoLoadingState.value = ProgressState.INITIAL
        getVoterInfoFromApi()
        updateButtonState()
    }

    private fun getVoterInfoFromApi() {
        viewModelScope.launch {
            _voterInfoLoadingState.value = ProgressState.LOADING_ACTIVE
            try {

                _voterInfoLoadingState.value = ProgressState.LOADING_SUCCESS
                val voterInfoResponse = electionsRepository.getVoterInfo(electionId, electionName)
                election = voterInfoResponse.election

                _voterInfoResponse.value = voterInfoResponse
            } catch (e: Exception) {
                _voterInfoLoadingState.value = ProgressState.LOADING_FAILURE
                Log.d(TAG, e.printStackTrace().toString())
            }
        }
    }

    private fun updateButtonState() {
        viewModelScope.launch {
            val election = electionsRepository.get(electionId)
            Log.d(TAG, "electionId = ${election?.id}")

            _savedState.value = election?.id == electionId
        }
    }

    fun updateElectionDataInDatabase() {
        viewModelScope.launch {

            when (_savedState.value) {
                true -> {
                    electionsRepository.remove(election)
                    _savedState.value = false
                }
                false -> {
                    val insertId = electionsRepository.save(election)
                    if (insertId?.toInt() == election?.id) {
                        _savedState.value = true
                    }
                }
            }
        }
    }
}