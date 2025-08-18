package com.github.xserxses.nqueensproblem.welcome.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.xserxses.nqueensproblem.persistance.GameRepository
import com.github.xserxses.nqueensproblem.welcome.home.model.WelcomeHomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class WelcomeHomeViewModel @Inject constructor(
    repository: GameRepository
) : ViewModel() {

    private val _state: MutableStateFlow<WelcomeHomeState> =
        MutableStateFlow(WelcomeHomeState(false))
    val state = _state.asStateFlow()

    init {
        repository
            .isSavedGameAvailable()
            .onEach { isAvailable ->
                _state.value = WelcomeHomeState(isAvailable)
            }
            .launchIn(viewModelScope)
    }
}
