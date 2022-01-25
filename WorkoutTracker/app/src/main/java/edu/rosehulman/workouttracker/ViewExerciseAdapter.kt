package edu.rosehulman.workouttracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.workouttracker.ui.ViewWorkoutFragment

class ViewExerciseAdapter(val fragment: ViewWorkoutFragment): RecyclerView.Adapter<ViewExerciseAdapter.ViewExerciseViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(ExerciseViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewExerciseAdapter.ViewExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_card, parent, false)
        return ViewExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewExerciseAdapter.ViewExerciseViewHolder, position: Int) {
        model.updateCurrentPos(position)
        holder.bind(model.getExerciseAt(position))
    }

    override fun getItemCount() = model.size()
    fun addExercise(exercise: Exercise?) {
        model.addExercise(exercise)
    }

    inner class ViewExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exercise_name_text_view)
        val setsValue: TextView = itemView.findViewById(R.id.exercise_sets_value_text_view)
        val repsValue: TextView = itemView.findViewById(R.id.exercise_reps_value_text_view)
        val notesValue: TextView = itemView.findViewById(R.id.exercise_notes_value_text_view)

        init {

        }

        fun bind(exercise: Exercise) {
            exerciseName.text = exercise.name
            setsValue.text = exercise.sets.toString()
            repsValue.text = exercise.reps.toString()
            notesValue.text = exercise.notes
        }
    }
}