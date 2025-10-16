    package com.example.chatapp.signup

    import android.content.Intent
    import android.net.Uri
    import android.os.Bundle
    import android.widget.Toast
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import com.example.chatapp.databinding.ActivitySignupBinding
    import com.example.chatapp.signin.SigninActivity

    class SignupActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySignupBinding
        private var imageUri: Uri? = null

        private val pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                Toast.makeText(this, "Image Selected!", Toast.LENGTH_SHORT).show()
                imageUri = uri
                binding.ivProfileImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        private val signupViewModel: SignupViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySignupBinding.inflate(layoutInflater)
            setContentView(binding.root)
            binding.ivProfileImage.setOnClickListener {
                pickImageLauncher.launch("image/*")
            }

            binding.btnCreateAccount.setOnClickListener {
                val name = binding.etName.text.toString().trim()
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                val confirmPassword = binding.etConfirmPassword.text.toString().trim()

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != confirmPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                signupViewModel.createAccount(name, email, password,imageUri) { success, error ->
                    if (success) {
                        Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Account Creation Failed: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
