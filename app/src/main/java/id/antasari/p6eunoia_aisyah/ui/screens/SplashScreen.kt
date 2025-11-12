package id.antasari.p6eunoia_aisyah.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    var visible by remember { mutableStateOf(false) }

    // Fade-in animasi halus
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1500),
        label = "fadeInAnim"
    )

    // Jalankan animasi & timeout
    LaunchedEffect(Unit) {
        visible = true
        delay(2500)
        onTimeout()
    }

    // Background lembut gradien pastel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFF0F4),
                        Color(0xFFFFFBFA)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = visible) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Eunoia",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5E4B56),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.alpha(alphaAnim)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "a calm mind journal 🌸",
                        fontSize = 16.sp,
                        color = Color(0xFF7E6C6C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(alphaAnim)
                    )
                }
            }
        }
    }
}
