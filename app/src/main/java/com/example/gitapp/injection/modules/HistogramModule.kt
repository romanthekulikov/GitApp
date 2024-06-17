package com.example.gitapp.injection.modules

import com.example.domain.domain.use_cases.diagram.AdaptPeriodUseCase
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import dagger.Module
import dagger.Provides

@Module
class HistogramModule {
    @Provides
    fun getHistogramPeriodAdapter(): AdaptPeriodUseCase {
        return AdaptPeriodUseCase()
    }




    @Provides
    fun getPeriodHelper(): ConvertPeriodUseCase {
        return ConvertPeriodUseCase()
    }
}