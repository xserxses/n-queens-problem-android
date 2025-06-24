package com.github.xserxses.nqueensproblem.app

import android.app.Application
import com.github.xserxses.nqueensproblem.app.di.DaggerQueensAppComponent
import com.github.xserxses.nqueensproblem.app.di.QueensAppComponent

class QueensApplication : Application() {

    lateinit var appComponent: QueensAppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerQueensAppComponent.create()
    }

}