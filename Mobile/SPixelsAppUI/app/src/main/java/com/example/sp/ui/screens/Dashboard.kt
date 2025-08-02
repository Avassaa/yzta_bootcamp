package com.example.sp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.sp.R
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.sp.ui.components.BottomNavBar

@Composable
fun DashboardScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2030.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.db_bg),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "July",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 42.dp, bottom = 8.dp),
                        textAlign = TextAlign.Start
                    )

                    // Calendar
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp)
                    ) {
                        val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                        val calendar = listOf(
                            listOf("29", "30", "1", "2", "3", "4", "5"),
                            listOf("6", "7", "8", "9", "10", "11", "12"),
                            listOf("13", "14", "15", "16", "17", "18", "19"),
                            listOf("20", "21", "22", "23", "24", "25", "26"),
                            listOf("27", "28", "29", "30", "31", "1", "2")
                        )

                        val cellColors = listOf(
                            listOf("#FFF9B1", "#FFF9B1", "#E95B5B", "#90CAF9", "#B39DDB", "#B39DDB", "#B39DDB"),
                            listOf("#90CAF9", "#90CAF9", "#90CAF9", "#B39DDB", "#B39DDB", "#B39DDB", "#90CAF9"),
                            listOf("#FFF9B1", "#90CAF9", "#90CAF9", "#90CAF9", "#E95B5B", "#E95B5B", "#90CAF9"),
                            listOf("#90CAF9", "#90CAF9", "#B39DDB", "#B39DDB", "#B39DDB", "#B39DDB", "#B39DDB"),
                            listOf("#90CAF9", "#90CAF9", "#90CAF9", "#90CAF9", "#FFF9B1", "#FFF9B1", "#FFF9B1")
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            daysOfWeek.forEach { day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = day, fontSize = 14.sp)
                                }
                            }
                        }

                        calendar.forEachIndexed { rowIndex, week ->
                            Row(modifier = Modifier.fillMaxWidth()) {
                                week.forEachIndexed { colIndex, day ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(0.9f)
                                            .padding(1.9.dp)
                                            .background(
                                                color = Color(android.graphics.Color.parseColor(cellColors[rowIndex][colIndex])),
                                                shape = RoundedCornerShape(4.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = day)
                                    }
                                }
                            }
                        }
                    }
                }
            }

          //  Spacer(modifier = Modifier.height(100.dp)) // BottomNavBar için boşluk
        }

        // Sabit Bottom Navigation Bar
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(navController = navController)
        }
    }
}
