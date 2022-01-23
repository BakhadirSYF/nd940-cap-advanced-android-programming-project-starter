package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsRepository {

    suspend fun getElections(): List<Election> {
        val electionList = CivicsApi.retrofitService.getElections()
        return electionList.elections
    }

}