package com.github.xserxses.nqueensproblem.app.di

import com.github.xserxses.nqueensproblem.app.QueensApplication
import com.github.xserxses.nqueensproblem.main.di.MainComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        QueensSubcomponentsModule::class,
        QueensModule::class
    ]
)
interface QueensAppComponent {
    fun mainComponent(): MainComponent.Factory

    fun inject(app: QueensApplication)
}
