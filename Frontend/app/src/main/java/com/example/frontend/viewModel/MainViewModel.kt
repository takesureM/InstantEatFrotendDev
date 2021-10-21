package com.example.frontend.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.models.Client
import com.example.frontend.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    var myResponse: MutableLiveData<Response<Client>> = MutableLiveData()

    fun addClient(id: Int, name: String, phoneNumber: String) {
        viewModelScope.launch {
            val response = repository.addClient( id, name, phoneNumber)
            myResponse.value = response
        }
    }
}