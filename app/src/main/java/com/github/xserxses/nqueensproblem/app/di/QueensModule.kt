package com.github.xserxses.nqueensproblem.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.xserxses.nqueensproblem.app.QueensApplication
import com.github.xserxses.nqueensproblem.utils.ApplicationScopeProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
class QueensModule {

    @Provides
    fun provideApplicationScopeProvider(
        application: Application
    ) = ApplicationScopeProviderImpl(application as QueensApplication)

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    @Provides
    fun provideJson() =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    private companion object {
        const val SHARED_PREFERENCES_NAME = "app_shared_prefs"
    }
}
