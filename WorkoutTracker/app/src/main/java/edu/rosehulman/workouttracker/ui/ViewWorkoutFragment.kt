package edu.rosehulman.workouttracker.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.workouttracker.*
import edu.rosehulman.workouttracker.databinding.FragmentExerciseListBinding
import edu.rosehulman.workouttracker.databinding.FragmentViewWorkoutBinding
import edu.rosehulman.workouttracker.ui.slideshow.WorkoutsViewModel

class ViewWorkoutFragment : Fragment() {
    private lateinit var workoutsViewModel: WorkoutsViewModel
    private lateinit var exerciseViewModel: ExerciseViewModel
    private lateinit var binding: FragmentViewWorkoutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workoutsViewModel = ViewModelProvider(requireActivity()).get(WorkoutsViewModel::class.java)
        exerciseViewModel = ViewModelProvider(requireActivity()).get(ExerciseViewModel::class.java)
        binding = FragmentViewWorkoutBinding.inflate(inflater, container, false)
        val adapter = ViewExerciseAdapter(this)
        adapter.addListener()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        setupButtons()

        return binding.root
    }

    fun setupButtons() {
        binding.shareButton.setOnClickListener {
            //TODO: Add share functionality after figuring out format to share the data in
        }

        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.nav_track_workout)
        }

        binding.templateButton.setOnClickListener {
            workoutsViewModel.addWorkout(null)
            val original = exerciseViewModel.exercises
            val newExercises = ArrayList<Exercise>()

            for(i in original.indices) {
                newExercises.add(original[i].copy(original[i].name, original[i].sets, original[i].reps))
            }
            exerciseViewModel.exercises = newExercises
            findNavController().navigate(R.id.nav_track_workout)
        }
    }
}