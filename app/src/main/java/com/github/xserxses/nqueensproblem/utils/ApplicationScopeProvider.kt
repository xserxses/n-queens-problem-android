package com.github.xserxses.nqueensproblem.utils

import com.github.xserxses.nqueensproblem.app.QueensApplication
import kotlinx.coroutines.CoroutineScope

interface ApplicationScopeProvider {
    val scope: CoroutineScope
}

class ApplicationScopeProviderImpl(
    private val application: QueensApplication
) : ApplicationScopeProvider {
    override val scope: CoroutineScope
        get() = application.applicationScope
}
