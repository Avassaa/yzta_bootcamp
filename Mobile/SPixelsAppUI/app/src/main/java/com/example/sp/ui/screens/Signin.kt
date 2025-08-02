package com.example.sp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sp.R
import com.example.sp.ui.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
    navController: NavHostController,
    // 1. Get an instance of the AuthViewModel
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    // 2. Add state variables for loading and error handling
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.signin),
            contentDescription = "Sign In Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(240.dp))

            Text(
                text = "Email address",
                fontSize = 16.sp,
                color = Color(0xFFC4985F),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                // 3. Disable the field when loading
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDFFC4),
                    unfocusedContainerColor = Color(0xFFFDFFC4),
                    disabledContainerColor = Color(0xFFFDFFC4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Password",
                fontSize = 16.sp,
                color = Color(0xFFC4985F),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                // 4. Disable the field when loading
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFDFFC4),
                    unfocusedContainerColor = Color(0xFFFDFFC4),
                    disabledContainerColor = Color(0xFFFDFFC4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- THIS IS THE MAIN CHANGE ---
            Button(
                onClick = {
                    isLoading = true
                    // 5. Call the ViewModel function
                    authViewModel.signInWithEmailAndPassword(email, password) { success, errorMessage ->
                        if (success) {
                            Toast.makeText(context, "Sign in successful!", Toast.LENGTH_SHORT).show()
                            // 6. Navigate to dashboard on success and clear the back stack
                            navController.navigate("dashboard") {
                                popUpTo(0) // Clears the entire back stack
                            }
                        } else {
                            // 7. Show error on failure
                            Toast.makeText(context, "Error: ${errorMessage ?: "Unknown error"}", Toast.LENGTH_LONG).show()
                        }
                        isLoading = false
                    }
                },
                // 8. Disable button when loading
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1102))
            ) {
                // 9. Show a loading indicator inside the button
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Log in", fontSize = 18.sp, color = Color(0xFFF9FD89))
                }
            }


            Spacer(modifier = Modifier.height(24.dp))
            // ... (Rest of your UI is perfect, no changes needed) ...
            Text("Or Continue with", fontSize = 16.sp, color = Color(0xFF6C4E2A))

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Image(painter = painterResource(id = R.drawable.google_icon), contentDescription = "Google", modifier = Modifier.size(48.dp))
                Image(painter = painterResource(id = R.drawable.fb_icon), contentDescription = "Facebook", modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(48.dp))

            val annotatedText = buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF2A1102))) {
                    append("Don't have an Account? ")
                }
                pushStringAnnotation(tag = "signup", annotation = "signup")
                withStyle(
                    SpanStyle(
                        color = Color(0xFF0000EE),
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium
                    )
                ) {
                    append("Sign up")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations("signup", offset, offset)
                        .firstOrNull()?.let {
                            navController.navigate("signup")
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}