package id.antasari.p6eunoia_aisyah.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.antasari.p6eunoia_aisyah.data.DiaryEntry
import id.antasari.p6eunoia_aisyah.data.DiaryViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

// --- (Helper-helper di atas sini tidak berubah) ---

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

@Composable
private fun moodColor(mood: String): Color {
    return when (mood.lowercase()) {
        "😊", "happy" -> Color(0xFFFFECB3) // Soft Pink
        "✨", "calm" -> Color(0xFFA2D5C6) // Soft Mint
        "😔", "sad" -> Color(0xFFBCA4D3) // Soft Lavender
        "😠", "angry" -> Color(0xFFF06292) // Soft Peach
        "😴", "tired" -> Color(0xFF9E9692) // Warm Grey
        "💖", "love" -> Color(0xFFF48FB1)
        else -> MaterialTheme.colorScheme.primary
    }
}

private fun moodLabel(mood: String): String {
    return when (mood.lowercase()) {
        "😊" -> "Happy"
        "✨" -> "Calm"
        "😔" -> "Sad"
        "😠" -> "Angry"
        "😴" -> "Tired"
        "💖" -> "love"
        else -> mood.replaceFirstChar { it.titlecase() }
    }
}

// --- UI UTAMA INSIGHTS SCREEN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    diaryViewModel: DiaryViewModel
) {
    // (Logika data & state tidak berubah)
    val allEntries by diaryViewModel.entries.collectAsStateWithLifecycle()
    val totalEntries = allEntries.size
    val distinctMoods = allEntries.map { it.mood }.toSet().size
    val datesSet = remember(allEntries) { allEntries.map { it.timestamp.toLocalDate() }.toSet() }
    val currentStreak = remember(datesSet) { calcCurrentStreak(datesSet) }
    val longestStreak = remember(datesSet) { calcLongestStreak(datesSet) }
    val moodCounts = remember(allEntries) {
        allEntries.groupingBy { it.mood }.eachCount()
    }
    val totalForPercent = moodCounts.values.sum().toFloat().coerceAtLeast(1.0f)
    val trendData = moodCounts.entries
        .sortedByDescending { it.value }
        .map { (mood, count) ->
            Triple(mood, count / totalForPercent, count)
        }
    val today = LocalDate.now()
    val last7Days = remember(today) { (0..6).map { today.minusDays(it.toLong()) } }
    val hasEntryOn = remember(datesSet) { last7Days.associateWith { it in datesSet } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Insights",
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
                .padding(horizontal = 16.dp), // Pindahkan padding horizontal ke sini
            verticalArrangement = Arrangement.spacedBy(16.dp), // Jarak antar kartu
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // --- Kartu Summary (Total, Moods, Streak) ---
            item {
                // --- MODIFIKASI 2: Ganti 'BorderedCard' jadi 'InsightCard' ---
                InsightCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryColumn(number = totalEntries, label = "Entries")
                        SummaryColumn(number = distinctMoods, label = "Moods")
                        SummaryColumn(number = longestStreak, label = "Best Streak")
                    }
                }
            }

            // --- Kartu 7-Day Streak ---
            item {
                InsightCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Diary Streak",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            last7Days.reversed().forEach { date ->
                                DayCheck(
                                    date = date,
                                    checked = hasEntryOn[date] == true,
                                    size = 36.dp
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔥 ", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = "Current Streak: ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$currentStreak days",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // --- Kartu Mood Trends (Donut Chart) ---
            item {
                InsightCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Mood Trends",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))

                        if (trendData.isEmpty()) {
                            Text(
                                text = "No mood data yet...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val donutFractions = trendData.map { it.second }
                                    val donutColors = trendData.map { moodColor(it.first) }
                                    DonutChart(
                                        fractions = donutFractions,
                                        colors = donutColors,
                                        size = 92.dp,
                                        thickness = 22.dp
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1.5f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    trendData.forEach { (mood, frac, _) ->
                                        LegendRowTight(
                                            dotColor = moodColor(mood),
                                            label = moodLabel(mood),
                                            percent = frac * 100
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// --- MODIFIKASI 2: Ganti 'BorderedCard' jadi 'InsightCard' ---
@Composable
private fun InsightCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // Bentuk dan shadow kita samakan dengan HomeScreen
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Warna 'surface' (Warm White) agar konsisten
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        content = content
    )
}

// --- (SummaryColumn tidak berubah) ---
@Composable
private fun SummaryColumn(number: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 60.dp)
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// --- (DayCheck kita modifikasi) ---
@Composable
private fun DayCheck(date: LocalDate, checked: Boolean, size: Dp) {
    val dayFmt = remember { DateTimeFormatter.ofPattern("E") } // "Mon"
    val dateFmt = remember { DateTimeFormatter.ofPattern("d") } // "27"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
                // --- MODIFIKASI 3: Ganti warna background ---
                .background(
                    if (checked) MaterialTheme.colorScheme.primary // Soft Lavender
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) // Faint Lavender
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "checked",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = date.format(dateFmt),
                    style = MaterialTheme.typography.titleMedium,
                    // Ganti warna teksnya juga
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = date.format(dayFmt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// --- (DonutChart tidak berubah) ---
@Composable
private fun DonutChart(
    fractions: List<Float>,
    colors: List<Color>,
    size: Dp,
    thickness: Dp
) {
    val normalized = if (fractions.isEmpty()) listOf(1f) else fractions
    val total = normalized.sum()
    val parts = normalized.map { (it / total) }
    val failColor = MaterialTheme.colorScheme.primary
    var startAngle = -90f

    Canvas(modifier = Modifier.size(size)) {
        parts.forEachIndexed { i, part ->
            val color = colors.getOrElse(i) { failColor }
            val sweepAngle = part * 360f
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = thickness.toPx(), cap = StrokeCap.Round)
            )
            startAngle += sweepAngle
        }
    }
}

// --- (LegendRowTight tidak berubah) ---
@Composable
private fun LegendRowTight(dotColor: Color, label: String, percent: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "${percent.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// --- (Fungsi Logika Streak tidak berubah) ---
private fun calcCurrentStreak(dates: Set<LocalDate>): Int {
    if (dates.isEmpty()) return 0
    var streak = 0
    var cursor = LocalDate.now()
    if (cursor in dates || cursor.minusDays(1) in dates) {
        if (cursor in dates) {
            streak = 1
            cursor = cursor.minusDays(1)
        }
        while (cursor in dates) {
            streak++
            cursor = cursor.minusDays(1)
        }
    }
    return streak
}

private fun calcLongestStreak(dates: Set<LocalDate>): Int {
    if (dates.isEmpty()) return 0
    val sorted = dates.sorted()
    var best = 0
    var current = 0
    (0 until sorted.size).forEach { i ->
        if (i == 0 || sorted[i].isEqual(sorted[i - 1].plusDays(1))) {
            current++
        } else {
            current = 1
        }
        best = maxOf(best, current)
    }
    return best
}