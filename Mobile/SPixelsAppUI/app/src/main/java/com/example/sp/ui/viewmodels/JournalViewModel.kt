package com.example.sp.ui.viewmodels

import JournalEntryRequest
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sp.data.models.JournalEntry
import com.example.sp.network.RetrofitClient
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

// Enhanced JournalEntry to include user profile data
data class JournalEntryWithUser(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val feelingText: String = "",
    val moodRating: Float = 0f,
    val entryPhotoUrl: String = "",
    val isPublic: Boolean = false,
    val timestamp: Long = 0L,
    val aiAnalysis: String = ""
)

// Sealed class to represent the different states of the UI
sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Success(val newEntryId: String) : UploadState()
    data class Error(val message: String) : UploadState()
}

class JournalViewModel : ViewModel() {

    // --- Service Instances ---
    private val auth = Firebase.auth
    private val storage = Firebase.storage
    private val db = Firebase.firestore
    private val api = RetrofitClient.instance

    // --- STATE MANAGEMENT ---
    // User's own journal entries (for Dashboard)
    private val _userEntries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val userEntries = _userEntries.asStateFlow()

    // Public journal entries with user data (for Journal/Explore page)
    private val _publicEntries = MutableStateFlow<List<JournalEntryWithUser>>(emptyList())
    val publicEntries = _publicEntries.asStateFlow()

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    // Loading states
    private val _isLoadingPublicEntries = MutableStateFlow(false)
    val isLoadingPublicEntries = _isLoadingPublicEntries.asStateFlow()

    // Cache for user data to avoid repeated fetches
    private val userDataCache = mutableMapOf<String, Pair<String, String>>() // userId -> (name, photoUrl)

    init {
        listenForUserJournals()
        fetchPublicJournalsWithUserData()
    }

