package com.github.xserxses.nqueensproblem.welcome.home.di

import com.github.xserxses.nqueensproblem.welcome.home.WelcomeHomeFragment
import dagger.Subcomponent

@Subcomponent(modules = [WelcomeHomeModule::class])
interface WelcomeHomeComponent {
    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): WelcomeHomeComponent
    }

    fun inject(fragment: WelcomeHomeFragment)
}
