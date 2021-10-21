package com.example.frontend.repository

import com.example.frontend.api.RetrofitInstance
import com.example.frontend.models.Client
import retrofit2.Response

class Repository {

    suspend fun addClient(id: Int, name: String, phoneNumber: String): Response<Client> {
        return RetrofitInstance.api.addClient(id, name, phoneNumber)
    }
}