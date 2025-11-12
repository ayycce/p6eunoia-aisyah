package id.antasari.p6eunoia_aisyah.ui.screens // Package-mu

// 1. Tambahkan/Ubah import-import ini
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape // <-- Impor baru
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
// (Kita HAPUS import 'Divider')
import androidx.compose.material3.Card // <-- Impor baru
import androidx.compose.material3.CardDefaults // <-- Impor baru
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow // <-- Impor baru
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p6eunoia_aisyah.R
import id.antasari.p6eunoia_aisyah.data.DiaryEntry
import id.antasari.p6eunoia_aisyah.data.DiaryRepository
import id.antasari.p6eunoia_aisyah.data.EunoiaDatabase
import id.antasari.p6eunoia_aisyah.util.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale // <-- Impor baru
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String?,
    onOpenEntry: (Int) -> Unit
) {
    // (Logika data & state tidak berubah, biarkan saja)
    val context = LocalContext.current
    val db = remember { EunoiaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    var entries by remember { mutableStateOf<List<DiaryEntry>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val current = repo.allEntries()
            if (current.isEmpty()) {
                val sample = DiaryEntry(
                    id = 0,
                    title = "Jurnal Pertamaku ✨",
                    content = "Apa yang aku syukuri hari ini?\nSiapa yang membuat hariku lebih baik?",
                    mood = "😊",
                    timestamp = System.currentTimeMillis()
                )

                repo.addEntry(sample.title, sample.content, sample.mood, sample.timestamp)
            }
            entries = repo.allEntries().sortedByDescending { it.timestamp }
        }
    }

    val filteredEntries = remember(entries, searchQuery) {
        val q = searchQuery.trim()
        if (q.isBlank()) entries
        else entries.filter { e ->
            e.title.contains(q, ignoreCase = true) ||
                    e.content.contains(q, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Eunoia",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSearching = !isSearching
                            if (!isSearching) searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                // Kita buat TopAppBar transparan agar menyatu!
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            // --- INI MODIFIKASINYA ---
            // Beri padding di kanan-kiri list
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            // Beri jarak 12.dp antar setiap kartu
            verticalArrangement = Arrangement.spacedBy(12.dp)
            // --- AKHIR MODIFIKASI ---

        ) {
            // Search bar (biarkan)
            if (isSearching) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Cari di dalam jurnalmu...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            // Hapus padding horizontal, karena sudah ada di LazyColumn
                            .padding(vertical = 8.dp),
                        singleLine = true
                    )
                }
            }

            // Banner Image (biarkan)
            item {
                Image(
                    painter = painterResource(id = R.drawable.banner_diary),
                    contentDescription = "Eunoia banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        // Hapus padding, biarkan mepet ke padding LazyColumn
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
                )
            }

            // List Diary (biarkan)
            items(filteredEntries, key = { it.id }) { entry ->
                DiaryListItem(
                    entry = entry,
                    onClick = { onOpenEntry(entry.id) }
                )
            }
        }
    }
}

// --- INI ADALAH MODIFIKASI BESARNYA ---
// Kita ganti total fungsi DiaryListItem
@Composable
private fun DiaryListItem(
    entry: DiaryEntry,
    onClick: () -> Unit
) {
    // Format tanggal
    val localDateTime = Instant.ofEpochMilli(entry.timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    val formatterDay = DateTimeFormatter.ofPattern("dd")
    val formatterMonth = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH) // Biar "NOV"

    // 1. Bungkus semuanya dengan CARD
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Aksi klik ada di Card
        shape = RoundedCornerShape(16.dp), // Sudut lebih bulat (aesthetic)
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // Bayangan soft
        ),
        colors = CardDefaults.cardColors(
            // Gunakan warna 'surface' hangat dari EunoiaTheme
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // 2. Row untuk menampung [Blok Tanggal] dan [Blok Konten]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Beri padding di dalam kartu
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically // <-- Penting!
        ) {
            // Kolom tanggal kiri (Blok Tanggal)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp) // Beri lebar tetap
            ) {
                Text(
                    text = localDateTime.format(formatterDay),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Warna Soft Lavender
                )
                Text(
                    text = localDateTime.format(formatterMonth).uppercase(), // "NOV"
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Abu-abu
                )
            }

            // Kolom konten kanan (Blok Konten)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp) // Jarak dari blok tanggal
            ) {
                // Baris 1: Judul (dengan mood)
                Text(
                    text = "${entry.mood} ${entry.title}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // Jika panjang, beri "..."
                    color = MaterialTheme.colorScheme.onSurface // Teks utama
                )

                Spacer(Modifier.height(4.dp))

                // Baris 2: Cuplikan Konten
                Text(
                    text = entry.content.replace("\n", " "), // Ganti baris baru dgn spasi
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // Abu-abu
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp // Jarak antar baris (user friendly)
                )

                Spacer(Modifier.height(8.dp))

                // Baris 3: Waktu
                Text(
                    text = formatTimestamp(entry.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
    // 3. TIDAK ADA LAGI Divider!
}