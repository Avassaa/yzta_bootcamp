package com.example.sp.datasource


import com.example.sp.model.MoodEntry
import java.time.LocalDate

class InMemoryDataSource : LocalDataSource {

    private val moodList = mutableListOf<MoodEntry>()

    override suspend fun insertMoodEntry(entry: MoodEntry) {
        moodList.add(entry)
    }

    override suspend fun getAllEntries(): List<MoodEntry> {
        return moodList
    }

    override suspend fun getEntriesForMonth(year: Int, month: Int): List<MoodEntry> {
        return moodList.filter {
            it.date.year == year && it.date.monthValue == month
        }
    }
}
