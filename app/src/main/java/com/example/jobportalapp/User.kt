// User.kt
package com.example.jobportalapp

enum class UserRole {
    JOB_SEEKER,
    RECRUITER
}

public data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    // Add other relevant fields like profile image URL, company name, etc.
)