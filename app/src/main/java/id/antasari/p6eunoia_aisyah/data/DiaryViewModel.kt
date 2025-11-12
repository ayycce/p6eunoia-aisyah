package id.antasari.p6eunoia_aisyah.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DiaryViewModel(private val repo: DiaryRepository) : ViewModel() {

    // 1. PERBAIKAN NAMA FUNGSI
    // Kita panggil 'allEntriesFlow()' yang baru dari repository
    val entries: StateFlow<List<DiaryEntry>> = repo.allEntriesFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 2. PERBAIKAN NAMA FUNGSI
    // Kita panggil 'addEntry' dari repository,
    // repository yang akan mengurus pembuatan objek DiaryEntry
    fun addEntry(title: String, content: String, mood: String = "🌸") {
        viewModelScope.launch {
            repo.addEntry(
                title = title,
                content = content,
                mood = mood,
                timestamp = System.currentTimeMillis() // <-- TAMBAHKAN PARAMETER INI
            )
        }
    }

    // 3. PERBAIKAN NAMA FUNGSI
    // Kita panggil 'remove' dari repository
    fun deleteEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            repo.remove(entry)
        }
    }

    // 4. PERBAIKAN NAMA FUNGSI
    // Kita panggil 'edit' dari repository
    fun updateEntry(entry: DiaryEntry) {
        viewModelScope.launch {
            repo.edit(entry)
        }
    }

}