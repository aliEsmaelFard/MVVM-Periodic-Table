package com.alief.periodictable.api

import com.alief.periodictable.model.ElementItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("elements")
    suspend fun getElements(): Response<ElementItem>


    @GET("element/symbol/{Lu}")
    suspend fun getSearchElement(
        @Path("Lu") symbol: String
    ): Response<ElementItem>


    @GET("elements/state/{gas}")
    suspend fun getElementByState(
        @Path("gas") state: String
    ): Response<ElementItem>
}