package com.sp.uxpulse.di

import com.sp.uxpulse.analytics.UxPulseTracker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LibAppModule::class])
interface LibAppComponent {

    fun inject(tracker: UxPulseTracker)
}