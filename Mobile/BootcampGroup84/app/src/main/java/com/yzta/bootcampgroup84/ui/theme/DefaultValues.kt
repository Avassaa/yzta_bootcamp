package com.yzta.bootcampgroup84.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val SmallPadding = 4.dp
val StandardPadding = 8.dp
val LargePadding = 16.dp

val SmallText= 12.sp
val StandardText= 16.sp
val TitleText = 24.sp

inline fun getStandardPadding(times:Int =1) = StandardPadding * times
inline fun getStandardTextSize(times:Int =1) = 16.sp * times