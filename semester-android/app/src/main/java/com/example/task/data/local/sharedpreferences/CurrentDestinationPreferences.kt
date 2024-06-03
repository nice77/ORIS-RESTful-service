package com.example.task.data.local.sharedpreferences

import android.content.SharedPreferences
import javax.inject.Inject

class CurrentDestinationPreferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun setCurrentDestination(resId : Int) {
        with(preferences.edit()) {
            putInt(Keys.CURR_DEST_PREF, resId)
            commit()
        }
    }

    fun deleteCurrentDestination() {
        with(preferences.edit()) {
            remove(Keys.CURR_DEST_PREF)
            commit()
        }
    }

    fun getCurrentDestination() : Int = preferences.getInt(Keys.CURR_DEST_PREF, -1)

}