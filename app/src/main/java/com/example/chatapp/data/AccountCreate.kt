package com.example.chatapp.data

import com.google.firebase.database.PropertyName

data class AccountCreate(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val profileUrl: String? = null
)
