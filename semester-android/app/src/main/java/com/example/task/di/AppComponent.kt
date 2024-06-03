package com.example.task.di

import android.content.Context
import com.example.task.data.di.DataModule
import com.example.task.data.di.DataModuleBinder
import com.example.task.presentation.MainActivityViewModel
import com.example.task.presentation.authentication.AuthViewModel
import com.example.task.presentation.editevent.EditEventViewModel
import com.example.task.presentation.editprofile.EditProfileViewModel
import com.example.task.presentation.event.EventViewModel
import com.example.task.presentation.main.MainViewModel
import com.example.task.presentation.profile.ProfileViewModel
import com.example.task.presentation.registration.RegisterViewModel
import com.example.task.presentation.search.SearchViewModel
import com.example.task.presentation.select.SelectViewModel
import com.example.task.presentation.themechoose.ThemeChooseViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [
    DataModule::class,
    DataModuleBinder::class
])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun provideContext(ctx : Context) : Builder

        fun build() : AppComponent
    }

    fun registerViewModel() : RegisterViewModel.Factory

    fun authViewModel() : AuthViewModel.Factory

    fun mainViewModel() : MainViewModel.Factory

    fun searchViewModel() : SearchViewModel.Factory

    fun profileViewModel() : ProfileViewModel.Factory

    fun editProfileViewModel() : EditProfileViewModel.Factory

    fun mainActivityViewModel() : MainActivityViewModel.Factory

    fun selectViewModel() : SelectViewModel.Factory

    fun themeChooseViewModel() : ThemeChooseViewModel.Factory

    fun eventViewModel() : EventViewModel.Factory

    fun editEventViewModel() : EditEventViewModel.Factory
}