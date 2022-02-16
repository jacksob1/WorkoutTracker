package edu.rosehulman.workouttracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.workouttracker.ExerciseViewModel
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.databinding.FragmentExerciseFormBinding

class ExerciseEditFragment : Fragment() {
    private lateinit var model: ExerciseViewModel
    private lateinit var binding: FragmentExerciseFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(requireActivity())[ExerciseViewModel::class.java]
        binding = FragmentExerciseFormBinding.inflate(inflater, container, false)

        updateView()

        setupListeners()

        val items = resources.getStringArray(R.array.default_exercises)
        val adapter = ArrayAdapter(requireActivity(), R.layout.drop_down_item, items)
        binding.pickerTextView.setAdapter(adapter)
        return binding.root
    }

    fun setupListeners() {
        binding.doneButton.setOnClickListener {
            var exerciseName = binding.pickerTextView.text.toString()
            Log.d("WT", "Exercise: $exerciseName")
            var setsText = binding.exerciseSetsValue.text.toString()
            var repsText = binding.exerciseRepsValue.text.toString()
            exerciseName = if(exerciseName.isNotBlank()){exerciseName} else {model.getCurrentExercise().name}
            var sets = if(setsText.isNotBlank()){if(setsText.toInt() > 0){setsText.toInt()} else {0}} else {model.getCurrentExercise().sets}
            var reps = if(repsText.isNotBlank()){if(repsText.toInt() > 0){repsText.toInt()} else {0}} else {model.getCurrentExercise().reps}
            var notes = binding.exerciseNotesValue.text.toString()
            model.updateCurrentExercise(exerciseName, sets, reps, notes)
            findNavController().navigate(R.id.nav_track_workout)
        }

        binding.cancelButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun updateView() {
        var name = model.getCurrentExercise().name

        if(name != "Exercise" && name.isNotBlank()) {
            binding.pickerTextView.hint = model.getCurrentExercise().name
        }
        binding.exerciseSetsValue.hint = model.getCurrentExercise().sets.toString()
        binding.exerciseRepsValue.hint = model.getCurrentExercise().reps.toString()
        binding.exerciseNotesValue.setText(model.getCurrentExercise().notes)
    }
}