package com.example.gitapp.injection.modules

import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import dagger.Module
import dagger.Provides

@Module
object PeriodModule {
    @Provides
    fun getWeek(): Week = Week()

    @Provides
    fun getMonth(): Month = Month()

    @Provides
    fun getYear(): Year = Year()
}