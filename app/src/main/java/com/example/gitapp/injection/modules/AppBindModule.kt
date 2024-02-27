package com.example.gitapp.injection.modules

import com.example.gitapp.data.repository.Repository
import com.example.gitapp.data.repository.RepositoryImpl
import com.example.gitapp.ui.diagram.utils.HistogramPeriodAdapter
import com.example.gitapp.ui.diagram.utils.PeriodHelper
import com.example.gitapp.ui.diagram.utils.implementation.HistogramPeriodAdapterImpl
import com.example.gitapp.ui.diagram.utils.implementation.PeriodHelperImpl
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

    @Binds
    @Singleton
    fun bindRepository(repository: RepositoryImpl): Repository
}