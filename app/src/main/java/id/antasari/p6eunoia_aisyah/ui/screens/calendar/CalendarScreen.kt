package id.antasari.p6eunoia_aisyah.ui.screens.calendar

// (Import-import lama)
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


// --- TAMBAHKAN IMPORT INI ---
import androidx.compose.ui.text.style.TextOverflow
// ---

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.antasari.p6eunoia_aisyah.data.DiaryEntry
import id.antasari.p6eunoia_aisyah.data.DiaryViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// --- Helper (Tidak berubah) ---
private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
private data class DayCellData(val date: LocalDate, val inCurrentMonth: Boolean)

// --- Fungsi utama UI Kalender (Tidak berubah) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    diaryViewModel: DiaryViewModel,
    onOpenEntry: (Int) -> Unit
) {
    // (Semua logika data & state tidak berubah)
    val allEntries by diaryViewModel.entries.collectAsStateWithLifecycle()
    val diaryByDate by remember(allEntries) {
        derivedStateOf {
            allEntries
                .groupBy { it.timestamp.toLocalDate() }
                .mapValues { (_, entries) -> entries.sortedByDescending { it.timestamp } }
        }
    }
    val today = remember { LocalDate.now() }
    var visibleMonth by remember { mutableStateOf(YearMonth.from(today)) }
    var selectedDate by remember { mutableStateOf(today) }

    val pagePadding = 16.dp
    val gridSpacing = 2.dp
    val cellHeight = 40.dp
    val portraitGridHeight = 260.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Calendar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                // MODIFIKASI: Kita buat TopAppBar transparan agar menyatu
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = pagePadding)
        ) {
            // (Baris Hari, Grid Kalender, Divider tidak berubah)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing)
            ) {
                val daysOfWeek = listOf(
                    DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
                )
                daysOfWeek.forEach { dow ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))

            val cells = remember(visibleMonth) { buildMonthCells(visibleMonth) }
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                verticalArrangement = Arrangement.spacedBy(gridSpacing),
                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(portraitGridHeight),
                userScrollEnabled = false
            ) {
                items(cells) { c ->
                    DayCell(
                        date = c.date,
                        inCurrentMonth = c.inCurrentMonth,
                        selected = c.date == selectedDate,
                        hasDiary = diaryByDate[c.date]?.isNotEmpty() == true,
                        cellHeight = cellHeight,
                        onClick = {
                            selectedDate = c.date
                            visibleMonth = YearMonth.from(c.date)
                        }
                    )
                }
            }

            Spacer(Modifier.height(6.dp))
            Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) // <-- Bikin Divider lebih soft
            Spacer(Modifier.height(16.dp)) // <-- Beri jarak lebih

            // List Jurnal (Tidak berubah)
            DiaryListForDate(
                date = selectedDate,
                entries = diaryByDate[selectedDate].orEmpty(),
                onOpenEntry = onOpenEntry,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// --- Helper buildMonthCells (Tidak berubah) ---
private fun buildMonthCells(ym: YearMonth): List<DayCellData> {
    val firstDay = ym.atDay(1)
    val sundayIndex = firstDay.dayOfWeek.value % 7
    val startDate = firstDay.minusDays(sundayIndex.toLong())

    return (0 until 42).map { i ->
        val d = startDate.plusDays(i.toLong())
        DayCellData(d, d.month == ym.month)
    }
}

// --- UI DayCell (Tidak berubah) ---
@Composable
private fun DayCell(
    date: LocalDate,
    inCurrentMonth: Boolean,
    selected: Boolean,
    hasDiary: Boolean,
    cellHeight: Dp,
    onClick: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val textColor = if (inCurrentMonth) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(cellHeight)
            .clip(RoundedCornerShape(10.dp)) // <-- Kita buat semua sel rounded
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(primaryColor)
            )
        }
        Text(
            text = date.dayOfMonth.toString(),
            color = if (selected) onPrimaryColor else textColor,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        if (hasDiary) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .align(Alignment.BottomCenter)
                    .offset(y = (-6).dp)
                    .clip(CircleShape)
                    .background(if (selected) onPrimaryColor else primaryColor)
            )
        }
    }
}

// --- UI DiaryListForDate (Tidak berubah) ---
@Composable
private fun DiaryListForDate(
    date: LocalDate,
    entries: List<DiaryEntry>,
    onOpenEntry: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (entries.isEmpty()) {
            val label = date.format(DateTimeFormatter.ofPattern("d MMM"))
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tidak ada jurnal pada $label 💭",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp), // <-- Jarak antar kartu
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(entries, key = { it.id }) { e ->
                    DiaryCardItem(e, onOpenEntry)
                }
            }
        }
    }
}

// --- INI DIA PERBAIKANNYA ---
// Ganti seluruh fungsi DiaryCardItem yang lama
@Composable
private fun DiaryCardItem(e: DiaryEntry, onOpenEntry: (Int) -> Unit) {
    val timeStr = Instant.ofEpochMilli(e.timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))

    // 1. Ganti Column luar dengan Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenEntry(e.id) }, // Klik di Card
        shape = RoundedCornerShape(16.dp), // Sudut aesthetic
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // Shadow soft
        ),
        colors = CardDefaults.cardColors(
            // 2. Gunakan warna 'surface' (Warm White), BUKAN 'surfaceVariant'
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // 3. Column sekarang ada di *dalam* Card
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp) // Padding di dalam
        ) {
            Text(
                text = "$timeStr  •  ${e.mood}", // Waktu & Mood
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp)) // Beri jarak

            Text(
                text = e.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // 4. Tambahkan preview konten (biar sama kayak HomeScreen)
            Spacer(Modifier.height(4.dp))
            Text(
                text = e.content.replace("\n", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )
        }
    }
}