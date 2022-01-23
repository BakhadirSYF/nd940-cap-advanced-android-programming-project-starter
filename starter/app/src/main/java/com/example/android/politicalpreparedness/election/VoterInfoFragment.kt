package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    companion object {
        const val TAG = "VoterInfoFragment"
    }

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var dataDao: ElectionDao

    val args: VoterInfoFragmentArgs by navArgs()

    /**
     * Lazily initialize [VoterInfoViewModel].
     */
    private val viewModel: VoterInfoViewModel by lazy {
        ViewModelProvider(
            this,
            VoterInfoViewModelFactory(/*dataDao*/args.argElectionId, args.argElectionName)
        ).get(VoterInfoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the ElectionsViewModel
        binding.viewModel = viewModel

        return binding.root

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */


        //TODO: Handle loading of URLs

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks

    }

    override fun onResume() {
        super.onResume()
    }

    //TODO: Create method to load URL intents

}