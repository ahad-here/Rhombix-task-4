package com.example.jobportalapp


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jobportalapp.Job
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: Job)

    @Query("SELECT * FROM jobs ORDER BY postedDate DESC")
    fun getAllJobs(): Flow<List<Job>>

    @Query("SELECT * FROM jobs WHERE recruiterId = :recruiterId ORDER BY postedDate DESC")
    fun getJobsByRecruiter(recruiterId: String): Flow<List<Job>>
}