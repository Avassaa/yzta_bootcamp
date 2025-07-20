package com.yzta.bootcampgroup84.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yzta.bootcampgroup84.interfaces.JournalEntry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// This sealed class defines the possible states for the UI
sealed class JournalState {
    object Loading : JournalState()
    data class Success(val entries: List<JournalEntry>) : JournalState()
    data class Error(val message: String) : JournalState()
}

class JournalViewModel : ViewModel() {

    private val _journalState = MutableStateFlow<JournalState>(JournalState.Loading)
    val journalState = _journalState.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage = _currentPage.asStateFlow()

    // We only have one page of sample data, so this is false
    private val _hasNextPage = MutableStateFlow(false)
    val hasNextPage = _hasNextPage.asStateFlow()

    init {
        loadJournalEntries()
    }

    fun refresh() {
        loadJournalEntries(page = 1)
    }

    fun deleteJournalEntry(id: String) {
        viewModelScope.launch {
            // This simulates deleting an entry from the current list
            if (_journalState.value is JournalState.Success) {
                val currentEntries = (_journalState.value as JournalState.Success).entries.toMutableList()
                currentEntries.removeAll { it.id == id }
                _journalState.value = JournalState.Success(currentEntries)
            }
        }
    }

    fun nextPage() { /* No action for sample data */ }
    fun previousPage() { /* No action for sample data */ }

    private fun loadJournalEntries(page: Int = _currentPage.value) {
        viewModelScope.launch {
            // 1. Set state to Loading to show the circular progress indicator
            _journalState.value = JournalState.Loading

            // 2. Simulate a network delay
            delay(1500)

            // 3. Create a list of sample entries matching the JournalEntry data class
            val sampleEntries = listOf(
                JournalEntry(
                    id = "1",
                    date = "2025-07-21T10:30:00Z",
                    title = "A Walk in the Mountains",
                    content = "The air was crisp and the views were breathtaking. A perfect day for a hike to clear the mind and get some exercise.",
                    imageUrl = "https://images.pexels.com/photos/417074/pexels-photo-417074.jpeg",
                    author = "Admin"
                ),
                JournalEntry(
                    id = "2",
                    date = "2025-07-20T14:00:00Z",
                    title = "New Coding Project",
                    content = "Started working on a new Jetpack Compose project. The declarative UI paradigm feels so intuitive and powerful.",
                    imageUrl = "https://images.pexels.com/photos/546819/pexels-photo-546819.jpeg",
                    author = "Admin"
                ),
                JournalEntry(
                    id = "3",
                    date = "2025-07-19T18:45:00Z",
                    title = "Exploring a Seaside Town",
                    content = "Visited a charming little town by the sea. The sound of the waves and the fresh seafood were the highlights of the day.",
                    imageUrl = "https://images.pexels.com/photos/1654834/pexels-photo-1654834.jpeg",
                    author = "Admin"
                )
            )

            // 4. Update the state to Success with the new list, which will update the UI
            _journalState.value = JournalState.Success(sampleEntries)
        }
    }
}