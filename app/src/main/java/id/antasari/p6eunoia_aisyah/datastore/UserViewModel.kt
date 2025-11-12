package id.antasari.p6eunoia_aisyah.datastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserPrefsRepository) : ViewModel() {

    val userName: StateFlow<String> = repo.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    val onboardingDone: StateFlow<Boolean> = repo.onboardingDoneFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun saveName(name: String) {
        viewModelScope.launch {
            repo.saveUserName(name)
        }
    }

    fun finishOnboarding() {
        viewModelScope.launch {
            repo.setOnboardingDone(true)
        }
    }
}
