package edu.rosehulman.workouttracker

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExerciseViewModel: ViewModel() {
    var workoutId: String? = null
    var exerciseChoices: ArrayList<ExerciseName> = ArrayList<ExerciseName>()
    lateinit var ref: CollectionReference
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
        ref.add(newExercise)
    }



    fun updateCurrentExercise(name: String, sets: Int, reps: Int, notes: String) {
        exercises[currentPos].name = name
        var exerciseName = ExerciseName(name)
        if(!exerciseChoices.contains(exerciseName)) {
            var nameRef = Firebase.firestore.collection("exerciseNames")
            nameRef.add(exerciseName).addOnSuccessListener {
                getExerciseNames()
            }
        }
        exercises[currentPos].sets = sets
        exercises[currentPos].reps = reps
        exercises[currentPos].notes = notes
        ref.document(getCurrentExercise().id).set(getCurrentExercise())
    }

    fun setWorkout(id: String) {
        workoutId = id
        var uid = Firebase.auth.uid!!
        ref = Firebase.firestore.collection("users").document(uid).collection("workouts").document(workoutId!!).collection("exercises")
    }

    fun setExerciseList(newList: ArrayList<Exercise>) {
        exercises.clear()
        newList.forEach {
            addExercise(it)
        }
    }

    fun reset() {
        exercises = ArrayList<Exercise>()
        currentPos = 0
    }

    fun removeExerciseAt(pos: Int) {
        var exercise = exercises.removeAt(pos)
        currentPos = 0
        ref.document(exercise.id).delete()
    }

    fun getExerciseNames() {
        var nameRef = Firebase.firestore.collection("exerciseNames")

        nameRef.get().addOnSuccessListener { snapshot: QuerySnapshot ->
            exerciseChoices.clear()
            snapshot.documents.forEach {
                exerciseChoices.add(it.toObject(ExerciseName::class.java)!!)
            }
        }
    }

    fun getChoices(): ArrayList<String> {
        var choices = ArrayList<String>()
        exerciseChoices.forEach {
            choices.add(it.name)
        }
        return choices
    }

    fun addListener(observer: () -> Unit) {
        var uid = Firebase.auth.uid!!
        ref = Firebase.firestore.collection("users").document(uid).collection("workouts").document(workoutId!!).collection("exercises")
        ref
            .orderBy("created", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, error ->
                error?.let {
                    Log.d("WT", "Error: $it")
                    return@addSnapshotListener
                }

                clear()
                snapshot?.documents?.forEach {
                    exercises.add(Exercise.from(it))
                }
                observer()
            }
    }

    fun clear() = exercises.clear()
}
