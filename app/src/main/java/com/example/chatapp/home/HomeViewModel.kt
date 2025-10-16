package com.example.chatapp.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.data.AccountCreate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference.child("users")
    private val auth = FirebaseAuth.getInstance()

    private val _currentUserProfile = MutableLiveData<AccountCreate>()
    val currentUserProfile: LiveData<AccountCreate> get() = _currentUserProfile

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchCurrentUserProfile() {
        val userId = auth.currentUser?.uid ?: run {
            _error.value = "User not logged in"
            return
        }

        database.child(userId).get().addOnSuccessListener { snapshot ->
            val name = snapshot.child("name").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val profileUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
            Log.d("HomeViewModel", "Fetched user profile: name=$name, email=$email, profileUrl=$profileUrl")
            _currentUserProfile.value = AccountCreate(name, email, "", profileUrl)
        }.addOnFailureListener { e ->
            _error.value = e.message
        }
    }
}
