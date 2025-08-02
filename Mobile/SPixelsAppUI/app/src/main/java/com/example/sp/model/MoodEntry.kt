package com.example.sp.model

import java.time.LocalDate

data class MoodEntry(
    val date: LocalDate,
    val photoUri: String,
    val caption: String,
    val moodScore: Int
)
