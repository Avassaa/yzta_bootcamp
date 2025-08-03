package com.example.sp.ui.viewmodels

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

import androidx.lifecycle.ViewModel
import com.example.sp.data.models.JournalEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnalysisViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val _journalEntry = MutableStateFlow<JournalEntry?>(null)
    val journalEntry = _journalEntry.asStateFlow()

    fun fetchJournalEntry(entryId: String) {
        db.collection("journal_entries").document(entryId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    _journalEntry.value = document.toObject(JournalEntry::class.java)
                } else {
                    // Handle case where document is not found, e.g., show an error state
                }
            }
    }
}