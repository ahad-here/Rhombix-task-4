package com.example.jobportalapp


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class JobSeekerDashboardViewModel(private val jobRepository: JobRepository) : ViewModel() {

    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>> = _jobs

    private val _query = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            jobRepository.getAllJobs().collect { allJobs ->
                val filteredJobs = if (_query.value.isNullOrBlank()) {
                    allJobs
                } else {
                    allJobs.filter { job ->
                        val query = _query.value!!.lowercase()
                        job.title.lowercase().contains(query) ||
                                job.company.lowercase().contains(query) ||
                                job.description.lowercase().contains(query)
                    }
                }
                _jobs.postValue(filteredJobs)
            }
        }
    }

    fun filterJobs(query: String) {
        _query.value = query
    }

    class Factory(private val jobRepository: JobRepository) : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(JobSeekerDashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return JobSeekerDashboardViewModel(jobRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}