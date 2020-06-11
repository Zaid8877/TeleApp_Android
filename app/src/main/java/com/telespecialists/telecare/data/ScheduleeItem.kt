package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class ScheduleeItem(
    @SerializedName("EndTime")
    val endTime: String,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("NPI")
    val nPI: String,
    @SerializedName("StartTime")
    val startTime: String

) {
    override fun toString(): String {
        return "ScheduleeItem(endTime='$endTime', firstName='$firstName', lastName='$lastName', nPI='$nPI', startTime='$startTime')"
    }
}