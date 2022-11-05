package com.alief.periodictable.repository

import com.alief.periodictable.api.RetrofitInstance

class Repository {

    suspend fun getElements() = RetrofitInstance.api.getElements()

    suspend fun getSearchElements(symbol : String) =
        RetrofitInstance.api.getSearchElement(symbol)

    suspend fun getElementByState(state: String) =
        RetrofitInstance.api.getElementByState(state)

}