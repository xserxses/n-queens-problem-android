package com.github.xserxses.nqueensproblem.welcome.home

import androidx.lifecycle.ViewModel
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import com.github.xserxses.nqueensproblem.welcome.home.model.WelcomeHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class WelcomeHomeViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _state: MutableStateFlow<WelcomeHomeState> =
        MutableStateFlow(WelcomeHomeState(false))
    val state = _state.asStateFlow()

    init {
        _state.value = WelcomeHomeState(repository.isSavedGameAvailable())
    }
}
