package edu.rosehulman.workouttracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.databinding.FragmentSaveWorkoutBinding

class SaveWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentSaveWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveWorkoutBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.doneButton.setOnClickListener {
            //TODO: Add in saving workout functionality
            findNavController().navigate(R.id.nav_home)
        }
        return binding.root
    }
}