package com.yzta.bootcampgroup84.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        // Initial data load can be triggered here
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Simulate a network request or data fetch
            delay(2000)
            _isRefreshing.value = false
        }
    }
}