// JobPostDialogFragment.kt
package com.example.jobportalapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.example.jobportalapp.R
import com.example.jobportalapp.Job

// Define an interface for the host activity to receive the new job
interface JobPostListener {
    fun onJobPosted(job: Job)
}

class JobPostDialogFragment : DialogFragment() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etCompany: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var etLocation: TextInputEditText
    private lateinit var etSalary: TextInputEditText
    private lateinit var etType: TextInputEditText
    private lateinit var btnPost: Button

    // We need the Recruiter ID to attach to the job
    private var recruiterId: String? = null

    companion object {
        const val TAG = "JobPostDialogFragment"
        private const val ARG_RECRUITER_ID = "recruiter_id"

        fun newInstance(recruiterId: String): JobPostDialogFragment {
            val fragment = JobPostDialogFragment()
            val args = Bundle()
            args.putString(ARG_RECRUITER_ID, recruiterId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recruiterId = arguments?.getString(ARG_RECRUITER_ID)
        // Make the dialog full-width
        setStyle(STYLE_NORMAL, R.style.Theme_JobPortalApp_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Assuming you have a layout named dialog_post_job.xml (create this next!)
        return inflater.inflate(R.layout.dialog_post_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        btnPost.setOnClickListener {
            postJob()
        }
    }

    private fun initViews(view: View) {
        etTitle = view.findViewById(R.id.et_job_title)
        etCompany = view.findViewById(R.id.et_company_name)
        etDescription = view.findViewById(R.id.et_job_description)
        etLocation = view.findViewById(R.id.et_job_location)
        etSalary = view.findViewById(R.id.et_job_salary)
        etType = view.findViewById(R.id.et_job_type)
        btnPost = view.findViewById(R.id.btn_post_job)
    }

    private fun postJob() {
        val title = etTitle.text.toString()
        val company = etCompany.text.toString()
        val description = etDescription.text.toString()
        val location = etLocation.text.toString()
        val salary = etSalary.text.toString()
        val type = etType.text.toString()

        if (title.isBlank() || company.isBlank() || recruiterId == null) {
            Toast.makeText(requireContext(), "Please fill in title and company.", Toast.LENGTH_SHORT).show()
            return
        }

        val newJob = Job(
            recruiterId = recruiterId!!,
            title = title,
            company = company,
            description = description,
            location = location,
            salary = salary,
            type = type
        )

        // Pass the new job back to the host activity/fragment
        (activity as? JobPostListener)?.onJobPosted(newJob)
        dismiss()
    }
}