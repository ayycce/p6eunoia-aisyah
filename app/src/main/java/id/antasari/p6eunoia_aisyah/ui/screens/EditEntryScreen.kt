package id.antasari.p6eunoia_aisyah.ui.screens

import androidx.compose.foundation.clickable // <-- Impor baru
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.antasari.p6eunoia_aisyah.data.DiaryEntry
import id.antasari.p6eunoia_aisyah.data.DiaryRepository
import id.antasari.p6eunoia_aisyah.data.EunoiaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
// --- Impor-impor BARU untuk Tanggal & Waktu ---
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
// ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntryScreen(
    entryId: Int,
    onBack: () -> Unit,
    onSaved: (Int) -> Unit
) {
    val context = LocalContext.current
    val db = remember { EunoiaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    val scope = rememberCoroutineScope()

    // --- State untuk Form ---
    var entry by remember { mutableStateOf<DiaryEntry?>(null) }
    var titleText by remember { mutableStateOf("") }
    var contentText by remember { mutableStateOf("") }
    val moodOptions = remember {
        listOf(
            "😊" to "Happy", "✨" to "Calm", "😔" to "Sad",
            "😠" to "Angry", "😴" to "Tired", "💖" to "Love"
        )
    }
    var selectedMood by remember { mutableStateOf("😊") }

    // --- State untuk Tanggal & Waktu ---
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Inisialisasi state picker (akan di-update di LaunchedEffect)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = false
    )

    // Formatter
    val dateFormatter = remember { DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", Locale.getDefault()) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH) }


    // --- 1. Ambil data LAMA dari database ---
    LaunchedEffect(entryId) {
        withContext(Dispatchers.IO) {
            val loadedEntry = repo.getEntryById(entryId)
            entry = loadedEntry

            if (loadedEntry != null) {
                // Isi field dengan data lama
                titleText = loadedEntry.title
                contentText = loadedEntry.content
                selectedMood = loadedEntry.mood

                // --- INI BAGIAN PENTINGNYA ---
                // Konversi Long timestamp LAMA ke ZonedDateTime
                val zdt = Instant.ofEpochMilli(loadedEntry.timestamp)
                    .atZone(ZoneId.systemDefault())

                // Set state tanggal & waktu ke data LAMA
                selectedDate = zdt.toLocalDate()
                selectedTime = zdt.toLocalTime()

                // Update juga state M3 Pickers
                datePickerState.selectedDateMillis = loadedEntry.timestamp
                timePickerState.hour = selectedTime.hour
                timePickerState.minute = selectedTime.minute
                // ---
            }
        }
    }

    // --- Fungsi Helper ---
    fun combineDateTimeToMillis(): Long {
        return selectedDate.atTime(selectedTime)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun onSaveEntry() {
        val currentEntry = entry
        if (currentEntry != null) {
            // Buat objek baru dengan data yang sudah di-update
            val updatedEntry = currentEntry.copy(
                title = titleText,
                content = contentText,
                mood = selectedMood,
                timestamp = combineDateTimeToMillis() // Pakai timestamp baru
            )

            scope.launch(Dispatchers.IO) {
                repo.edit(updatedEntry) // Panggil 'edit'
                withContext(Dispatchers.Main) {
                    onSaved(currentEntry.id) // Kembali ke Detail Screen
                }
            }
        }
    }
    // --- Akhir Fungsi Helper ---


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Entry ✏️", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { onSaveEntry() },
                        enabled = titleText.isNotBlank() && contentText.isNotBlank(),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Done")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Tampilan Tanggal & Waktu (Sama)
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedDate.format(dateFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { showDatePicker = true }
                )
                Text( " • ", style = MaterialTheme.typography.titleMedium )
                Text(
                    text = selectedTime.format(timeFormatter),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { showTimePicker = true }
                )
            }

            // Field Judul & Konten (Sama)
            OutlinedTextField(
                value = titleText,
                onValueChange = { titleText = it },
                label = { Text("Title / Headline") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = contentText,
                onValueChange = { contentText = it },
                label = { Text("What's on your mind?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 5
            )
            Spacer(Modifier.height(16.dp))

            // Mood Picker (Sama)
            Text(
                text = "Mood",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            val row1 = remember { moodOptions.take(3) }
            val row2 = remember { moodOptions.drop(3) }
            MoodRow(row1, selectedMood) { selectedMood = it }
            Spacer(Modifier.height(8.dp))
            MoodRow(row2, selectedMood) { selectedMood = it }
        }

        // --- Dialog-dialog Picker (Sama) ---

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        }
                        showDatePicker = false
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                modifier = Modifier.fillMaxWidth(),
                confirmButton = {
                    TextButton(onClick = {
                        selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }) { Text("Save") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
                },
                text = {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            )
        }
    }
}

// --- Helper MoodRow (Tetap sama) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoodRow(
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { (emoji, label) ->
            val color = moodColor(emoji)
            FilterChip(
                selected = (selected == emoji),
                onClick = { onSelect(emoji) },
                label = { Text("$emoji $label") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color.copy(alpha = 0.20f),
                    selectedLabelColor = color
                )
            )
        }
    }
}

// --- Helper moodColor (Tetap sama) ---
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