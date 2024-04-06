package com.example.gitapp.injection.modules

import com.example.data.data.repository.Repository
import com.example.data.data.repository.RepositoryImpl
import com.example.domain.domain.entity.Repo
import com.example.gitapp.ui.service_repo.NotificationsCreator
import com.example.domain.domain.HistogramPeriodAdapter
import com.example.domain.domain.PeriodHelper
import com.example.domain.domain.HistogramPeriodAdapterImpl
import com.example.domain.domain.PeriodHelperImpl
import com.example.gitapp.ui.service_repo.RepoNotificationsCreator
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

    @Binds
    @Singleton
    fun bindRepoNotificationsCreator(notificationsCreator: RepoNotificationsCreator): NotificationsCreator<Repo>
}