package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    companion object {
        const val TAG = "ElectionsFragment"
    }

    private lateinit var binding: FragmentElectionBinding

    /**
     * Lazily initialize [ElectionsViewModel].
     */
    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(
            this,
            ElectionsViewModelFactory()
        ).get(ElectionsViewModel::class.java)
    }


    /**
     * Upcoming Elections RecyclerView adapter.
     */
    private var viewModelAdapter: ElectionListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.electionsList.observe(viewLifecycleOwner, Observer<List<Election>> { elections ->
            elections?.apply {
                viewModelAdapter?.elections = elections
                viewModel.displayAsteroidListComplete()
            }
        })
    }

    /**
     * Inflate the layout with Data Binding, set its lifecycle owner to the ElectionsFragment
     * to enable Data Binding to observe LiveData, and set up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentElectionBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the ElectionsViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the electionsList RecyclerView with clickHandler lambda that
        // tells the viewModel when list item is clicked
        viewModelAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.displayElectionDetails(it)
        })

        binding.upcomingElectionsRecyclerView.adapter = viewModelAdapter

        setHasOptionsMenu(true)
        return binding.root

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

    }

    //TODO: Refresh adapters when fragment loads
    override fun onResume() {
        super.onResume()
        // Load the reminders list on the ui
        viewModel.loadUpcomingElections()
    }
}