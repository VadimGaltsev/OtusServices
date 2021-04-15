package com.otus.services

import android.app.job.JobParameters
import android.app.job.JobService
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest

class MyJobService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UploadWorker>()
                .build()
        val operation = WorkManager.getInstance(this)
            .enqueue(uploadWorkRequest)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}