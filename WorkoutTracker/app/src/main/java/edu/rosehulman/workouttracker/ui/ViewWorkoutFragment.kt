package edu.rosehulman.workouttracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.workouttracker.ExerciseAdapter
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.ViewExerciseAdapter
import edu.rosehulman.workouttracker.databinding.FragmentExerciseListBinding
import edu.rosehulman.workouttracker.databinding.FragmentViewWorkoutBinding

class ViewWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentViewWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewWorkoutBinding.inflate(inflater, container, false)
        val adapter = ViewExerciseAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        return binding.root
    }
}