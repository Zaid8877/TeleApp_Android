package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class Cases(
    @SerializedName("cases")
    val cases: MutableList<Case>,
    @SerializedName("totalRecords")
    val totalRecords: Int


) {
    override fun toString(): String {
        return "Cases(cases=$cases, totalRecords=$totalRecords)"
    }
}