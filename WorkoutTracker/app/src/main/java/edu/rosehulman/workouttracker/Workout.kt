package edu.rosehulman.workouttracker

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Workout(var name: String = "Workout", var exercises: ArrayList<Exercise> = ArrayList<Exercise>()) {
    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    override fun toString(): String {
        var workoutString = "Exercise Name, Sets, Reps, Notes,\n"
        exercises.forEach {
            workoutString += it.toString()
        }
        return workoutString
    }
    companion object {
        fun from(snapshot: DocumentSnapshot): Workout {
            var workout = snapshot.toObject(Workout::class.java)!!
            workout.id = snapshot.id
            return workout
        }
    }
}