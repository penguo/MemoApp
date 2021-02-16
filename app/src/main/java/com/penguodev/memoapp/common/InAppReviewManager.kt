package com.penguodev.memoapp.common

import android.app.Activity
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import timber.log.Timber

object InAppReviewHelper {
    fun show(activity: Activity, onSuccess: () -> Unit, onError: (e: Exception) -> Unit) {
        val manager = ReviewManagerFactory.create(activity)
        manager.requestReviewFlow()
            .addOnSuccessListener { reviewInfo ->
                manager.launchReviewFlow(activity, reviewInfo).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { exception ->
                    Timber.e(exception)
                    exception?.let { onError(exception) }
                }
            }.addOnFailureListener { exception ->
                Timber.e(exception)
                exception?.let { onError(exception) }
            }
//            .addOnCompleteListener { taskReview ->
//                if (taskReview.isSuccessful) {
//                    manager.launchReviewFlow(activity, taskReview.result).addOnCompleteListener {
//
//                    }
//                } else {
//                }
//            }
    }

    suspend fun showSuspend(activity: Activity) {
        val manager = ReviewManagerFactory.create(activity)
        manager.launchReview(activity, manager.requestReview())
    }
}