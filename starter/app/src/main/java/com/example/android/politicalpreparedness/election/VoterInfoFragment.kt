package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    companion object {
        const val TAG = "VoterInfoFragment"
    }

    private lateinit var binding: FragmentVoterInfoBinding

    private val args: VoterInfoFragmentArgs by navArgs()

    private val dataSource: ElectionDao by lazy {
        ElectionDatabase.getInstance(requireNotNull(this.activity).application).electionDao
    }

    /**
     * Lazily initialize [VoterInfoViewModel].
     */
    private val viewModel: VoterInfoViewModel by lazy {
        ViewModelProvider(
            this,
            VoterInfoViewModelFactory(dataSource, args.argElectionId, args.argElectionName)
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

        binding.followElectionButton.setOnClickListener {
            viewModel.updateElectionDataInDatabase()
        }

        viewModel.voterInfoResponse.observe(viewLifecycleOwner, Observer {
            val isAddressAvailable =
                !TextUtils.isEmpty(it.state?.get(0)?.electionAdministrationBody?.correspondenceAddress?.toFormattedString())
            binding.stateCorrespondenceHeader.visibility =
                if (isAddressAvailable) View.VISIBLE else View.GONE
            binding.address.visibility = if (isAddressAvailable) View.VISIBLE else View.GONE
        })

        return binding.root
    }
}