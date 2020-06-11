package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class Physician(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Name")
    val name: String
)