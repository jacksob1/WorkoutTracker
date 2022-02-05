package edu.rosehulman.workouttracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.workouttracker.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    lateinit var model: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        model.getOrMakeUser {
            binding.profileName.text = model.user!!.name
            binding.profileDetail1.text = model.user!!.age.toString()+" years old"
            binding.profileDetail2.text = model.user!!.major
        }
        if(model.user!!.storageUriString.isNotEmpty()) {
            binding.profileIcon.load(model.user!!.storageUriString) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
        binding.clearButton.setOnClickListener {
            Firebase.auth.signOut()
            model.logout()
        }
        binding.doneButton.setOnClickListener {
            findNavController().navigate(R.id.nav_user_edit)
        }
        return binding.root
    }
}