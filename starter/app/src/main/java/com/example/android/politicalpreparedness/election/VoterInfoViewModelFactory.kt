package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao

class VoterInfoViewModelFactory(private val dataSource: ElectionDao, private val electionId: Int, private val electionName: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(dataSource, electionId, electionName) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }

}
