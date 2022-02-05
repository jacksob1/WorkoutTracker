package edu.rosehulman.workouttracker

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel: ViewModel() {
    val uid = Firebase.auth.uid!!
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid)

    var user: User? = null

    fun getOrMakeUser(observer: () -> Unit) {
        ref = Firebase.firestore.collection("users").document(Firebase.auth.uid!!)
        if(user != null) {
            //get
            observer()
        } else {
            // make
            ref.get().addOnSuccessListener { snapshot: DocumentSnapshot ->
                if(snapshot.exists()) {
                    user = snapshot.toObject(User::class.java)
                } else {
                    user = User(name= Firebase.auth.currentUser!!.displayName!!)
                    ref.set(user!!)
                }
                observer()
            }
        }
    }

    fun hasCompletedSetup() = user?.hasCompletedSetup ?: false

    fun update(newName: String, newAge: Int, newMajor: String, newStorageUriString: String, newHasCompletedSetup: Boolean) {
        ref = Firebase.firestore.collection("users").document(Firebase.auth.uid!!)
        if(user != null) {
            with(user!!) {
                name = newName
                age = newAge
                major = newMajor
                storageUriString = newStorageUriString
                hasCompletedSetup = newHasCompletedSetup
                ref.set(this)
            }
        }
    }

    fun logout() {
        user = null
    }
}