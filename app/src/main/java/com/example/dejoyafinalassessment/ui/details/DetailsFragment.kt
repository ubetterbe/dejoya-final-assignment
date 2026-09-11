package com.example.dejoyafinalassessment.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dejoyafinalassessment.R
import com.example.dejoyafinalassessment.databinding.FragmentDetailsBinding
import com.google.android.material.color.MaterialColors

// no ViewModel here on purpose - this screen doesn't call the API or do any
// async work, it's just displaying an entity Dashboard already fetched and
// handed over via Bundle args. A ViewModel would just be an empty pass-through.
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate(actionId, bundle) sets this bundle as the destination's
        // arguments - that's how plain-Bundle nav args work without Safe Args
        val args = requireArguments()
        binding.textSportName.text = args.getString(ARG_SPORT_NAME)
        binding.textPlayerCount.text = args.getInt(ARG_PLAYER_COUNT).toString()
        binding.textFieldType.text = args.getString(ARG_FIELD_TYPE)
        binding.textDescription.text = args.getString(ARG_DESCRIPTION)

        // "Yes"/"No" reads a lot friendlier here than a raw true/false would.
        // Same gold badge as the Dashboard card - only lit up when it's actually true
        if (args.getBoolean(ARG_OLYMPIC_SPORT)) {
            binding.textOlympicSport.text = getString(R.string.details_olympic_yes)
            binding.textOlympicSport.setBackgroundResource(R.drawable.bg_olympic_badge)
            binding.textOlympicSport.setTextColor(
                MaterialColors.getColor(binding.textOlympicSport, com.google.android.material.R.attr.colorOnSecondary)
            )
        } else {
            binding.textOlympicSport.text = getString(R.string.details_olympic_no)
            binding.textOlympicSport.background = null
            binding.textOlympicSport.setTextColor(
                MaterialColors.getColor(binding.textOlympicSport, com.google.android.material.R.attr.colorOnPrimaryContainer)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // plain Bundle keys (no Safe Args) - DashboardFragment writes these,
        // this Fragment reads them
        const val ARG_SPORT_NAME = "arg_sport_name"
        const val ARG_PLAYER_COUNT = "arg_player_count"
        const val ARG_FIELD_TYPE = "arg_field_type"
        const val ARG_OLYMPIC_SPORT = "arg_olympic_sport"
        const val ARG_DESCRIPTION = "arg_description"
    }
}
