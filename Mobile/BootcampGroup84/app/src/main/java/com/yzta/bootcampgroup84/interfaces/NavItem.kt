package com.yzta.bootcampgroup84.interfaces

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val label : String,
    val icon : ImageVector,
    val badgeCount : Int,
)