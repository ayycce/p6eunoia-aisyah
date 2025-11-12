package id.antasari.p6eunoia_aisyah.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.antasari.p6eunoia_aisyah.data.DiaryEntry
import id.antasari.p6eunoia_aisyah.data.DiaryRepository
import id.antasari.p6eunoia_aisyah.data.EunoiaDatabase
import id.antasari.p6eunoia_aisyah.util.formatTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    entryId: Int,
    onBack: () -> Unit,
    onDeleted: () -> Unit,
    onEdit: (Int) -> Unit
) {
    // --- (Blok Logika & State tidak berubah) ---
    val context = LocalContext.current
    val db = remember { EunoiaDatabase.getInstance(context) }
    val repo = remember { DiaryRepository(db.diaryDao()) }
    var entry by remember { mutableStateOf<DiaryEntry?>(null) }

    var showMenu by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val iconColor = MaterialTheme.colorScheme.onSurfaceVariant

    LaunchedEffect(entryId) {
        withContext(Dispatchers.IO) {
            entry = repo.getEntryById(entryId)
        }
    }
    // --- (Akhir Blok Logika) ---

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Entry", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = iconColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(entryId) }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = iconColor
                        )
                    }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "More",
                                tint = iconColor
                            )
                        }
                        // --- INI DIA PERBAIKANNYA ---
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    showConfirmDelete = true
                                },
                                // Change DropdownMenuItemDefaults to MenuDefaults
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.error
                                )
                            )
                        }
                        // --- AKHIR PERBAIKAN ---
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { innerPadding ->
        // ... (Isi Column tidak berubah)
        if (entry == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        entry?.let { e ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = e.mood,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(16.dp))

                Text(
                    text = e.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    text = formatTimestamp(e.timestamp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Text(
                        text = e.content,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(all = 20.dp),
                        lineHeight = 26.sp
                    )
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

    // --- (Dialog Konfirmasi Hapus tidak berubah) ---
    if (showConfirmDelete && entry != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            title = { Text("Hapus Jurnal?") },
            text = { Text("Jurnal yang dihapus tidak bisa dikembalikan lagi. Yakin?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val entryToDelete = entry
                        if (entryToDelete != null) {
                            scope.launch(Dispatchers.IO) {
                                repo.remove(entryToDelete)
                                withContext(Dispatchers.Main) {
                                    showConfirmDelete = false
                                    onDeleted()
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        "Hapus",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
