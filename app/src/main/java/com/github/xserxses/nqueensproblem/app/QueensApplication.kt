package com.github.xserxses.nqueensproblem.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class QueensApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
