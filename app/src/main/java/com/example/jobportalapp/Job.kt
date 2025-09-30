package com.example.jobportalapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class Job(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val recruiterId: String,
    val title: String,
    val company: String,
    val description: String,
    val location: String,
    val salary: String,
    val type: String,
    val postedDate: Long = System.currentTimeMillis()
)