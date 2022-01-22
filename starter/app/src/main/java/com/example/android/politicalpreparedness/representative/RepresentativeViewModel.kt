package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Office
import com.example.android.politicalpreparedness.network.models.Official
import com.example.android.politicalpreparedness.representative.model.Representative
import java.util.*

class RepresentativeViewModel: ViewModel() {
    // The internal MutableLiveData that holds the data to be displayed on the UI
    private val _representativesList = MutableLiveData<List<Representative>>()

    // The external immutable LiveData
    val representativesList: LiveData<List<Representative>>
        get() = _representativesList

    // The internal MutableLiveData to store boolean value that changes when list of representatives
    // displayed to the user. Used for progress bar visibility
    private val _representativesDisplayed = MutableLiveData<Boolean>()

    // The external immutable LiveData used for progress bar visibility via BindingsAdapters
    val representativesDisplayed: LiveData<Boolean>
        get() = _representativesDisplayed

    /**
     * After representatives list displayed to the user, set [_representativesDisplayed] to true
     */
    fun displayRepresentativesListComplete() {
        _representativesDisplayed.value = true
    }

    fun searchRepresentatives() {
        val dataList = mutableListOf<Representative>()

        val official = Official(name = "Official Name", party = "Democratic Party")
        val division = Division("division_id", "division_country", "division_state")
        val officials = mutableListOf<Int>()
        officials.add(1)
        officials.add(2)
        val office = Office("Office Name", division, officials)
        val representative = Representative(official, office)
        dataList.add(representative)

        _representativesList.value = dataList
    }

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
