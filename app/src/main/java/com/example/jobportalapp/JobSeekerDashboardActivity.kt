package com.example.jobportalapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class JobSeekerDashboardActivity : AppCompatActivity() {

    private lateinit var viewModel: JobSeekerDashboardViewModel
    private lateinit var adapter: JobListAdapter
    private lateinit var etSearch: TextInputEditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_seeker_dashboard)

        // 1. Initialize the database and repository
        val jobDao = AppDatabase.getDatabase(this).jobDao()
        val jobRepository = JobRepository(jobDao)

        // 2. Initialize ViewModel
        val factory = JobSeekerDashboardViewModel.Factory(jobRepository)
        viewModel = ViewModelProvider(this, factory)[JobSeekerDashboardViewModel::class.java]

        // 3. Initialize UI
        etSearch = findViewById(R.id.et_search)
        recyclerView = findViewById(R.id.recycler_view_jobs_job_seeker) // Add this ID to layout

        // 4. Setup RecyclerView
        adapter = JobListAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 5. Setup Observers and Listeners
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.jobs.observe(this) { jobs ->
            adapter.updateList(jobs)
        }
    }

    private fun setupListeners() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter jobs whenever the text changes
                viewModel.filterJobs(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}