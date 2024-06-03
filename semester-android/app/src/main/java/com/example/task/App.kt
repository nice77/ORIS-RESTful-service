package com.example.task

import android.app.Application
import com.example.task.di.AppComponent
import com.example.task.di.DaggerAppComponent
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    lateinit var component : AppComponent

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_KEY)
        component = DaggerAppComponent.builder()
            .provideContext(applicationContext)
            .build()
    }

}