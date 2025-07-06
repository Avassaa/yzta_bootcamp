package com.yzta.bootcampgroup84.ui.screens.login
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yzta.bootcampgroup84.R
import com.yzta.bootcampgroup84.interfaces.Screens
import com.yzta.bootcampgroup84.ui.theme.NavyBlue

@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier) {
    val appLogo = R.drawable.app_logo

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.wrapContentSize(),
        ) {
            Image(

                painter = painterResource(id = appLogo),
                contentDescription = "App Logo",
                modifier = Modifier.size(64.dp).padding(8.dp),
            )

            Text(
                "Momento",
                modifier = Modifier.padding(8.dp),
                fontSize = 24.sp,
                color = Color.White

            )

            TextField(
                value = "",
                onValueChange = {},
                label = { Text("Email") },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NavyBlue) },
                modifier = modifier.padding(8.dp)
            )

            TextField(
                modifier = modifier.padding(bottom = 16.dp, top = 8.dp),
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = NavyBlue) },
            )

            Button(
                onClick = { navController.navigate(Screens.DashboardScreen.screenName) },
                modifier = modifier.height(45.dp)
            ) {
                Text("Login")
            }
        }
    }
}