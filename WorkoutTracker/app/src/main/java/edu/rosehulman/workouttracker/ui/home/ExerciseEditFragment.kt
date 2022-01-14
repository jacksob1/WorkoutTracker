package edu.rosehulman.workouttracker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        setupListeners()
        updateView()
        return binding.root
    }

    fun setupListeners() {
        binding.doneButton.setOnClickListener {
            model.getCurrentExercise().name = binding.menu.editText!!.text.toString()
            model.getCurrentExercise().sets = binding.exerciseSetsValue.text.toString().toInt()
            model.getCurrentExercise().reps = binding.exerciseRepsValue.text.toString().toInt()
            model.getCurrentExercise().notes = binding.exerciseNotesValue.text.toString()
            findNavController().navigate(R.id.nav_home)
        }

        binding.cancelButton.setOnClickListener {
            model.removeCurrentQuote()
            findNavController().popBackStack()
        }
    }

    fun updateView() {
        binding.menu.editText!!.setText(model.getCurrentExercise().name)
        binding.exerciseSetsValue.setText(model.getCurrentExercise().sets.toString())
        binding.exerciseRepsValue.setText(model.getCurrentExercise().reps.toString())
        binding.exerciseNotesValue.setText(model.getCurrentExercise().notes)
    }
}