package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class RapidMails(
    @SerializedName("cases")
    val cases: List<CaseX>,
    @SerializedName("totalRecords")
    val totalRecords: Int
)