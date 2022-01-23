package com.example.android.politicalpreparedness.repository

import android.util.Log
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionsRepository(private val database: ElectionDao) {

    companion object {
        const val TAG = "ElectionsRepository"
    }

    suspend fun getElections(): List<Election> {
        val electionList = CivicsApi.retrofitService.getElections()
        return electionList.elections
    }

    suspend fun getVoterInfo(electionId: Int, electionName: String): VoterInfoResponse {
        return CivicsApi.retrofitService.getVoterInfo(
            mapOf(
                "electionId" to electionId.toString(),
                "address" to electionName
            )
        )
    }

    suspend fun saveElection(election: Election?): Long {
        val insertId = database.insert(election)
        Log.d(TAG, "insertLong = $insertId")
        return insertId
    }

}