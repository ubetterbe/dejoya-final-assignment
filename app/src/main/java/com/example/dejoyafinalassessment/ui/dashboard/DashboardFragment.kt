package com.example.dejoyafinalassessment.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dejoyafinalassessment.MainActivity
import com.example.dejoyafinalassessment.R
import com.example.dejoyafinalassessment.data.model.Entity
import com.example.dejoyafinalassessment.databinding.FragmentDashboardBinding
import com.example.dejoyafinalassessment.ui.details.DetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModel()
    private val adapter = EntityAdapter { entity -> onEntityClicked(entity) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerEntities.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEntities.adapter = adapter

        // observing here (not in onCreate) because the views we're updating
        // don't exist until the binding is inflated - viewLifecycleOwner (not
        // `this`) so the observer dies with the view, not the whole Fragment
        viewModel.uiState.observe(viewLifecycleOwner) { state -> render(state) }

        // only kick off the load the first time - the ViewModel survives view
        // recreation (e.g. rotation) and already has the last state, so re-firing
        // the network call here every time onViewCreated runs would be wasteful
        if (viewModel.uiState.value !is DashboardUiState.Success) {
            val keypass = requireActivity().intent.getStringExtra(MainActivity.EXTRA_KEYPASS).orEmpty()
            viewModel.loadDashboard(keypass)
        }
    }

    private fun render(state: DashboardUiState) {
        binding.progressDashboard.visibility = if (state is DashboardUiState.Loading) View.VISIBLE else View.GONE

        when (state) {
            is DashboardUiState.Success -> {
                binding.textDashboardError.visibility = View.GONE
                binding.recyclerEntities.visibility = View.VISIBLE
                adapter.submitList(state.entities)
            }

            is DashboardUiState.Error -> {
                binding.recyclerEntities.visibility = View.GONE
                binding.textDashboardError.text = state.message
                binding.textDashboardError.visibility = View.VISIBLE
            }

            DashboardUiState.Loading, DashboardUiState.Idle -> {
                binding.textDashboardError.visibility = View.GONE
            }
        }
    }

    private fun onEntityClicked(entity: Entity) {
        // plain Bundle args, not Safe Args - keeping this consistent with how
        // the nav graph was set up in the last step
        val args = Bundle().apply {
            putString(DetailsFragment.ARG_SPORT_NAME, entity.sportName)
            putInt(DetailsFragment.ARG_PLAYER_COUNT, entity.playerCount)
            putString(DetailsFragment.ARG_FIELD_TYPE, entity.fieldType)
            putBoolean(DetailsFragment.ARG_OLYMPIC_SPORT, entity.olympicSport)
            putString(DetailsFragment.ARG_DESCRIPTION, entity.description)
        }
        findNavController().navigate(R.id.action_dashboardFragment_to_detailsFragment, args)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // drop the adapter reference too, not just the binding - RecyclerView
        // hangs on to it otherwise
        binding.recyclerEntities.adapter = null
        _binding = null
    }
}
