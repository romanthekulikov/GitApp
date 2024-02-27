package com.example.gitapp.injection

import com.example.gitapp.injection.modules.AppModule
import com.example.gitapp.ui.service_repo.RepoNotificationsCreator
import com.example.gitapp.ui.diagram.DiagramPresenter
import com.example.gitapp.ui.main.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(diagramPresenter: DiagramPresenter)
    fun inject(notificationCreator: RepoNotificationsCreator)
    fun inject(mainPresenter: MainPresenter)
}
