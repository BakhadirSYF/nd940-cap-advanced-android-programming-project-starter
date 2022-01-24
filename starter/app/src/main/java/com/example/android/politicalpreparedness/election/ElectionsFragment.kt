package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    companion object {
        const val TAG = "ElectionsFragment"
    }

    private lateinit var binding: FragmentElectionBinding

    private val dataSource: ElectionDao by lazy {
        ElectionDatabase.getInstance(requireNotNull(this.activity).application).electionDao
    }

    /**
     * Lazily initialize [ElectionsViewModel].
     */
    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(
            this,
            ElectionsViewModelFactory(dataSource)
        ).get(ElectionsViewModel::class.java)
    }

    /**
     * Upcoming Elections RecyclerView adapter.
     */
    private var upcomingElectionsAdapter: ElectionListAdapter? = null

    /**
     * Saved Elections RecyclerView adapter.
     */
    private var savedElectionsAdapter: ElectionListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.upcomingElectionsList.observe(
            viewLifecycleOwner,
            Observer<List<Election>> { elections ->
                elections?.apply {
                    upcomingElectionsAdapter?.elections = elections
                    viewModel.displayElectionListComplete()
                }
            })

        viewModel.savedElectionsList.observe(
            viewLifecycleOwner,
            Observer<List<Election>> { elections ->
                elections?.apply {
                    savedElectionsAdapter?.elections = elections
                    // TODO: might need extra boolean to separate upcoming and saved list loading
                    viewModel.displayElectionListComplete()
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
        upcomingElectionsAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.displayVoterInfo(it)
        })

        savedElectionsAdapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.displayVoterInfo(it)
        })

        binding.upcomingElectionsRecyclerView.adapter = upcomingElectionsAdapter
        binding.savedElectionsRecyclerView.adapter = savedElectionsAdapter

        // Observe the navigateToSelectedElection LiveData and navigate when it isn't null.
        // After navigating, call displayElectionDetailsComplete() so that the ViewModel is ready
        // for another navigation event.
        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(
                    ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                        it.id,
                        it.name
                    )
                )
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.displayVoterInfoComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSavedElections()
    }

//TODO: Refresh adapters when fragment loads

}