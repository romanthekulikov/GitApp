package com.example.gitapp.injection.modules

import dagger.Module

@Module(includes = [AppBindModule::class, PeriodModule::class, ApiModule::class])
object AppModule

