// AuthResponse.kt
package com.example.jobportalapp




sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}