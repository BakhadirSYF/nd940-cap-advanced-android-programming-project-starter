package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    /**
     * Inserts election data
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(election: Election?): Long

    /**
     * Selects and returns all saved elections.
     * Sorted by date
     */
    @Query("SELECT * FROM election_table ORDER BY electionDay ASC")
    suspend fun getElections(): List<Election>?

    //TODO: Add select single election query

    //TODO: Add delete query

    //TODO: Add clear query

}