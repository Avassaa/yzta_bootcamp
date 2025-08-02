import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sp.R


@Composable
fun BottomNavBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF2A1102)) // Kahverengi
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Dashboard Icon
            Icon(
                painter = painterResource(id = R.drawable.nav_dashboard_icon),
                contentDescription = "Dashboard",
                modifier = Modifier.size(36.dp),
                tint = Color.Unspecified
            )

            // Camera (Center)
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .offset(y = (-24).dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A1102)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFFFFFF89), CircleShape)
                        .background(Color(0xFF2A1102)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.nav_camera__icon),
                        contentDescription = "Camera",
                        modifier = Modifier.size(42.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // Settings Icon
            Icon(
                painter = painterResource(id = R.drawable.nav_settings_icon),
                contentDescription = "Settings",
                modifier = Modifier.size(36.dp),
                tint = Color.Unspecified
            )
        }
    }
}
