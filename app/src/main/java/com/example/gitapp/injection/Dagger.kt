package com.example.gitapp.injection

import com.example.gitapp.data.api.models.ApiOwner
import com.example.gitapp.ui.diagram.models.Week
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
}

@Module
object AppModule {
}

