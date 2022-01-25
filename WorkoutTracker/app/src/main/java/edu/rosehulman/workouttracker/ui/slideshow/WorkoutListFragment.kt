package edu.rosehulman.workouttracker.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.databinding.FragmentWorkoutListBinding

class WorkoutListFragment : Fragment() {

    private lateinit var workoutsViewModel: WorkoutsViewModel
    private lateinit var binding: FragmentWorkoutListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workoutsViewModel = ViewModelProvider(this).get(WorkoutsViewModel::class.java)

        binding = FragmentWorkoutListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.nav_track_workout)
        }
        return binding.root
    }
}