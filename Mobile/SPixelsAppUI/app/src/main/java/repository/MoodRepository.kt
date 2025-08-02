package com.example.sp.repository

import com.example.sp.model.MoodEntry
import com.example.sp.model.AIAnalysis
import com.example.sp.datasource.LocalDataSource
import com.example.sp.network.AIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MoodRepository(
    private val localDataSource: LocalDataSource,
    private val aiService: AIService
) {

    suspend fun saveMoodEntry(
        date: LocalDate,
        photoUri: String,
        caption: String,
        moodScore: Int
    ): Result<MoodEntry> = withContext(Dispatchers.IO) {
        val entry = MoodEntry(date, photoUri, caption, moodScore)
        localDataSource.insertMoodEntry(entry)
        Result.success(entry)
    }

    suspend fun getAllMoodEntries(): List<MoodEntry> = withContext(Dispatchers.IO) {
        localDataSource.getAllEntries()
    }

    suspend fun getMoodEntriesForMonth(year: Int, month: Int): List<MoodEntry> =
        withContext(Dispatchers.IO) {
            localDataSource.getEntriesForMonth(year, month)
        }

    suspend fun analyzeEmotion(entry: MoodEntry): Result<AIAnalysis> = withContext(Dispatchers.IO) {
        try {
            val response = aiService.analyzeMood(entry.caption, entry.photoUri)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
