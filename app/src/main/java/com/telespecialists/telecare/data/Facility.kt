package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class Facility(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Name")
    val name: String
)