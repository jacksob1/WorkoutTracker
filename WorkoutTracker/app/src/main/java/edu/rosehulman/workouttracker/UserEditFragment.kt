package edu.rosehulman.workouttracker

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.rosehulman.workouttracker.databinding.FragmentUserEditBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

class UserEditFragment : Fragment() {
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.profileImage.setImageURI(uri)
                    addPhotoFromUri(uri)
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.profileImage.setImageURI(uri)
                addPhotoFromUri(uri)
            }
        }

    private var latestTmpUri: Uri? = null
    private lateinit var binding: FragmentUserEditBinding
    private val storageImagesRef = Firebase.storage
        .reference
        .child("images")
    private var storageUriStringInFragment: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val userModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        Log.d("WT", "User in user edit fragment: ${userModel.user}")

        binding = FragmentUserEditBinding.inflate(inflater, container, false)
        binding.userEditDoneButton.setOnClickListener {
            // Save user info into Firestore.
            val newAgeString = binding.userEditAgeEditText.text.toString()
            userModel.update(
                newName = binding.userEditNameEditText.text.toString(),
                newAge = if (newAgeString.isNotBlank()) newAgeString.toInt() else -1,
                newMajor = binding.userEditMajorEditText.text.toString(),
                newStorageUriString = storageUriStringInFragment,
                newHasCompletedSetup = true
            )
            findNavController().navigate(R.id.nav_user)
        }
        binding.userEditUploadPhotoButton.setOnClickListener {
            showPictureDialog()
        }

        userModel.getOrMakeUser {
            with(userModel.user!!) {
                Log.d("WT", "$this")
                binding.userEditNameEditText.setText(name)
                binding.userEditAgeEditText.setText(age.toString())
                binding.userEditMajorEditText.setText(major)
                // storageUriStringInFragment = storageUriString
            }
        }
        return binding.root
    }

    private fun showPictureDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose a photo source")
        builder.setMessage("Would you like to take a new picture?\nOr choose an existing one?")
        builder.setPositiveButton("Take Picture") { _, _ ->
            binding.userEditDoneButton.isEnabled = false
            binding.userEditDoneButton.text = "Loading image"
            takeImage()
        }

        builder.setNegativeButton("Choose Picture") { _, _ ->
            binding.userEditDoneButton.isEnabled = false
            binding.userEditDoneButton.text = "Loading image"
            selectImageFromGallery()
        }
        builder.create().show()
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val tmpFile = File.createTempFile("JPEG_${timeStamp}_", ".png", storageDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun addPhotoFromUri(uri: Uri?) {
        if (uri == null) {
            Log.e("WT", "Uri is null. Not saving to storage")
            return
        }
        val stream = requireActivity().contentResolver.openInputStream(uri)
        if (stream == null) {
            Log.e("WT", "Stream is null. Not saving to storage")
            return
        }

        // TODO: Add to storage
        val imageId = abs(Random.nextLong()).toString()
        storageImagesRef.child(imageId).putStream(stream)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageImagesRef.child(imageId).downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageUriStringInFragment = task.result.toString()
                    Log.d("WT", "Got download uri: $storageUriStringInFragment")
                    binding.userEditDoneButton.text = "done"
                    binding.userEditDoneButton.isEnabled = true
                } else {
                    // Handle failures
                    // ...
                }
            }
    }
}