package com.koleo.network

import com.koleo.network.model.Station
import com.koleo.network.model.StationKeyword
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface KoleoApi {

    @Headers("X-KOLEO-Version: 1")
    @GET("/api/v2/main/stations")
    suspend fun getStations(): Response<List<Station>>

    @Headers("X-KOLEO-Version: 1")
    @GET("/api/v2/main/station_keywords")
    suspend fun getStationsKeywords(): Response<List<StationKeyword>>

}