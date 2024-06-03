package com.example.task.data.local.sharedpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class FirstRunPreferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun setFirstRun() {
        with(preferences.edit()) {
            putBoolean(Keys.FIRST_RUN_PREF, false)
            commit()
        }
    }

    fun getFirstRun() = preferences.getBoolean(Keys.FIRST_RUN_PREF, true)

}