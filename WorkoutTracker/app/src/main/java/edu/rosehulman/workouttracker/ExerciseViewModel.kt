package edu.rosehulman.workouttracker

import androidx.lifecycle.ViewModel

class ExerciseViewModel: ViewModel() {
    var exercises = ArrayList<Exercise>()
    var currentPos = 0

    fun updateCurrentPos(pos: Int) {
        currentPos = pos
    }

    fun getExerciseAt(pos: Int) = exercises[pos]
    fun getCurrentExercise() = getExerciseAt(currentPos)
    fun size() = exercises.size

    fun addQuote(exercise: Exercise?) {
        val defaultExercise = Exercise()

        val newExercise = exercise ?: defaultExercise
        exercises.add(newExercise)
    }

    fun updateCurrentExercise(name: String, sets: Int, reps: Int, notes: String) {
        exercises[currentPos].name = name
        exercises[currentPos].sets = sets
        exercises[currentPos].reps = reps
        exercises[currentPos].notes = notes
    }

    fun removeCurrentQuote() {
        exercises.removeAt(currentPos)
        currentPos = 0
    }
}
