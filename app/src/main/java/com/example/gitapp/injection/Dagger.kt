package com.example.gitapp.injection

//import com.example.gitapp.data.api.models.ApiOwner
//import com.example.gitapp.data.api.models.ApiRepo
//import com.example.gitapp.data.api.models.ApiStarredData
//import com.example.gitapp.data.api.models.ApiUser
//import com.example.gitapp.entity.StarredData
//import com.example.gitapp.ui.main.MainActivity
//import com.example.gitapp.ui.main.RepoAdapter
//import dagger.Binds
//import dagger.Component
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton

//@Singleton
//@Component(modules = [Entity::class, AppBindModule::class])
//interface AppComponent {
//    fun inject(activity: MainActivity)
//}
//
//@Module
//class Entity {
//    @Provides
//    fun getOwner(
//        name: String,
//        avatarUrl: String
//    ): ApiOwner {
//        return ApiOwner(name, avatarUrl)
//    }
//
//    @Provides
//    fun getRepo(
//        id: Int,
//        name: String,
//        isPrivate: Boolean,
//        owner: ApiOwner,
//        stargazersCount: Int
//    ): ApiRepo {
//        return ApiRepo(id, name, isPrivate, owner, stargazersCount)
//    }
//
//    @Provides
//    fun getStarredData(
//        time: String,
//        user: ApiUser
//    ): StarredData {
//        return ApiStarredData(time, user)
//    }
//
//    @Provides
//    fun getApiUser(
//        name: String,
//        iconUrl: String
//    ): ApiUser {
//        return ApiUser(name, iconUrl)
//    }
//}
//
//@Module
//interface AppBindModule {
//    @Binds
//    fun bindRecyclerHandler(repoHelper: RepoAdapter.RepoRecyclerHelper): RepoAdapter.RepoRecyclerHelper
//}