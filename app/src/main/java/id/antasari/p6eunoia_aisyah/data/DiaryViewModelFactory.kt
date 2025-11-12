package id.antasari.p6eunoia_aisyah.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DiaryViewModelFactory(private val repo: DiaryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
