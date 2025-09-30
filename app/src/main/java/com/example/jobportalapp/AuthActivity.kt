package com.example.jobportalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jobportalapp.databinding.ActivityMainBinding

// Note: I've removed the redundant imports for individual UI elements 
// (Button, LinearLayout, RadioButton, RadioGroup, TextView, TextInputLayout)
// as View Binding makes them unnecessary.

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AuthViewModel
    // Using by lazy for AuthRepository is cleaner since it's only used for ViewModel Factory
    private val repository by lazy { AuthRepository() }
    private lateinit var authPreferences: AuthPreferences

    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        authPreferences = AuthPreferences(this)

        val (isLoggedIn, role) = authPreferences.getLoginStatus()
        if (isLoggedIn && role != null) {
            navigateToDashboard(UserRole.valueOf(role))
            return
        }


        val factory = AuthViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]


        setupListeners()
        setupObservers()


        updateUiMode()
    }



    private fun setupListeners() {

        binding.btnPrimaryAction.setOnClickListener {
            if (isLoginMode) {
                performLogin()
            } else {
                performRegister()
            }
        }
        binding.tvToggleMode.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUiMode()
        }
    }

    private fun updateUiMode() {
        if (isLoginMode) {
            binding.tvTitle.text = "Login"
            binding.tilName.visibility = View.GONE
            binding.llRoleSelector.visibility = View.GONE
            binding.btnPrimaryAction.text = "Login"
            binding.tvToggleMode.text = "Don't have an account? Register"
        } else {
            binding.tvTitle.text = "Register"
            binding.tilName.visibility = View.VISIBLE
            binding.llRoleSelector.visibility = View.VISIBLE
            binding.btnPrimaryAction.text = "Register"
            binding.tvToggleMode.text = "Already have an account? Login"
        }
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill in email and password", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.loginUser(email, password)
    }

    private fun performRegister() {
        val name = binding.etName.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val role = if (binding.rbJobSeeker.isChecked) UserRole.JOB_SEEKER else UserRole.RECRUITER
        viewModel.registerUser(name, email, password, role)
        // Note: Assumed registerUser accepts a name based on your UI logic
    }

    private fun setupObservers() {
        viewModel.authResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    handleAuthSuccess(result.userRole)
                }
                is Result.Failure -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun handleAuthSuccess(userRole: UserRole) {
        authPreferences.saveLoginStatus(true, userRole.name)
        navigateToDashboard(userRole)
    }

    private fun navigateToDashboard(role: UserRole) {
        val intent = when (role) {
            UserRole.JOB_SEEKER -> Intent(this, JobSeekerDashboardActivity::class.java)
            UserRole.RECRUITER -> Intent(this, RecruiterDashboardActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    fun logoutUser() {
        authPreferences.clear()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

}