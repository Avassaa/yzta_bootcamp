package com.example.sp.data.models

import com.google.firebase.firestore.DocumentId

data class JournalEntry(
    // Use @DocumentId to automatically map the document's ID to this field
    @DocumentId val id: String = "",

    // User information
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",

    // Entry content
    val feelingText: String = "",
    val moodRating: Float = 0f,
    val entryPhotoUrl: String = "",
    val geminiAnalysis: String = "",

    // Metadata
    val isPublic: Boolean = true
    // Timestamp is handled by the server, so we don't strictly need it here
)