package com.example.chatapp.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.currentUserProfile.observe(this) { user ->
            user.profileUrl?.let { url ->
                Glide.with(this)
                    .load(url)
                    .into(binding.btnProfile) // make sure btnProfile is ImageView
            }
        }


        homeViewModel.error.observe(this) { err ->
            // Show error if needed
        }

        // Fetch current user profile
        homeViewModel.fetchCurrentUserProfile()
    }
}
