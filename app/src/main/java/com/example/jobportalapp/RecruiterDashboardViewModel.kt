// RecruiterDashboardViewModel.kt
package com.example.jobportalapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobportalapp.JobRepository
import com.example.jobportalapp.Job
import kotlinx.coroutines.launch

class RecruiterDashboardViewModel(private val jobRepository: JobRepository) : ViewModel() {


    private val _recruiterJobs = MutableLiveData<List<Job>>()
    val recruiterJobs: LiveData<List<Job>> = _recruiterJobs

    private val _postJobStatus = MutableLiveData<Boolean>()
    val postJobStatus: LiveData<Boolean> = _postJobStatus

    fun fetchRecruiterJobs(recruiterId: String) {
        viewModelScope.launch {
            // Collect the Flow and update the LiveData
            jobRepository.getJobsByRecruiter(recruiterId).collect { jobs ->
                _recruiterJobs.postValue(jobs)
            }
        }
    }

    fun postNewJob(job: Job) {
        viewModelScope.launch {
            jobRepository.postJob(job)
        }
    }
    class Factory(private val jobRepository: JobRepository) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecruiterDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecruiterDashboardViewModel(jobRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}