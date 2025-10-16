package com.example.chatapp.signup

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignupViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

    fun createAccount(
        name: String,
        email: String,
        password: String,
        profileImageUri: Uri?, // Pass the image URI here
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: run {
                        onResult(false, "Failed to get user ID")
                        return@addOnCompleteListener
                    }
                    if (profileImageUri != null) {
                        // Upload profile image first
                        val imageRef = storage.child("profile_images/$userId.jpg")
                        imageRef.putFile(profileImageUri)
                            .addOnSuccessListener {
                                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                                    saveUserToDatabase(userId, name, email, downloadUrl.toString(), onResult)
                                }
                            }
                            .addOnFailureListener { e ->
                                onResult(false, e.message)
                            }
                    } else {
                        // No profile image, just save user
                        saveUserToDatabase(userId, name, email, null, onResult)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    private fun saveUserToDatabase(
        userId: String,
        name: String,
        email: String,
        profileImageUrl: String?,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userProfile = mutableMapOf(
            "name" to name,
            "email" to email
        )
        profileImageUrl?.let { userProfile["profileImageUrl"] = it }

        database.child("users").child(userId).setValue(userProfile)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }
}
