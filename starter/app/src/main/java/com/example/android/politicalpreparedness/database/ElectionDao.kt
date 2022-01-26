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

    /**
     * Selects and returns the row that matches the supplied id, which is our key.
     *
     * @param id: id to match
     */
    @Query("SELECT * FROM election_table WHERE id = :id")
    suspend fun get(id: Int): Election?

    /**
     * Deletes election data
     */
    @Delete
    suspend fun delete(election: Election?)
}