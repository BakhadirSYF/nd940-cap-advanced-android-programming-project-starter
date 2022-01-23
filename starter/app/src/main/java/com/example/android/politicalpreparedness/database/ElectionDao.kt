package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert
    suspend fun insert(election: Election?)

    //TODO: Add select all election query

    //TODO: Add select single election query

    //TODO: Add delete query

    //TODO: Add clear query

}