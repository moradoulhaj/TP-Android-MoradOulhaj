package com.example.tpandroidapp.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.tpandroidapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Localized strings
    val createAccountText = stringResource(id = R.string.create_account)
    val fullnameLabel = stringResource(id = R.string.fullname)
    val emailLabel = stringResource(id = R.string.email)
    val passwordLabel = stringResource(id = R.string.password)
    val phoneLabel = stringResource(id = R.string.phone)
    val signUpButton = stringResource(id = R.string.sign_up)
    val accountCreatedText = stringResource(id = R.string.account_created)
    val alreadyHaveAccount = stringResource(id = R.string.already_have_account)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1524995997946-a1c2e315a42f"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.6f }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = createAccountText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = state.fullname,
                    onValueChange = { viewModel.onIntent(SignupIntent.FullnameChanged(it)) },
                    label = { Text(fullnameLabel) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { viewModel.onIntent(SignupIntent.EmailChanged(it)) },
                    label = { Text(emailLabel) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onIntent(SignupIntent.PasswordChanged(it)) },
                    label = { Text(passwordLabel) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { viewModel.onIntent(SignupIntent.PhoneChanged(it)) },
                    label = { Text(phoneLabel) },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.onIntent(SignupIntent.SubmitSignup) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(signUpButton)
                    }
                }

                state.errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                if (state.isSuccess) {
                    Text(accountCreatedText, color = MaterialTheme.colorScheme.primary)
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                }

                TextButton(onClick = { navController.navigate("login") }) {
                    Text(alreadyHaveAccount)
                }
            }
        }
    }
}

