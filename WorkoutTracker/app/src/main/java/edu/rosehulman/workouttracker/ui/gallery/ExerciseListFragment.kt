package edu.rosehulman.workouttracker.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

        return binding.root
    }
}