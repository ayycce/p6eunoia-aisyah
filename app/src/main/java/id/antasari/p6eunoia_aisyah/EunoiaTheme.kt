package id.antasari.p6eunoia_aisyah // [cite: 347]

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import id.antasari.p6eunoia_aisyah.ui.theme.EunoiaTypography

// 🌸✨ Palet Warna Eunoia (Soft & Aesthetic) ✨🌸
private val EunoiaLightColorScheme = lightColorScheme(
    // Warna Aksen Utama (Tombol, Tanggal Aktif, FAB)
    // Kita pakai warna ungu lavender yang soft
    primary = Color(0xFFBCA4D3), // Soft Lavender

    // Warna Aksen Sekunder (Filter chips, dll)
    // Kita pakai warna soft peach/coral
    secondary = Color(0xFFFAB9B0), // Soft Peach

    // Warna Aksen Tersier (Highlight, dll)
    // Kita pakai warna teal/mint yang menenangkan
    tertiary = Color(0xFFA2D5C6), // Soft Mint

    // Warna Latar Belakang Utama (di belakang semua layar)
    // Warna krem/ivory yang sangat lembut
    background = Color(0xFFFFFBF8), // Warm Ivory

    // Warna Permukaan (Kartu, Dialog, TopBar)
    // Sedikit lebih putih dari background, tapi tetap hangat
    surface = Color(0xFFFFFDFC), // Clean White (Warm)

    // --- Warna Teks & Ikon ---
    // Teks di atas warna Primary (e.g., di dalam Tombol)
    onPrimary = Color(0xFF4A3A60), // Dark Purple (Good contrast)

    // Teks di atas warna Secondary
    onSecondary = Color(0xFF633B36), // Dark Brown

    // Teks di atas warna Tertiary
    onTertiary = Color(0xFF2E5B50), // Dark Green/Teal

    // Teks utama (paling gelap) di atas Background/Surface
    onSurface = Color(0xFF5E5855), // Warm Dark Grey

    // Teks sekunder (abu-abu) di atas Background/Surface
    onSurfaceVariant = Color(0xFF9E9692), // Warm Medium Grey

    // Garis pemisah, border, dll
    outline = Color(0xFFE0D9D5) // Soft Grey Beige
)

/*
    Catatan: Kita tidak mendefinisikan darkColorScheme()
    karena aplikasi jurnal ini kita fokuskan di mode terang
    agar nuansa "soft"-nya konsisten.
*/

@Composable
fun EunoiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Biarkan false untuk mode terang
    content: @Composable () -> Unit
) {
    // Kita paksa menggunakan skema warna terang (EunoiaLightColorScheme)
    val colorScheme = EunoiaLightColorScheme

    // Mengatur warna status bar (bar di paling atas HP)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Samakan dgn background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EunoiaTypography, // Kita pakai font default dulu
        content = content
    )
}