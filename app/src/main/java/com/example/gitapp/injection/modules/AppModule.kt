package com.example.gitapp.injection.modules

import dagger.Module

@Module(includes = [AppBindModule::class, ApiModule::class])
object AppModule

