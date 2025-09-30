// RecruiterDashboardActivity.kt
package com.example.jobportalapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobportalapp.JobPostDialogFragment
import com.example.jobportalapp.JobPostListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.jobportalapp.R
import com.example.jobportalapp.JobRepository
import com.example.jobportalapp.Job
import com.example.jobportalapp.JobListAdapter

class RecruiterDashboardActivity : AppCompatActivity(), JobPostListener {

    private lateinit var viewModel: RecruiterDashboardViewModel
    private lateinit var adapter: JobListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabPostJob: FloatingActionButton

    private var currentRecruiterId: String = "1"

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recruiter_dashborad)
        val jobDao = AppDatabase.getDatabase(this).jobDao()
        val jobRepository = JobRepository(jobDao)
        val factory = RecruiterDashboardViewModel.Factory(jobRepository)
        viewModel = ViewModelProvider(this, factory)[RecruiterDashboardViewModel::class.java]


        currentRecruiterId = intent.getStringExtra(EXTRA_USER_ID) ?: currentRecruiterId

        currentRecruiterId = intent.getStringExtra(EXTRA_USER_ID) ?: currentRecruiterId
        viewModel = ViewModelProvider(this, factory)[RecruiterDashboardViewModel::class.java]

        recyclerView = findViewById(R.id.recycler_view_jobs)
        fabPostJob = findViewById(R.id.fab_post_job)

        val logoutButton: Button = findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Call the logout logic here
            val authPreferences = AuthPreferences(this)
            authPreferences.clear()

            // Navigate back to the AuthActivity
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
        adapter = JobListAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // 5. Setup Listeners and Observers
        fabPostJob.setOnClickListener {
            showPostJobDialog()
        }

        setupObservers()
        viewModel.fetchRecruiterJobs(currentRecruiterId)
    }

    private fun setupObservers() {
        viewModel.recruiterJobs.observe(this) { jobs ->
            adapter.updateList(jobs)
        }

        viewModel.postJobStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Job Posted Successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to post job.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPostJobDialog() {
        JobPostDialogFragment.newInstance(currentRecruiterId)
            .show(supportFragmentManager, JobPostDialogFragment.TAG)
    }


    // the Listener interface
    override fun onJobPosted(job: Job) {
        viewModel.postNewJob(job)
    }
}