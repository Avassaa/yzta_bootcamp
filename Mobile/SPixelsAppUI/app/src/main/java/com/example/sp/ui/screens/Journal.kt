package com.example.sp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sp.R
import com.example.sp.ui.components.BottomNavBar

val interFont = FontFamily(Font(R.font.inter_regular))

@Composable
fun JournalScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var mood by remember { mutableStateOf(5f) }
    var feelingText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.journal_bg),
                    contentDescription = "Background",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 110.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                ) {
                    // 🔹 Stories
                    val stories = listOf(
                        R.drawable.story_1,
                        R.drawable.story_2,
                        R.drawable.story_3,
                        R.drawable.story_4,
                        R.drawable.story_5
                    )

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(stories) { imageRes ->
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Red, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(180.dp))

                    // 🔹 Upload area
                    Row(
                        modifier = Modifier
                            .widthIn(30.dp)
                            .align(Alignment.CenterHorizontally)
                            .border(1.dp, Color(0xFFB07E5B), RoundedCornerShape(12.dp))
                            .padding(horizontal = 48.dp, vertical = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.take_a_photo_icon),
                                contentDescription = "Take Photo Icon",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Take a Photo", color = Color(0xFFB07E5B))
                        }

                        Text("or", color = Color(0xFFB07E5B))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Image(
                                painter = painterResource(id = R.drawable.upload_photo_icon),
                                contentDescription = "Upload Photo Icon",
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Upload a Photo", color = Color(0xFFB07E5B))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔹 Feelings Text
                    Text(
                        text = "Describe how you feel. (Only you’ll see this.)",
                        color = Color(0xFFB07E5B),
                        modifier = Modifier.padding(start = 18.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    BasicTextField(
                        value = feelingText,
                        onValueChange = { feelingText = it },
                        modifier = Modifier
                            .widthIn(330.dp)
                            .height(120.dp)
                            .background(Color(0xFFFFFFE0), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .align(Alignment.CenterHorizontally),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔹 Mood Slider
                    Text(
                        text = "Rate your mood",
                        color = Color(0xFFB07E5B),
                        modifier = Modifier.padding(start = 18.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = mood,
                        onValueChange = { mood = it },
                        valueRange = 1f..10f,
                        steps = 8,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF2A1102),
                            activeTrackColor = Color(0xFF2A1102),
                            inactiveTrackColor = Color(0x802A1102)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Positive\nFeelings", color = Color(0xFF2A1102))
                        Text("Negative\nFeelings", color = Color(0xFF2A1102))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* Upload logic */ },
                        modifier = Modifier
                            .widthIn(330.dp)
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1102)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Upload",
                            color = Color(0xFFF9FD89),
                            fontSize = 18.sp,
                            fontFamily = interFont,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JournalScreenPreview() {
    JournalScreen(navController = rememberNavController())
}
