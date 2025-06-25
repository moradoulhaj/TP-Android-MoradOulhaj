package com.example.tpandroidapp.ui.Profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest
import com.example.tpandroidapp.data.model.UserData
import com.example.tpandroidapp.ui.utils.UserInfoItem
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: UserData,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    var isEditing by remember { mutableStateOf(false) }

    // Format date function
    fun formatDate(isoDate: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            parser.timeZone = TimeZone.getTimeZone("UTC")
            val date = parser.parse(isoDate) ?: return isoDate

            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            isoDate
        }
    }
    val avatarUrl = user.fullname.replace(" ", "+").let {
        "https://ui-avatars.com/api/?name=$it&background=6200EE&color=fff&size=128"
    }

    // State vars with initial user data
    var fullname by remember { mutableStateOf(user.fullname) }
    var email by remember { mutableStateOf(user.email) }
    var phone by remember { mutableStateOf(user.phone) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Events collection same as before
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProfileViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is ProfileViewModel.UiEvent.NavigateToLogin -> {
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(128.dp)
                        .padding(bottom = 16.dp)
                        .background(Color(0xFF6200EE), CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    OutlinedTextField(
                        value = fullname,
                        onValueChange = { fullname = it },
                        label = { Text("Nom complet") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Téléphone") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = isEditing,
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (isEditing) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Mot de passe (laisser vide si inchangé)") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmer le mot de passe") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
if (!isEditing){
    UserInfoItem(label = "Role", value = if (user.admin == "0") "Customer" else "Admin")
    UserInfoItem(label = "Cree le", value = formatDate(user.createdAt))
}


                    Spacer(modifier = Modifier.height(24.dp))

                    if (isEditing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    // Cancel editing, revert to original values
                                    fullname = user.fullname
                                    email = user.email
                                    phone = user.phone
                                    password = ""
                                    confirmPassword = ""
                                    isEditing = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text("Annuler", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    viewModel.handleIntent(
                                        ProfileIntent.UpdateUser(
                                            fullname = fullname,
                                            email = email,
                                            phone = phone,
                                            password = password.takeIf { it.isNotBlank() },
                                            confirmPassword = confirmPassword.takeIf { it.isNotBlank() }
                                        )
                                    )
                                    isEditing = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                            ) {
                                Text("Enregistrer", color = Color.White)
                            }
                        }
                    } else {
                        Button(
                            onClick = { isEditing = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                        ) {
                            Text("Modifier les informations", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.handleIntent(ProfileIntent.Logout) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text("Se déconnecter", color = Color.White)
                    }
                }
            }
        }
    }
}

