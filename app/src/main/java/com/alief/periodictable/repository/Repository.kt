package com.alief.periodictable.repository

import com.alief.periodictable.api.RetrofitInstance

class Repository {

    suspend fun getElements() = RetrofitInstance.api.getElements()

    suspend fun getSearchElements(symbol : String) =
        RetrofitInstance.api.getSearchElement(symbol)

}