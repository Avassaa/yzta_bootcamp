package com.yzta.bootcampgroup84.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yzta.bootcampgroup84.R
import com.yzta.bootcampgroup84.interfaces.JournalEntry
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun JournalEntryCardWithImage(
    entry: JournalEntry,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .animateContentSize()
            .height(if (expanded) 400.dp else 220.dp)
            .clickable { expanded = !expanded }
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (entry.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(entry.imageUrl).crossfade(true).build(),
                    contentDescription = entry.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.landscape3), // A default placeholder image
                    contentDescription = "Placeholder Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.55f)))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = entry.title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.content,
                    color = Color.White.copy(alpha = 0.9f),
                    maxLines = if (expanded) 10 else 3,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .background(color = Color.Blue.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "Journal",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .background(color = Color.Black.copy(alpha = 0.7f), shape = RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = formatDate(entry.date),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        dateString
    }
}