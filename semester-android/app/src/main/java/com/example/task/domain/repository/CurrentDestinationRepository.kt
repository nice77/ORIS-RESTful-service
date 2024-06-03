package com.example.task.domain.repository

interface CurrentDestinationRepository {

    fun setCurrentDestination(resId : Int)

    fun getCurrentDestination() : Int

    fun deleteCurrentDestination()

}