package com.example.sp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun createUserAndSaveDetails(
        name: String,
        email: String,
        password: String,
        profileImageUri: Uri? = null,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            // Validation
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                onResult(false, "All fields must be filled.")
                return@launch
            }

            try {
                // Create user with email and password
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    var userPhotoUrl: String? = null

                    // Upload profile picture if provided
                    if (profileImageUri != null) {
                        try {
                            userPhotoUrl = uploadProfileImage(firebaseUser.uid, profileImageUri)
                        } catch (e: Exception) {
                            // If image upload fails, we can still create the user without the photo
                            // or you can choose to fail the entire registration
                            onResult(false, "Failed to upload profile picture: ${e.message}")
                            return@launch
                        }
                    }

                    // Save user details to Firestore
                    val userMap = hashMapOf(
                        "uid" to firebaseUser.uid,
                        "name" to name,
                        "email" to email,
                        "userPhotoUrl" to userPhotoUrl,
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("users")
                        .document(firebaseUser.uid)
                        .set(userMap)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, dbTask.exception?.message)
                            }
                        }
                } else {
                    onResult(false, "User creation failed, could not get user details.")
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    private suspend fun uploadProfileImage(userId: String, imageUri: Uri): String {
        val imageRef = storage.reference
            .child("profile_images")
            .child("$userId/${UUID.randomUUID()}.jpg")

        val uploadTask = imageRef.putFile(imageUri).await()
        return imageRef.downloadUrl.await().toString()
    }

    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            // Validation
            if (email.isBlank() || password.isBlank()) {
                onResult(false, "Email and password cannot be empty.")
                return@launch
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(true, null)
                    } else {
                        onResult(false, task.exception?.message)
                    }
                }
        }
    }

    // Optional: Function to update profile picture after registration
    fun updateProfilePicture(imageUri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                try {
                    val photoUrl = uploadProfileImage(currentUser.uid, imageUri)

                    // Update Firestore document
                    db.collection("users")
                        .document(currentUser.uid)
                        .update("userPhotoUrl", photoUrl)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, task.exception?.message)
                            }
                        }
                } catch (e: Exception) {
                    onResult(false, e.message)
                }
            } else {
                onResult(false, "No user is currently signed in.")
            }
        }
    }

    // Optional: Function to get user data including profile picture
    fun getUserData(onResult: (Map<String, Any>?, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document != null && document.exists()) {
                            onResult(document.data, null)
                        } else {
                            onResult(null, "User data not found.")
                        }
                    } else {
                        onResult(null, task.exception?.message)
                    }
                }
        } else {
            onResult(null, "No user is currently signed in.")
        }
    }
}