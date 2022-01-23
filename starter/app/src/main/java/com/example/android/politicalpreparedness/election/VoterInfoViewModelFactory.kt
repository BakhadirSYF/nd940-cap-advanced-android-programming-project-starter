package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VoterInfoViewModelFactory(private val electionId: Int, private val electionName: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(/*dataDao*/electionId, electionName) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}
