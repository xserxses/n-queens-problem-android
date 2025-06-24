package com.github.xserxses.nqueensproblem.main.di

import com.github.xserxses.nqueensproblem.main.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [MainModule::class])
interface MainComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainComponent
    }

    fun inject(activity: MainActivity)
}