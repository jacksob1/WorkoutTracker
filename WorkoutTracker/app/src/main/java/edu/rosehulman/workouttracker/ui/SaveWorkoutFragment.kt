package edu.rosehulman.workouttracker.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.workouttracker.Exercise
import edu.rosehulman.workouttracker.ExerciseViewModel
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.Workout
import edu.rosehulman.workouttracker.databinding.FragmentSaveWorkoutBinding
import edu.rosehulman.workouttracker.ui.slideshow.WorkoutsViewModel
import kotlin.random.Random

class SaveWorkoutFragment : Fragment() {
    private lateinit var binding: FragmentSaveWorkoutBinding
    private lateinit var exerciseModel: ExerciseViewModel
    private lateinit var workoutsViewModel: WorkoutsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exerciseModel = ViewModelProvider(requireActivity()).get(ExerciseViewModel::class.java)
        workoutsViewModel = ViewModelProvider(requireActivity()).get(WorkoutsViewModel::class.java)
        binding = FragmentSaveWorkoutBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.doneButton.setOnClickListener {
            //TODO: Add in saving workout functionality
            Log.d("WT", "Clicked done")
            var name = binding.workoutName.text.toString()

            name = if(name.isNotBlank()) {name} else {workoutsViewModel.getCurrentWorkout().name}
            var exercises = exerciseModel.exercises
            workoutsViewModel.updateCurrentWorkout(name, exercises)

            exerciseModel.exercises = ArrayList<Exercise>()
            findNavController().navigate(R.id.nav_home)
        }

        binding.workoutName.hint = workoutsViewModel.getCurrentWorkout().name
        return binding.root
    }
}