package com.example.frontend.api

import com.example.frontend.models.Client
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("clients")
    suspend fun addClient(
        @Field("Id") Id: Int,
        @Field("name") name: String,
        @Field("phoneNumber") phoneNumber: String
    ): Response<Client>
}