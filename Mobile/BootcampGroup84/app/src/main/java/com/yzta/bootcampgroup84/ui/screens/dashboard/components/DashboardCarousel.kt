package com.TSNBank.TSNBank.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yzta.bootcampgroup84.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCarousel(modifier: Modifier = Modifier) {
    val carouselSize = 221.dp
    val carouselPhotoPlaceholder = listOf(R.drawable.landscape1, R.drawable.landscape2)
    val carouselState = rememberCarouselState { 25 }
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalMultiBrowseCarousel(
            state = carouselState,
            modifier = modifier
                .fillMaxWidth()
                .height(221.dp),
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(horizontal = 16.dp),
            preferredItemWidth = 412.dp,
        ) { index ->
            Box(modifier = Modifier
                .height(carouselSize)
                .fillMaxWidth()) {
                Image(
                    modifier = Modifier
                        .fillMaxSize()
                        .maskClip(MaterialTheme.shapes.extraLarge),
                    painter = painterResource(id = carouselPhotoPlaceholder[index%2]),
                    contentDescription = "Nature",
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .maskClip(MaterialTheme.shapes.extraLarge)

                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                    contentAlignment = Alignment.BottomStart
                ) {
                    Row(modifier=Modifier.fillMaxWidth().wrapContentHeight().background(Color.Black.copy(0.7f)),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Text(
                            "Welcome to Momento",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.weight(0.7f).padding(16.dp)
                        )
                        IconButton(onClick = { /*TODO*/ } ,modifier=Modifier.weight(0.3f)) {
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription ="Keep on reading",
                                tint=Color.White
                            )

                        }
                    }
                }
            }
        }
    }
}