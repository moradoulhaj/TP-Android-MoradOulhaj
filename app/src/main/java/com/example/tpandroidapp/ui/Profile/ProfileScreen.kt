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
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: UserData,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Format ISO date to readable date
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

    // Collect events like Logout toast and navigation
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar on top
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(128.dp)
                        .padding(bottom = 16.dp)
                        .background(Color(0xFF6200EE), CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                // User info fields aligned start
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    UserInfoItem(label = "Nom complet", value = user.fullname)
                    UserInfoItem(label = "Email", value = user.email)
                    UserInfoItem(label = "Téléphone", value = user.phone)
                    UserInfoItem(label = "Role", value = if (user.admin == "0") "Customer" else "Admin")
                    UserInfoItem(label = "Joined at", value = formatDate(user.createdAt))

                    Spacer(modifier = Modifier.weight(1f))

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

@Composable
fun UserInfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF3F3F3), RoundedCornerShape(6.dp))
            .padding(12.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
