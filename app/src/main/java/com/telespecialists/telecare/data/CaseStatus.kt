package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class CaseStatus(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Name")
    val name: String
)