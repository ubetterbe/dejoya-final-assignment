package com.example.dejoyafinalassessment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dejoyafinalassessment.databinding.FragmentDetailsBinding

// bare minimum stub so the nav graph has somewhere valid to point to - built
// out properly (showing the tapped entity's full details) in a later step
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // plain Bundle keys (no Safe Args) that Dashboard writes to and this
        // Fragment will read from once it's built out for real
        const val ARG_SPORT_NAME = "arg_sport_name"
        const val ARG_PLAYER_COUNT = "arg_player_count"
        const val ARG_FIELD_TYPE = "arg_field_type"
        const val ARG_OLYMPIC_SPORT = "arg_olympic_sport"
        const val ARG_DESCRIPTION = "arg_description"
    }
}
