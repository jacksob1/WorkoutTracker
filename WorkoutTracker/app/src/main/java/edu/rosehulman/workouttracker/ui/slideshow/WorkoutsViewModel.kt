package edu.rosehulman.workouttracker.ui.slideshow

import android.util.Log
import androidx.lifecycle.ViewModel
import edu.rosehulman.workouttracker.Exercise
import edu.rosehulman.workouttracker.Workout

class WorkoutsViewModel : ViewModel() {
    var workouts = ArrayList<Workout>()
    var currentPos = 0
    var updateAdapter: () -> Unit = {}

    fun updateCurrentPos(pos: Int) {
        currentPos = pos
    }

    fun getWorkoutAt(pos: Int) = workouts[pos]
    fun getCurrentWorkout() = getWorkoutAt(currentPos)
    fun size() = workouts.size

    fun addWorkout(workout: Workout?, observer: () -> Unit) {
        var newWorkout = workout?: Workout()
        if(size() > 0) {
            currentPos++
        }
        workouts.add(newWorkout)
        updateAdapter = observer
    }

    fun updateCurrentWorkout(name: String, exercises: ArrayList<Exercise>) {
        workouts[currentPos].name = name
        workouts[currentPos].exercises = exercises
        Log.d("WT", "Updating workout after save")
        updateAdapter()
    }
}