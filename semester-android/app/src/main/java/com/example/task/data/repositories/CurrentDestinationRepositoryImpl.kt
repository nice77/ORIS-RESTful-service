package com.example.task.data.repositories

import com.example.task.data.local.sharedpreferences.CurrentDestinationPreferences
import com.example.task.domain.repository.CurrentDestinationRepository
import javax.inject.Inject

class CurrentDestinationRepositoryImpl @Inject constructor(
    private val preferences: CurrentDestinationPreferences
) : CurrentDestinationRepository {
    override fun setCurrentDestination(resId : Int) = preferences.setCurrentDestination(resId)

    override fun getCurrentDestination(): Int = preferences.getCurrentDestination()

    override fun deleteCurrentDestination() = preferences.deleteCurrentDestination()
}