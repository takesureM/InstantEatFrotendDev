package com.example.frontend.models


import com.google.gson.annotations.SerializedName

data class FoodItem(
    @SerializedName("description")
    val description: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pictureUrl")
    val pictureUrl: String,
    @SerializedName("price")
    val price: Int
)