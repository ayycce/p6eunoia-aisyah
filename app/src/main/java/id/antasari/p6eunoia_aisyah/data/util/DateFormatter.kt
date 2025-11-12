package id.antasari.p6eunoia_aisyah.util // <-- Package name kita

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Helper untuk mengubah Long timestamp (System.currentTimeMillis())
 * menjadi format tanggal yang bisa dibaca.
 * [cite_start]Sesuai Langkah 1.8 [cite: 335]
 */
fun formatTimestamp(ts: Long): String {
    // Format: "27 Okt, 2025, 05:50 PM"
    val sdf = SimpleDateFormat("dd MMM, yyyy, hh:mma", Locale("id", "ID")) // [cite: 342]
    return sdf.format(Date(ts)) // [cite: 343]
}