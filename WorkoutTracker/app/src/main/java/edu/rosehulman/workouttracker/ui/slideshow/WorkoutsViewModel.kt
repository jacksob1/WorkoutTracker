package edu.rosehulman.workouttracker.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.rosehulman.workouttracker.Exercise

class WorkoutsViewModel : ViewModel() {
    var workouts = ArrayList<ArrayList<Exercise>>()
    var currentWorkout = 0
}