package com.yzta.bootcampgroup84.ui.screens.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yzta.bootcampgroup84.interfaces.JournalEntry
import com.yzta.bootcampgroup84.interfaces.Screens
import com.yzta.bootcampgroup84.ui.screens.dashboard.DashboardViewModel
import com.yzta.bootcampgroup84.ui.screens.dashboard.JournalState // Ensure this import is correct
import com.yzta.bootcampgroup84.ui.screens.dashboard.JournalViewModel
import com.yzta.bootcampgroup84.ui.theme.MomentoBlue
import com.yzta.bootcampgroup84.ui.theme.VeryDarkGray

@Composable
fun ShowJournalSection(
    modifier: Modifier = Modifier,
    journalViewModel: JournalViewModel = viewModel(),
    navController: NavController,
) {
    val journalState by journalViewModel.journalState.collectAsState()
    val currentPage by journalViewModel.currentPage.collectAsState()
    val hasNextPage by journalViewModel.hasNextPage.collectAsState()

    // Default to false, check for roles only if currentUser is not null

    var showDeleteDialog by remember { mutableStateOf(false) }
    var entryToDelete by remember { mutableStateOf<JournalEntry?>(null) }

    if (showDeleteDialog && entryToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion", color = Color.Red, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this journal entry?", color = Color.White) },
            confirmButton = {
                TextButton(onClick = {
                    entryToDelete?.let {  }
                    showDeleteDialog = false
                    entryToDelete = null
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    entryToDelete = null
                }) {
                    Text("Cancel")
                }
            },
            containerColor = VeryDarkGray,
            textContentColor = Color.White
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Latest Journal Entries",
                fontSize = 22.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

                IconButton(onClick = {
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Journal Entry",
                        tint = MomentoBlue
                    )
                }

        }

        when (val state = journalState) {
            is JournalState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MomentoBlue)
                }
            }
            is JournalState.Success -> {
                if (state.entries.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No journal entries available.", color = Color.White, fontSize = 16.sp)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        state.entries.forEach { journalEntry ->
                            SwipeableJournalCard(
                                journalEntry = journalEntry,
                                onDelete = {

                                },
                                onEdit = {

                                },
                                canSwipe = true
                            )
                        }
                    }
                }
            }
            is JournalState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Failed to load entries: ${state.message}",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { journalViewModel.refresh() }) {
                        Text("Retry")
                    }
                }
            }
        }

        // Pagination Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { journalViewModel.previousPage() },
                enabled = currentPage > 1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MomentoBlue,
                    disabledContainerColor = MomentoBlue.copy(alpha = 0.5f)
                )
            ) {
                Text("Previous")
            }
            Text(text = "Page $currentPage", color = Color.White)
            Button(
                onClick = { journalViewModel.nextPage() },
                enabled = hasNextPage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MomentoBlue,
                    disabledContainerColor = MomentoBlue.copy(alpha = 0.5f)
                )
            ) {
                Text("Next")
            }
        }
    }
}

