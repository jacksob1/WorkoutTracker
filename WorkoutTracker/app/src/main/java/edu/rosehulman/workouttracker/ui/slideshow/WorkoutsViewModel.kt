package edu.rosehulman.workouttracker.ui.slideshow

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.workouttracker.Exercise
import edu.rosehulman.workouttracker.Workout

class WorkoutsViewModel : ViewModel() {
    var workouts = ArrayList<Workout>()
    var currentPos = 0
    var updateAdapter: () -> Unit = {}
    lateinit var ref: CollectionReference

    fun updateCurrentPos(pos: Int) {
        currentPos = pos
    }

    fun getWorkoutAt(pos: Int) = workouts[pos]
    fun getCurrentWorkout() = getWorkoutAt(currentPos)
    fun size() = workouts.size

    fun addWorkoutThroughAdapter(workout: Workout?, observer: () -> Unit) {
        var newWorkout = workout?: Workout()
        if(size() > 0) {
            updateCurrentPos(size())
        }
        ref.add(newWorkout)
        updateAdapter = observer
    }

    fun addWorkout(workout: Workout?, observer: (id: String) -> Unit) {
        var newWorkout = workout?: Workout()
        if(size() > 0) {
            updateCurrentPos(size())
        }
        ref.add(newWorkout).addOnSuccessListener {
            observer(it.id)
        }
    }

    fun addListener(observer: () -> Unit) {
        var uid = Firebase.auth.uid!!
        ref = Firebase.firestore.collection("users").document(uid).collection("workouts")

        ref
            .orderBy("created", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, error ->
                error?.let {
                    Log.d("WT", "Error: $it")
                    return@addSnapshotListener
                }

                clear()
                snapshot?.documents?.forEach {
                    workouts.add(Workout.from(it))
                }
                observer()
            }
    }

    fun clear() = workouts.clear()

    fun updateCurrentWorkout(name: String, exercises: ArrayList<Exercise>) {
        workouts[currentPos].name = name
        workouts[currentPos].exercises = exercises
        Log.d("WT", "Updating workout after save")
        updateAdapter()
        ref.document(getCurrentWorkout().id).update("name", name)
        exercises.forEach { exercise: Exercise ->
            ref.document(getCurrentWorkout().id).collection("exercises").document(exercise.id).set(exercise)
        }
    }
}