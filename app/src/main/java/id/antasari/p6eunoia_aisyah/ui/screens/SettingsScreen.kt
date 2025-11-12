package id.antasari.p6eunoia_aisyah.ui.screens

// (Import-import lama)
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// --- (Kita tidak butuh import .border lagi) ---

// --- UI UTAMA SETTINGS SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userName: String?
) {
    val displayName = remember(userName) {
        if (userName.isNullOrBlank()) "Anonymous" else userName
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                // --- MODIFIKASI 1: TopAppBar Transparan ---
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            // --- MODIFIKASI 2: Beri jarak antar Kartu ---
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- (Semua item list tidak berubah) ---
            item { SectionHeader("PERSONAL") }
            item {
                SettingsItem(
                    icon = Icons.Filled.Person,
                    label = "Your name",
                    value = displayName,
                    onClick = { }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Filled.Lock,
                    label = "Password (PIN)",
                    onClick = { }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Filled.Palette,
                    label = "Themes",
                    onClick = { }
                )
            }

            item { SectionHeader("MY DATA") }
            item {
                SettingsItem(
                    icon = Icons.Filled.Save,
                    label = "Backup & Restore",
                    onClick = { }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Filled.Delete,
                    label = "Delete all data",
                    onClick = { }
                )
            }

            item { SectionHeader("REMINDERS") }
            item {
                SettingsItem(
                    icon = Icons.Filled.Notifications,
                    label = "Daily logging reminder",
                    onClick = { }
                )
            }

            item { SectionHeader("OTHER") }
            item {
                SettingsItem(
                    icon = Icons.Filled.Share,
                    label = "Share with Friends",
                    onClick = { }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Filled.Help,
                    label = "Help and Feedback",
                    onClick = { }
                )
            }
            item {
                SettingsItem(
                    icon = Icons.Filled.Star,
                    label = "Rate app",
                    onClick = { }
                )
            }
        }
    }
}

// --- Helper SectionHeader (Tidak berubah, sudah bagus) ---
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary // Warna Soft Lavender
        ),
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

// --- MODIFIKASI 3: Ganti 'Row + border' menjadi 'Card' ---
@Composable
private fun SettingsItem(
    icon: ImageVector,
    label: String,
    value: String? = null,
    onClick: () -> Unit
) {
    // 1. Ganti Row luar dengan Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Aksi klik di Card
        shape = RoundedCornerShape(16.dp), // Sudut aesthetic
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // Shadow soft
        ),
        colors = CardDefaults.cardColors(
            // Warna 'surface' (Warm White) konsisten
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // 2. Row sekarang ada di *dalam* Card
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikon di kiri (tidak berubah)
            Box(
                modifier = Modifier.size(22.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.width(14.dp))

            // Teks Label (tidak berubah)
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            // Teks Value (tidak berubah)
            if (value != null) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
            }

            // Ikon Panah Kanan (tidak berubah)
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}