package com.example.task.data.repositories

import com.example.task.data.local.sharedpreferences.FirstRunPreferences
import com.example.task.domain.repository.FirstRunRepository
import javax.inject.Inject

class FirstRunRepositoryImpl @Inject constructor(
    private val preferences: FirstRunPreferences
) : FirstRunRepository {
    override fun setFirstRun() {
        preferences.setFirstRun()
    }

    override fun getFirstRun()= preferences.getFirstRun()
}