package id.antasari.p6eunoia_aisyah.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import id.antasari.p6eunoia_aisyah.ui.navigation.Routes // Import Routes

// Data class untuk item navigasi
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

// Daftar tab utama kita
private val bottomItems = listOf(
    BottomNavItem(Routes.HOME, "Home", Icons.Filled.Home),
    BottomNavItem("calendar", "Calendar", Icons.Filled.CalendarMonth), // Akan kita buat
    BottomNavItem("insights", "Insights", Icons.Filled.BarChart), // Akan kita buat
    BottomNavItem("settings", "Settings", Icons.Filled.Settings)  // Akan kita buat
)

// Composable untuk BottomNav kustom (Langkah 8.2)
@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface), // Warna dari EunoiaTheme
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logika untuk membagi 4 tab di sekitar FAB tengah
        val homeItem = bottomItems[0]
        val calendarItem = bottomItems[1]
        val insightsItem = bottomItems[2]
        val settingsItem = bottomItems[3]

        // Home
        BottomNavButton(
            item = homeItem,
            selected = currentRoute == homeItem.route,
            onClick = { navController.navigate(homeItem.route) },
            modifier = Modifier.weight(1f).padding(start = 10.dp)
        )
        // Calendar
        BottomNavButton(
            item = calendarItem,
            selected = currentRoute == calendarItem.route,
            onClick = { navController.navigate(calendarItem.route) },
            modifier = Modifier.weight(1f).padding(start = 10.dp)
        )

        // Slot Kosong untuk FAB
        Spacer(modifier = Modifier.weight(1f))

        // Insights
        BottomNavButton(
            item = insightsItem,
            selected = currentRoute == insightsItem.route,
            onClick = { navController.navigate(insightsItem.route) },
            modifier = Modifier.weight(1f).padding(end = 10.dp)
        )
        // Settings
        BottomNavButton(
            item = settingsItem,
            selected = currentRoute == settingsItem.route,
            onClick = { navController.navigate(settingsItem.route) },
            modifier = Modifier.weight(1f).padding(end = 10.dp)
        )
    }
}

// Composable untuk Tombol Tab (Langkah 8.2)
@Composable
private fun BottomNavButton(
    item: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    // Warna aktif akan mengambil warna Primary (Soft Lavender) dari EunoiaTheme
    val activeColor = colors.primary
    val inactiveColor = colors.onSurfaceVariant // Warna abu-abu

    val iconColor = if (selected) activeColor else inactiveColor
    val textColor = if (selected) activeColor else inactiveColor

    Box(
        modifier = modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = iconColor
            )
            Spacer(modifier = Modifier.height(0.dp)) // Jarak ikon & teks
            Text(
                text = item.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = textColor
            )
        }
    }
}