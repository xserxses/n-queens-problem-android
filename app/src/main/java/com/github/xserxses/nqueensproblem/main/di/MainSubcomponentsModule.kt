package com.github.xserxses.nqueensproblem.main.di

import com.github.xserxses.nqueensproblem.welcome.home.di.WelcomeHomeComponent
import dagger.Module

@Module(subcomponents = [WelcomeHomeComponent::class])
class MainSubcomponentsModule
