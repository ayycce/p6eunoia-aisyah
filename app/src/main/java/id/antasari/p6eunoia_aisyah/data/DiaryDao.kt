package id.antasari.p6eunoia_aisyah.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    // --- Untuk Langkah 1.5 ---
    // (Mengambil list data satu kali)
    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC") // [cite: 272, 560]
    suspend fun getAll(): List<DiaryEntry>

    // --- Ini yang kita tambahkan di Langkah 2.1 ---
    // (HARUS suspend fun, BUKAN Flow, agar cocok dengan NoteDetailScreen)
    @Query("SELECT * FROM diary_entries WHERE id = :entryId LIMIT 1") // [cite: 561]
    suspend fun getById(entryId: Int): DiaryEntry? // [cite: 562]

    // --- Untuk CRUD standar ---
    @Insert(onConflict = OnConflictStrategy.REPLACE) // [cite: 267, 554]
    suspend fun insert(entry: DiaryEntry)

    @Update // [cite: 269, 556]
    suspend fun update(entry: DiaryEntry)

    @Delete // [cite: 271, 558]
    suspend fun delete(entry: DiaryEntry)

    // --- BONUS: Ini untuk NANTI di Langkah 9 (Kalender) ---
    // (Kita siapkan saja dari sekarang, ini mirip dengan fun getAllEntries() di kodemu)
    @Query("SELECT * FROM diary_entries ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<DiaryEntry>> //
}