    // Listen to current user's own journal entries
    private fun listenForUserJournals() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e("JournalViewModel", "No authenticated user found")
            return
        }

        db.collection("journal_entries")
            .whereEqualTo("userId", currentUser.uid) // Filter by current user's ID
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("JournalViewModel", "Listen for user journals failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _userEntries.value = snapshot.toObjects(JournalEntry::class.java)
                    Log.d("JournalViewModel", "Loaded ${_userEntries.value.size} user journals")
                }
            }
    }

    // Fetch public journal entries with user data
    private fun fetchPublicJournalsWithUserData() {
        viewModelScope.launch {
            _isLoadingPublicEntries.value = true
            try {
                // First, get all public journal entries
                val journalSnapshot = db.collection("journal_entries")
                    .whereEqualTo("isPublic", true)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .await()

                val journalEntries = journalSnapshot.toObjects(JournalEntry::class.java)
                val entriesWithUserData = mutableListOf<JournalEntryWithUser>()

                // Get unique user IDs to minimize database calls
                val uniqueUserIds = journalEntries.map { it.userId }.distinct()

                // Fetch user data for all unique users
                for (userId in uniqueUserIds) {
                    if (!userDataCache.containsKey(userId)) {
                        try {
                            val userDoc = db.collection("users")
                                .document(userId)
                                .get()
                                .await()

                            if (userDoc.exists()) {
                                val userName = userDoc.getString("name") ?: "Unknown User"
                                val userPhotoUrl = userDoc.getString("userPhotoUrl") ?: ""
                                userDataCache[userId] = Pair(userName, userPhotoUrl)
                            } else {
                                userDataCache[userId] = Pair("Unknown User", "")
                            }
                        } catch (e: Exception) {
                            Log.e("JournalViewModel", "Error fetching user data for $userId", e)
                            userDataCache[userId] = Pair("Unknown User", "")
                        }
                    }
                }

                // Combine journal entries with user data
                for (entry in journalEntries) {
                    val userData = userDataCache[entry.userId] ?: Pair("Unknown User", "")
                    val entryWithUser = JournalEntryWithUser(
                        id = entry.id,
                        userId = entry.userId,
                        userName = userData.first,
                        userPhotoUrl = userData.second,
                        feelingText = entry.feelingText,
                        moodRating = entry.moodRating,
                        entryPhotoUrl = entry.entryPhotoUrl,
                        isPublic = entry.isPublic,
                        aiAnalysis = entry.geminiAnalysis
                    )
                    entriesWithUserData.add(entryWithUser)
                }

                _publicEntries.value = entriesWithUserData
                Log.d("JournalViewModel", "Loaded ${entriesWithUserData.size} public journals with user data")

            } catch (e: Exception) {
                Log.e("JournalViewModel", "Error fetching public journals with user data", e)
            } finally {
                _isLoadingPublicEntries.value = false
            }
        }
    }

    // Listen to public journals with real-time updates (alternative approach)
    private fun listenForPublicJournalsWithUserData() {
        db.collection("journal_entries")
            .whereEqualTo("isPublic", true)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("JournalViewModel", "Listen for public journals failed.", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    // When journal entries change, refetch with user data
                    fetchPublicJournalsWithUserData()
                }
            }
    }

    // Method to refresh user's journals manually if needed
    fun refreshUserJournals() {
        listenForUserJournals()
    }

    // Method to refresh public journals manually if needed
    fun refreshPublicJournals() {
        fetchPublicJournalsWithUserData()
    }

    // Method to refresh both feeds
    fun refreshAllJournals() {
        refreshUserJournals()
        refreshPublicJournals()
    }

    // Clear user cache when needed (e.g., when user logs out)
    fun clearUserCache() {
        userDataCache.clear()
    }

    fun uploadJournalEntry(
        feelingText: String,
        moodRating: Float,
        photoUri: Uri
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Uploading
            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")
                val token = currentUser.getIdToken(true).await().token ?: throw Exception("Could not get auth token")

                val storagePath = "journal_photos/${currentUser.uid}/${UUID.randomUUID()}.jpg"
                val storageRef = storage.reference.child(storagePath)
                storageRef.putFile(photoUri).await()
                val photoUrl = storageRef.downloadUrl.await().toString()

                val requestBody = JournalEntryRequest(
                    feelingText = feelingText,
                    moodRating = moodRating,
                    entryPhotoUrl = photoUrl
                )

                val response = api.createJournalEntry("Bearer $token", requestBody)

                if (response.isSuccessful && response.body() != null) {
                    val newId = response.body()!!.documentId
                    _uploadState.value = UploadState.Success(newId)
                    // Refresh both feeds after successful upload
                    refreshAllJournals()
                } else {
                    throw Exception("Backend error: ${response.code()} ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.e("JournalViewModel", "Upload failed", e)
                _uploadState.value = UploadState.Error("Upload failed: ${e.message}")
            }
        }
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }

    // Helper method to check if current user owns a specific entry
    fun isCurrentUserEntry(entry: JournalEntry): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && entry.userId == currentUser.uid
    }

    // Helper method for JournalEntryWithUser
    fun isCurrentUserEntryWithUser(entry: JournalEntryWithUser): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null && entry.userId == currentUser.uid
    }

    // Method to get a specific user's data (useful for profile screens)
    suspend fun getUserData(userId: String): Pair<String, String>? {
        return try {
            if (userDataCache.containsKey(userId)) {
                userDataCache[userId]
            } else {
                val userDoc = db.collection("users")
                    .document(userId)
                    .get()
                    .await()

                if (userDoc.exists()) {
                    val userName = userDoc.getString("name") ?: "Unknown User"
                    val userPhotoUrl = userDoc.getString("userPhotoUrl") ?: ""
                    val userData = Pair(userName, userPhotoUrl)
                    userDataCache[userId] = userData
                    userData
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("JournalViewModel", "Error fetching user data for $userId", e)
            null
        }
    }
}