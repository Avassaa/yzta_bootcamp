package com.example.sp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createUserAndSaveDetails(name: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            // --- FIX #1: ADD VALIDATION HERE ---
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                onResult(false, "All fields must be filled.")
                return@launch // Stop the function here
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
                        val firebaseUser = authTask.result?.user
                        if (firebaseUser != null) {
                            val userMap = hashMapOf(
                                "uid" to firebaseUser.uid,
                                "name" to name,
                                "email" to email,
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
                    } else {
                        onResult(false, authTask.exception?.message)
                    }
                }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            // --- FIX #2: ADD VALIDATION HERE ---
            if (email.isBlank() || password.isBlank()) {
                onResult(false, "Email and password cannot be empty.")
                return@launch // Stop the function here
            }

            // This is line 58 from your crash log. It will now only run if the check above passes.
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
}