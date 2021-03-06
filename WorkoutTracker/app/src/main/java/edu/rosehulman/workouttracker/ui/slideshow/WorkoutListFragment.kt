package edu.rosehulman.workouttracker.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.rosehulman.workouttracker.R
import edu.rosehulman.workouttracker.WorkoutAdapter
import edu.rosehulman.workouttracker.databinding.FragmentWorkoutListBinding

class WorkoutListFragment : Fragment() {
    private lateinit var binding: FragmentWorkoutListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWorkoutListBinding.inflate(inflater, container, false)
        var adapter = WorkoutAdapter(this)
        adapter.addListener()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.setHasFixedSize(true)

        binding.fab.setOnClickListener {
            adapter.addWorkout(null)
        }
        return binding.root
    }
}