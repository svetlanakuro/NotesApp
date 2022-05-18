package com.svetlana.kuro.notesapp.utils

import android.app.IntentService
import android.content.Intent
import android.util.Log

private const val TAG = "NoteAnalyticsServiceTAG"
const val NOTE_ANALYTICS_SERVICE_EXTRA = "NoteAnalyticsServiceExtra"

class NoteAnalyticsService(name: String = "NoteAnalyticsService") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent ${intent?.getStringExtra(NOTE_ANALYTICS_SERVICE_EXTRA)}")
    }

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }
}