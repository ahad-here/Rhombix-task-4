package com.example.jobportalapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authResult = MutableLiveData<Result<UserRole>>()
    val authResult: LiveData<Result<UserRole>> = _authResult

    fun registerUser(email: String, password: String, password1: String, role: UserRole) {
        viewModelScope.launch {
            try {
                val success = authRepository.register(email, password, role)
                if (success) {
                    _authResult.value = Result.Success(role)
                } else {
                    _authResult.value = Result.Failure("Registration failed.")
                }
            } catch (e: Exception) {
                _authResult.value = Result.Failure(e.message ?: "An error occurred.")
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val role = authRepository.login(email, password)
                if (role != null) {
                    _authResult.value = Result.Success(role)
                } else {
                    _authResult.value = Result.Failure("Invalid credentials.")
                }
            } catch (e: Exception) {
                _authResult.value = Result.Failure(e.message ?: "An error occurred.")
            }
        }
    }

    // Factory to correctly instantiate the ViewModel with the repository
    class Factory(private val authRepository: AuthRepository) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}