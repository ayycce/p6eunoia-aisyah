package id.antasari.p6eunoia_aisyah.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// PASTIKAN ANOTASI INI ADA DAN NAMA TABELNYA BENAR
@Entity(tableName = "diary_entries") //
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) //
    val id: Int = 0,
    val title: String, //
    val content: String, //
    val mood: String, //
    val timestamp: Long //
)