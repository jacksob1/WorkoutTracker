package edu.rosehulman.workouttracker.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.workouttracker.ExerciseAdapter
import edu.rosehulman.workouttracker.ExerciseViewModel
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.databinding.FragmentExerciseListBinding

class ExerciseListFragment : Fragment() {
    private lateinit var binding: FragmentExerciseListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseListBinding.inflate(inflater, container, false)
        val adapter = ExerciseAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        binding.fab.setOnClickListener {
            adapter.addExercise(null)
            findNavController().navigate(R.id.nav_exercise_form)
        }

        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.nav_save_workout)
        }
        return binding.root
    }
}