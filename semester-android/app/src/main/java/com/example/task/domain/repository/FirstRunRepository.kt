package com.example.task.domain.repository

interface FirstRunRepository {

    fun setFirstRun()

    fun getFirstRun() : Boolean

}