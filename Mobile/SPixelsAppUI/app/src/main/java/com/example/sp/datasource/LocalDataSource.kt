package com.example.sp.datasource

import com.example.sp.model.MoodEntry
import java.time.LocalDate

interface LocalDataSource {

    suspend fun insertMoodEntry(entry: MoodEntry)

    suspend fun getAllEntries(): List<MoodEntry>

    suspend fun getEntriesForMonth(year: Int, month: Int): List<MoodEntry>
}


