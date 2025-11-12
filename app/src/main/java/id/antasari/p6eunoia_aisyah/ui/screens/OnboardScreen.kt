package id.antasari.p6eunoia_aisyah.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p6eunoia_aisyah.R

// 🌸 WELCOME SCREEN (Step 1)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    onLogin: () -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFFFFBFA)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TopGradientHeader()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))

                Text(
                    text = "Welcome to",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF6D4C41)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFFF48FB1))) {
                            append("Eunoia")
                        }
                        append(" 🌷")
                    },
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 38.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(Modifier.height(60.dp))
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1))
                ) {
                    Text("Get started", fontSize = 16.sp, color = Color.White)
                }

                Spacer(Modifier.height(12.dp))
                TextButton(onClick = onLogin) {
                    Text(
                        "Log in and restore",
                        color = Color(0xFF8D6E63),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// 🌸 ASK NAME SCREEN (Step 2)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskNameScreen(
    onConfirm: (String) -> Unit,
    onSkip: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color(0xFF5E4B56))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFFFFBFA)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TopGradientHeader()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))
                Text(
                    "What’s your name?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF4E342E)
                )
                Spacer(Modifier.height(24.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your name") },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFF48FB1),
                        cursorColor = Color(0xFFF48FB1)
                    )
                )

                Spacer(Modifier.weight(1f))
                DotsIndicator(total = 4, current = 2)
                Spacer(Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onSkip,
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF8D6E63))
                    ) {
                        Text("Skip", fontSize = 16.sp)
                    }
                    Button(
                        onClick = { onConfirm(name.trim()) },
                        modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1))
                    ) {
                        Text("Confirm", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// 🌸 HELLO SCREEN (Step 3)
@Composable
fun HelloScreen(
    userName: String,
    onNext: () -> Unit
) {
    val displayName = if (userName.isBlank()) "Explorer" else userName

    Scaffold(containerColor = Color(0xFFFFFBFA)) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TopGradientHeader()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))
                Text(
                    buildAnnotatedString {
                        append("Welcome to your Eunoia,\n")
                        withStyle(SpanStyle(color = Color(0xFFF48FB1))) {
                            append(displayName)
                        }

                    },
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    ),
                    color = Color(0xFF4E342E)
                )

                Spacer(Modifier.height(40.dp))
                InfoCard(Icons.Outlined.Book, "Save your moments", "Capture thoughts & memories beautifully.")
                Spacer(Modifier.height(16.dp))
                InfoCard(Icons.Outlined.Lock, "Private & Secure", "Your stories stay only with you.")
                Spacer(Modifier.height(16.dp))
                InfoCard(Icons.Outlined.Mood, "Reflect your mood", "Understand yourself better each day.")

                Spacer(Modifier.weight(1f))
                DotsIndicator(total = 4, current = 3)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1))
                ) {
                    Text("Next", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

// 🌸 START JOURNALING (Step 4)
@Composable
fun StartJournalingScreen(onStart: () -> Unit) {
    Scaffold(containerColor = Color(0xFFFFFBFA)) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            TopGradientHeader()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(80.dp))
                Text(
                    "You’re all set!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4E342E)
                )

                Spacer(Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.banner_diary),
                    contentDescription = "Eunoia banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.weight(1f))
                DotsIndicator(total = 4, current = 4)
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF48FB1))
                ) {
                    Text("Got it! Let's start", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

// 🌸 Helper Components
@Composable
private fun TopGradientHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFF0F4),
                        Color(0xFFFFFBFA)
                    )
                )
            )
    )
}

@Composable
private fun DotsIndicator(total: Int, current: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(total) { i ->
            val isActive = i + 1 == current
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (isActive) 24.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isActive) Color(0xFFF48FB1) else Color(0xFFE0E0E0))
            )
        }
    }
}

@Composable
private fun InfoCard(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF0F4), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFFF48FB1),
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, color = Color(0xFF4E342E))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6D4C41))
        }
    }
}
