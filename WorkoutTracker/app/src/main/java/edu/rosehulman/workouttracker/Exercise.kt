package edu.rosehulman.workouttracker

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp

data class Exercise(var name: String = "Exercise", var sets: Int = 0, var reps: Int = 0, var notes: String = "") {
    @get:Exclude
    var id = ""

    @ServerTimestamp
    var created: Timestamp? = null

    override fun toString(): String {
        return "$name, $sets, $reps, $notes,\n"
    }

    companion object {
        fun from(snapshot: DocumentSnapshot): Exercise {
            var exercise = snapshot.toObject(Exercise::class.java)!!
            exercise.id = snapshot.id
            return exercise
        }
    }

}