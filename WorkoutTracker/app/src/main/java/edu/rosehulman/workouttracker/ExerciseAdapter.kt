package edu.rosehulman.workouttracker

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import edu.rosehulman.workouttracker.ui.gallery.ExerciseListFragment

class ExerciseAdapter(val fragment: ExerciseListFragment): RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {
    val model = ViewModelProvider(fragment.requireActivity()).get(ExerciseViewModel::class.java)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciseAdapter.ExerciseViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ExerciseViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            TODO("Not yet implemented")
        }

        fun bind(exercise: Exercise) {
            TODO("Not yet implemented")
        }
    }
}