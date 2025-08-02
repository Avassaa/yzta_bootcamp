package com.example.sp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.sp.R
import com.example.sp.ui.screens.interFont // varsa bunu ekle
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sp.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(navController: NavController,authViewModel: AuthViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // To show a loading indicator
    val context = LocalContext.current

    val annotatedString = buildAnnotatedString {
        append("Already have an Account? ")
        pushStringAnnotation(tag = "login", annotation = "login")
        withStyle(
            style = SpanStyle(
                color = Color(0xFF0000EE),
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Sign in")
        }
        pop()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.signup),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(220.dp))

            Text(
                "Full name", fontFamily = interFont,
                color = Color(0xFFC4985F),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDFFC4),
                    unfocusedContainerColor = Color(0xFFFDFFC4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                placeholder = { Text("") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Email address", fontFamily = interFont,
                color = Color(0xFFC4985F),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDFFC4),
                    unfocusedContainerColor = Color(0xFFFDFFC4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                placeholder = { Text("") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Password", fontFamily = interFont,
                color = Color(0xFFC4985F),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDFFC4),
                    unfocusedContainerColor = Color(0xFFFDFFC4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    append("By signing up, you are agree to our ")
                    pushStringAnnotation(tag = "terms", annotation = "terms")
                    withStyle(SpanStyle(color = Color(0xFF0044CC), textDecoration = TextDecoration.Underline)) {
                        append("Terms & Conditions")
                    }
                    pop()
                    append(" and ")
                    pushStringAnnotation(tag = "privacy", annotation = "privacy")
                    withStyle(SpanStyle(color = Color(0xFF0044CC), textDecoration = TextDecoration.Underline)) {
                        append("Privacy Policy.")
                    }
                    pop()
                },
                fontSize = 16.sp,
                color = Color(0xFF6A4B2A),
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            Button(
                onClick = {
                    isLoading = true
                    authViewModel.createUserAndSaveDetails(name, email, password) { success, errorMessage ->
                        if (success) {
                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigate("signin") {
                                popUpTo("signup") { inclusive = true } // Go to sign in and remove sign up from back stack
                            }
                        } else {
                            Toast.makeText(context, "Error: ${errorMessage ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                        }
                        isLoading = false
                    }
                },
                enabled = !isLoading, // Disable button while loading
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1102))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        color = Color(0xFFF9FD89),
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations("login", offset, offset)
                        .firstOrNull()?.let {
                            navController.navigate("signin")
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = Color(0xFFC4985F),
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}
