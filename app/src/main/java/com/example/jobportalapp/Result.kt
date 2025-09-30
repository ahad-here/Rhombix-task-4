package com.example.jobportalapp


sealed class Result<out T> {
    data class Success<out T>(val userRole: UserRole) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
}