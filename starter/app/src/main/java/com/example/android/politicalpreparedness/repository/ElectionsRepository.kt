package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

class ElectionsRepository(private val database: ElectionDao) {

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

    suspend fun saveElection(election: Election?) {
        database.insert(election)
    }

}