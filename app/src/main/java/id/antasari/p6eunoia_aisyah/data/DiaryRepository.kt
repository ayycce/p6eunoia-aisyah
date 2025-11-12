package id.antasari.p6eunoia_aisyah.data

import kotlinx.coroutines.flow.Flow //

/**
 * Repository (Juru Masak)
 * Ini adalah lapisan yang dipakai oleh UI (Screen)
 * untuk "meminta" data, tanpa perlu tahu rumitnya DAO.
 */
class DiaryRepository(private val dao: DiaryDao) { //

    // (Untuk Langkah 1 & 2)
    // Ambil SEMUA entry, tapi hanya SEKALI. (Dipakai di HomeScreen lama/Test)
    suspend fun allEntries(): List<DiaryEntry> = dao.getAll() //

    // (Untuk Langkah 2.2)
    // Ambil SATU entry by ID. Ini HARUS 'suspend fun'
    // agar cocok dengan NoteDetailScreen.kt
    suspend fun getEntryById(id: Int): DiaryEntry? = dao.getById(id) //

    // (Untuk Langkah 1.7 & 2.2)
    // Hapus satu entry.
    suspend fun remove(entry: DiaryEntry) = dao.delete(entry) //

    // (Untuk Langkah 2.2 & 3)
    // Edit satu entry.
    suspend fun edit(entry: DiaryEntry) = dao.update(entry) //

    // (Untuk Langkah 1.7)
    // Helper untuk menambah entry baru (dipakai di TestRoomScreen)
    suspend fun addEntry(
        title: String,
        content: String,
        mood: String,
        timestamp: Long // <-- 1. TAMBAHKAN PARAMETER INI
    ) {
        val newEntry = DiaryEntry(
            title = title,
            content = content,
            mood = mood,
            timestamp = timestamp // <-- 2. GUNAKAN TIMESTAMP DARI PARAMETER
        )
        dao.insert(newEntry)
    }

    // (Untuk Langkah 9 - Nanti)
    // Ambil SEMUA entry, tapi sebagai 'Flow' (mengalir/reaktif)
    // Ini yang akan kita pakai di Kalender nanti.
    fun allEntriesFlow(): Flow<List<DiaryEntry>> = dao.observeAll()
}