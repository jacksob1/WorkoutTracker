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

    fun addExercise(exercise: Exercise?) {
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

    fun reset() {
        exercises = ArrayList<Exercise>()
        currentPos = 0
    }

    fun removeCurrentExercise() = removeExerciseAt(currentPos)

    fun removeExerciseAt(pos: Int) {
        exercises.removeAt(pos)
        currentPos = 0
    }
}
