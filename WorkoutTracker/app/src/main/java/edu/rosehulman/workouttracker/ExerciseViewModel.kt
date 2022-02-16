package edu.rosehulman.workouttracker

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class ExerciseViewModel: ViewModel() {
    var workoutId: String? = null
    var exerciseChoices: ArrayList<ExerciseName> = ArrayList<ExerciseName>()
    lateinit var context: Context
    lateinit var ref: CollectionReference
    var exercises = ArrayList<Exercise>()
    var currentPos = 0

    fun updateCurrentPos(pos: Int) {
        currentPos = pos
    }

    fun getExerciseAt(pos: Int) = exercises[pos]
    fun getCurrentExercise() = getExerciseAt(currentPos)
    fun size() = exercises.size

    fun setContextForExercises(newContext: Context) {
        context = newContext
    }

    fun addExercise(exercise: Exercise?) {
        val defaultExercise = Exercise()

        val newExercise = exercise ?: defaultExercise
        ref.add(newExercise)
    }

    fun getWorkoutName(name: String): String? {
        return when(name) {
            "Arnold Press" -> WorkoutExercises.ARNOLD_PRESS
            "Back Extension" -> WorkoutExercises.BACK_EXTENSION
            "Bench Press" -> WorkoutExercises.BENCH_PRESS
            "Bicep Curls" -> WorkoutExercises.BICEP_CURL
            "Burpees" -> WorkoutExercises.BURPEE
            "Calf Press" -> WorkoutExercises.CALF_PRESS
            "Calf Raises" -> WorkoutExercises.CALF_RAISE
            "Chest Dips" -> WorkoutExercises.CHEST_DIP
            "Chin Ups" -> WorkoutExercises.CHINUP
            "Clean" -> WorkoutExercises.CLEAN
            "Clean & Jerk" -> WorkoutExercises.CLEAN_JERK
            "Close Grip Bench Press" -> WorkoutExercises.CLOSE_GRIP_BENCH_PRESS
            "Close Grip Push Ups" -> WorkoutExercises.CLOSE_GRIP_PUSHUP
            "Crunches" -> WorkoutExercises.CRUNCH
            "Deadlift" -> WorkoutExercises.DEADLIFT
            "Decline Bench Press" -> WorkoutExercises.DECLINE_BENCH_PRESS
            "Dips" -> WorkoutExercises.DIP
            "Flies" -> WorkoutExercises.FLY
            "Front Raise" -> WorkoutExercises.FRONT_RAISE
            "Good Mornings" -> WorkoutExercises.GOOD_MORNING
            "Hang Clean" -> WorkoutExercises.HANG_CLEAN
            "Hang Power Clean" -> WorkoutExercises.HANG_POWER_CLEAN
            "High Row" -> WorkoutExercises.HIGH_ROW
            "Hip Thrust" -> WorkoutExercises.HIP_THRUST
            "JM Press" -> WorkoutExercises.JM_PRESS
            "Jumping Jacks" -> WorkoutExercises.JUMPING_JACK
            "Lateral Raise" -> WorkoutExercises.LATERAL_RAISE
            "Leg Curls" -> WorkoutExercises.LEG_CURL
            "Leg Extensions" -> WorkoutExercises.LEG_EXTENSION
            "Leg Press" -> WorkoutExercises.LEG_PRESS
            "Leg Raises" -> WorkoutExercises.LEG_RAISE
            "Lunges" -> WorkoutExercises.LUNGE
            "Military Press" -> WorkoutExercises.MILITARY_PRESS
            "Pike Press" -> WorkoutExercises.PIKE_PRESS
            "Pike Push Ups" -> WorkoutExercises.PIKE_PUSHUP
            "Plank" -> WorkoutExercises.PLANK
            "Forearm Plank" -> WorkoutExercises.PLANK
            "Power Clean" -> WorkoutExercises.POWER_CLEAN
            "Pull Ups" -> WorkoutExercises.PULLUP
            "Lat Pulldown" -> WorkoutExercises.PULLDOWN
            "RDLs" -> WorkoutExercises.RDL_DEADLIFT
            "Reverse Lunge" -> WorkoutExercises.REAR_LUNGE
            "Rows" -> WorkoutExercises.ROW
            "Russian Twists" -> WorkoutExercises.RUSSIAN_TWIST
            "Seated Calf Raises" -> WorkoutExercises.SEATED_CALF_RAISE
            "Shoulder Press" -> WorkoutExercises.SHOULDER_PRESS
            "Shoulder Shrugs" -> WorkoutExercises.SHRUG
            "Side Lunge" -> WorkoutExercises.SIDE_LUNGE
            "Side Plank" -> WorkoutExercises.SIDE_PLANK
            "Single Leg Deadlift" -> WorkoutExercises.SINGLE_LEG_DEADLIFT
            "Single Leg Hip Thrust" -> WorkoutExercises.SINGLE_LEG_HIP_BRIDGE
            "Sit Ups" -> WorkoutExercises.SITUP
            "Squats" -> WorkoutExercises.SQUAT
            "Standing Calf Raises" -> WorkoutExercises.STANDING_CALF_RAISE
            "Step Ups" -> WorkoutExercises.STEP_UP
            "Straight Leg Deadlift" -> WorkoutExercises.STRAIGHT_LEG_DEADLIFT
            "Thrusters" -> WorkoutExercises.THRUSTER
            "Tricep Dips" -> WorkoutExercises.TRICEPS_DIP
            "Tricep Extensions" -> WorkoutExercises.TRICEPS_EXTENSION
            "Twisting Crunches" -> WorkoutExercises.TWISTING_CRUNCH
            "Twisting Sit Ups" -> WorkoutExercises.TWISTING_SITUP
            "Upright Rows" -> WorkoutExercises.UPRIGHT_ROW
            "V-Sit Ups" -> WorkoutExercises.V_UPS
            "Wall Sit" -> WorkoutExercises.WALL_SIT
            else -> null
        }
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

        val googleName = getWorkoutName(name)
        if(!googleName.isNullOrEmpty()) {
            val dataSource = DataSource.Builder()
                .setAppPackageName(context)
                .setDataType(DataType.TYPE_WORKOUT_EXERCISE)
                .setStreamName("WT - update exercise")
                .setType(DataSource.TYPE_RAW)
                .build()

            val dataPoint = DataPoint.builder(dataSource)
                .setField(Field.FIELD_EXERCISE, googleName)
                .setField(Field.FIELD_REPETITIONS, reps)
                .setTimestamp(LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond(), TimeUnit.SECONDS)
                .build()

            var dataSet = DataSet.builder(dataSource).add(dataPoint).build()

            var fitnessOptions = FitnessOptions.builder().addDataType(DataType.TYPE_WORKOUT_EXERCISE).build()
            var googleSignInAccount = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

            Fitness.getHistoryClient(context, googleSignInAccount)
                .insertData(dataSet)
                .addOnSuccessListener {
                    Log.d("WT", "Successfully added new exercise entry")
                }
                .addOnFailureListener {
                    Log.d("WT", "Failed to add exercise to history. Exception: $it")
                }
        }

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
