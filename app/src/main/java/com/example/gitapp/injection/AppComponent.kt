package com.example.gitapp.injection

import com.example.gitapp.injection.modules.AppModule
import com.example.gitapp.injection.modules.ServiceModule
import com.example.gitapp.background.RepoNotificationsCreator
import com.example.gitapp.ui.diagram.DiagramPresenter
import com.example.gitapp.ui.main.MainPresenter
import com.example.gitapp.background.RepoWorker
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ServiceModule::class])
interface AppComponent {
    fun inject(diagramPresenter: DiagramPresenter)
    fun inject(notificationCreator: RepoNotificationsCreator)
    fun inject(mainPresenter: MainPresenter)
    fun inject(repoWorker: RepoWorker)
}
