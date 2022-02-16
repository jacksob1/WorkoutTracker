package edu.rosehulman.workouttracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.workouttracker.ui.gallery.ExerciseListFragment

class ExerciseAdapter(val fragment: ExerciseListFragment): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(ExerciseViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseAdapter.ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.exercise_card, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        model.updateCurrentPos(position)
        holder.bind(model.getExerciseAt(position))
    }

    override fun getItemCount() = model.size()
    fun addExercise(exercise: Exercise?) {
        if(model.size() != 0) {
            model.updateCurrentPos(model.size())
        }
        model.addExercise(exercise)
    }

    fun addListener() {
        model.addListener() {
            notifyDataSetChanged()
        }
    }

    fun setContextForExercises(newContext: Context) {
        model.setContextForExercises(newContext)
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exercise_name_text_view)
        val setsValue: TextView = itemView.findViewById(R.id.exercise_sets_value_text_view)
        val repsValue: TextView = itemView.findViewById(R.id.exercise_reps_value_text_view)
        val notesValue: TextView = itemView.findViewById(R.id.exercise_notes_value_text_view)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_icon)

        init {
            itemView.setOnClickListener {
                model.updateCurrentPos(adapterPosition)
                itemView.findNavController().navigate(R.id.nav_exercise_form)
            }

            deleteButton.setOnClickListener {
                model.removeExerciseAt(adapterPosition)
                notifyDataSetChanged()
            }
        }

        fun bind(exercise: Exercise) {
            model.updateCurrentPos(adapterPosition)
            exerciseName.text = exercise.name
            setsValue.text = exercise.sets.toString()
            repsValue.text = exercise.reps.toString()
            notesValue.text = exercise.notes
        }
    }
}