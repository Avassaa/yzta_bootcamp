package com.yzta.bootcampgroup84.ui.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yzta.bootcampgroup84.interfaces.JournalEntry
import com.yzta.bootcampgroup84.ui.components.JournalEntryCardWithImage

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableJournalCard(
    journalEntry: JournalEntry,
    onDelete: (JournalEntry) -> Unit,
    onEdit: (JournalEntry) -> Unit,
    canSwipe: Boolean,
    modifier: Modifier = Modifier
) {
    if (canSwipe) {
        val dismissState = rememberDismissState(
            confirmStateChange = { dismissValue ->
                when (dismissValue) {
                    DismissValue.DismissedToStart -> {
                        onDelete(journalEntry)
                        false
                    }
                    DismissValue.DismissedToEnd -> {
                        onEdit(journalEntry)
                        false
                    }
                    else -> false
                }
            }
        )

        SwipeToDismiss(
            state = dismissState,
            modifier = modifier,
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
            background = {
                val color = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Color(0xFF4CAF50)
                    DismissDirection.EndToStart -> Color(0xFFE53935)
                    null -> Color.Transparent
                }
                val alignment = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                    null -> Alignment.Center
                }
                val icon = when (dismissState.dismissDirection) {
                    DismissDirection.StartToEnd -> Icons.Default.Edit
                    DismissDirection.EndToStart -> Icons.Default.Delete
                    null -> null
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .padding(horizontal = 20.dp),
                    contentAlignment = alignment
                ) {
                    icon?.let {
                        Icon(it, contentDescription = "Action Icon", tint = Color.White)
                    }
                }
            },
            dismissContent = {
                JournalEntryCardWithImage(entry = journalEntry)
            }
        )
    } else {
        JournalEntryCardWithImage(entry = journalEntry, modifier = modifier)
    }
}