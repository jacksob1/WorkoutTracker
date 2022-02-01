package edu.rosehulman.workouttracker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.workouttracker.ui.slideshow.WorkoutListFragment
import edu.rosehulman.workouttracker.ui.slideshow.WorkoutsViewModel

class WorkoutAdapter(val fragment: WorkoutListFragment): RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(WorkoutsViewModel::class.java)
    val exerciseViewModel = ViewModelProvider(fragment.requireActivity()).get(ExerciseViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutAdapter.WorkoutViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.workout_card, parent, false)
        return WorkoutViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WorkoutAdapter.WorkoutViewHolder, position: Int) {
        model.updateCurrentPos(position)
        holder.bind(model.getWorkoutAt(position))
    }

    override fun getItemCount() = model.size()

    fun addListener() {
        model.addListener {
            notifyDataSetChanged()
        }
    }
    fun addWorkout(workout: Workout?) {
        exerciseViewModel.reset()
        model.addWorkout(workout) {
            Log.d("WT", "Notifying data set changed: ${model.size()}")
            notifyDataSetChanged()
        }
    }

    inner class WorkoutViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val workoutNameView: TextView = itemView.findViewById(R.id.workout_name_text_view)
        val workoutDateView: TextView = itemView.findViewById(R.id.workout_date_text_view)

        init {
            itemView.setOnClickListener {
                model.updateCurrentPos(adapterPosition)
                exerciseViewModel.workoutId = model.getCurrentWorkout().id
                exerciseViewModel.exercises = model.getCurrentWorkout().exercises
                exerciseViewModel.updateCurrentPos(0)
                itemView.findNavController().navigate(R.id.nav_view_workout)
            }
        }

        fun bind(workout: Workout) {
            workoutNameView.text = workout.name
            workoutDateView.text = workout.created?.toDate().toString()
        }
    }

}