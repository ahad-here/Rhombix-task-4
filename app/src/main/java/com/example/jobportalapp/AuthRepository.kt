package com.example.jobportalapp

import com.example.jobportalapp.UserRole
import kotlinx.coroutines.delay

class AuthRepository {

    // Mock "database" to store user credentials in memory
    private val mockUsers = mutableMapOf<String, Pair<String, UserRole>>()

    private suspend fun mockNetworkDelay() = delay(500)

    suspend fun register(email: String, password: String, role: UserRole): Boolean {
        mockNetworkDelay()
        return if (mockUsers.containsKey(email)) {
            false // User already exists
        } else {
            mockUsers[email] = Pair(password, role)
            true
        }
    }

    suspend fun login(email: String, password: String): UserRole? {
        mockNetworkDelay()
        val userCredentials = mockUsers[email]
        return if (userCredentials != null && userCredentials.first == password) {
            userCredentials.second // Return the role
        } else {
            null // Invalid credentials
        }
    }
}