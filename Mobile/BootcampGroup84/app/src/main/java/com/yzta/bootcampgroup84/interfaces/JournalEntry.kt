package com.yzta.bootcampgroup84.interfaces

import java.util.Date

data class JournalEntry(
    val id: String,
    val date: String,
    val title: String,
    val content: String,
    val imageUrl: String,
    val author: String?
)