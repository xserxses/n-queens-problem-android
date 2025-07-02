package com.github.xserxses.nqueensproblem.welcome.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class WelcomeHomeViewModel @Inject constructor() : ViewModel() {

    @OptIn(ExperimentalUuidApi::class)
    val user = Uuid.random()

}