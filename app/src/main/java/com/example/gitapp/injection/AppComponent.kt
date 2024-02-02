package com.example.gitapp.injection

import com.example.gitapp.injection.modules.AppModule
import com.example.gitapp.ui.diagram.DiagramActivity
import com.example.gitapp.ui.diagram.DiagramPresenter
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.stargazers.StargazersActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DiagramActivity)
    fun inject(stargazersActivity: StargazersActivity)
    fun inject(diagramPresenter: DiagramPresenter)
}
