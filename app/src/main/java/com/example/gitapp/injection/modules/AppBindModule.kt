package com.example.gitapp.injection.modules

import com.example.gitapp.utils.HistogramPeriodAdapter
import com.example.gitapp.utils.PeriodHelper
import com.example.gitapp.utils.implementation.HistogramPeriodAdapterImpl
import com.example.gitapp.utils.implementation.PeriodHelperImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppBindModule {
    @Binds
    @Singleton
    fun bindHistogramPeriodAdapter(histogramPeriodAdapterImpl: HistogramPeriodAdapterImpl): HistogramPeriodAdapter

    @Binds
    @Singleton
    fun bindPeriodHelper(periodHelperImpl: PeriodHelperImpl): PeriodHelper
}