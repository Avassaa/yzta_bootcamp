package com.example.sp.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sp.R
import com.example.sp.ui.components.BottomNavBar
import com.example.sp.ui.viewmodels.JournalViewModel
import com.example.sp.ui.viewmodels.UploadState

@Composable
fun JournalScreen(
    navController: NavController,
    journalViewModel: JournalViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    var mood by remember { mutableStateOf(5f) }
    var feelingText by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val publicEntries by journalViewModel.publicEntries.collectAsState()
    val uploadState by journalViewModel.uploadState.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectedPhotoUri = uri }
    )

    LaunchedEffect(key1 = uploadState) {
        when (val state = uploadState) {
            is UploadState.Success -> {
                Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                navController.navigate("analysis/${state.newEntryId}") {
                    popUpTo("journal") { inclusive = true }
                }
                journalViewModel.resetUploadState()
            }
            is UploadState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                journalViewModel.resetUploadState()
            }
            else -> Unit
        }
    }

    Scaffold(bottomBar = { BottomNavBar(navController = navController) }) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding()) {
            Image(
                painter = painterResource(id = R.drawable.journal_bg),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(publicEntries) { entry ->
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Red, CircleShape)
                                .clickable { navController.navigate("analysis/${entry.id}") },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = entry.userPhotoUrl.ifEmpty { R.drawable.profile_placeholder },
                                contentDescription = entry.userName,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(180.dp))
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .border(1.dp, Color(0xFFB07E5B), RoundedCornerShape(12.dp))
                        .clickable(enabled = uploadState !is UploadState.Uploading) { imagePickerLauncher.launch("image/*") }
                        .padding(horizontal = 48.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.take_a_photo_icon), contentDescription = "Take Photo", modifier = Modifier.size(48.dp))
                        Text("Take a Photo", color = Color(0xFFB07E5B))
                    }
                    Text("or", color = Color(0xFFB07E5B))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(painter = painterResource(id = R.drawable.upload_photo_icon), contentDescription = "Upload Photo", modifier = Modifier.size(48.dp))
                        Text("Upload a Photo", color = Color(0xFFB07E5B))
                    }
                }
                selectedPhotoUri?.let { uri ->
                    Spacer(modifier = Modifier.height(16.dp))
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selected photo preview",
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)).align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
                Text("Describe how you feel...", color = Color(0xFFB07E5B), modifier = Modifier.padding(start = 18.dp))
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = feelingText,
                    onValueChange = { feelingText = it },
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp).background(Color(0xFFFFFFE0), RoundedCornerShape(8.dp)).padding(12.dp),
                    textStyle = TextStyle(fontSize = 16.sp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text("Rate your mood", color = Color(0xFFB07E5B), modifier = Modifier.padding(start = 18.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = mood, onValueChange = { mood = it }, valueRange = 1f..10f, steps = 8,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    colors = SliderDefaults.colors(thumbColor = Color(0xFF2A1102), activeTrackColor = Color(0xFF2A1102), inactiveTrackColor = Color(0x802A1102))
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Negative", color = Color(0xFF2A1102))
                    Text("Positive", color = Color(0xFF2A1102))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { selectedPhotoUri?.let { uri -> journalViewModel.uploadJournalEntry(feelingText, mood, uri) } },
                    enabled = selectedPhotoUri != null && uploadState !is UploadState.Uploading,
                    modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1102)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uploadState is UploadState.Uploading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Text(text = "Upload", color = Color(0xFFF9FD89), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}