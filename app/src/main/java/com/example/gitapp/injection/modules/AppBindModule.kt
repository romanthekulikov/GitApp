package com.example.gitapp.injection.modules

import com.example.domain.domain.Repository
import com.example.data.data.RepositoryImpl
import com.example.domain.domain.entity.Repo
import com.example.gitapp.background.NotificationsCreator
import com.example.gitapp.background.RepoNotificationsCreator
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface AppBindModule {

    @Binds
    @Singleton
    fun bindRepository(repository: RepositoryImpl): Repository

    @Binds
    @Singleton
    fun bindRepoNotificationsCreator(notificationsCreator: RepoNotificationsCreator): NotificationsCreator<Repo>
}