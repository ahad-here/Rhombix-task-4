// JobRepository.kt
package com.example.jobportalapp

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


class JobRepository (private val jobDao: JobDao){

    // Mock database: Stores all jobs
    private val mockJobListings = mutableListOf<Job>()

    // For simplicity, we'll store a hardcoded job to show on the dashboard
    init {
        // Mock Job Post 1 (Recruiter ID 1)
        mockJobListings.add(Job(
            recruiterId = "1",
            title = "Senior Android Developer",
            company = "TechCorp Solutions",
            description = "Lead the development of our flagship mobile applications.",
            location = "Remote",
            salary = "$120k - $150k",
            type = "Full-Time"
        ))
    }

    private suspend fun mockNetworkDelay() = delay(500)


    suspend fun postJob(job: Job) {
        jobDao.insertJob(job)
    }

    // Recruiter: Get jobs posted by a specific recruiter
    fun getJobsByRecruiter(recruiterId: String): Flow<List<Job>> {
        return jobDao.getJobsByRecruiter(recruiterId)
    }

    // Job Seeker: Get all jobs
    fun getAllJobs(): Flow<List<Job>> {
        return jobDao.getAllJobs()
    }
}