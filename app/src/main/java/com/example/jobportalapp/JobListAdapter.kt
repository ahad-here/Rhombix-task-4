// JobListAdapter.kt
package com.example.jobportalapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jobportalapp.R
import com.example.jobportalapp.Job

class JobListAdapter(private var jobs: List<Job>) : RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    class JobViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_job_title)
        val tvCompany: TextView = view.findViewById(R.id.tv_job_company)
        val tvLocation: TextView = view.findViewById(R.id.tv_job_location)
        val tvSalaryType: TextView = view.findViewById(R.id.tv_job_salary_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            // You need to create this layout (item_job.xml) next!
            .inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobs[position]
        holder.tvTitle.text = job.title
        holder.tvCompany.text = job.company
        holder.tvLocation.text = job.location
        holder.tvSalaryType.text = "${job.salary} • ${job.type}"
    }

    override fun getItemCount() = jobs.size

    fun updateList(newJobs: List<Job>) {
        jobs = newJobs
        notifyDataSetChanged()
    }
}