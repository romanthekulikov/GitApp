package com.example.gitapp.injection.modules

import com.example.domain.domain.HistogramPeriodAdapter
import com.example.domain.domain.PeriodHelper
import dagger.Module
import dagger.Provides

@Module
class HistogramModule {
    @Provides
    fun getHistogramPeriodAdapter(): HistogramPeriodAdapter {
        return HistogramPeriodAdapter()
    }

    @Provides
    fun getPeriodHelper(): PeriodHelper {
        return PeriodHelper()
    }
}