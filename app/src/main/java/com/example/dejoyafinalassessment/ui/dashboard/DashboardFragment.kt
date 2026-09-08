package com.example.dejoyafinalassessment.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dejoyafinalassessment.MainActivity
import com.example.dejoyafinalassessment.databinding.FragmentDashboardBinding

// placeholder for now - just proves the nav graph/fragment structure works and
// the keypass makes it through from login. Real RecyclerView of sports entities
// (calling GET /dashboard/{keypass}) is the next step.
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

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

        // a Fragment doesn't get its own Intent - it's the hosting Activity that
        // was actually launched with one, so we go up through requireActivity()
        // to read the extra LoginActivity attached to the MainActivity Intent
        val keypass = requireActivity().intent.getStringExtra(MainActivity.EXTRA_KEYPASS)
        binding.textKeypass.text = "keypass: $keypass"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // fragment views can outlive the Fragment itself briefly - null this out so
        // we're not holding a reference to a view that's already been torn down
        _binding = null
    }
}
