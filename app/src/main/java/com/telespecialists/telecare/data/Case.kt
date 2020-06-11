package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class Case(
    @SerializedName("CaseNumber")
    val caseNumber: Int,
    @SerializedName("CaseStatus")
    val caseStatus: CaseStatus,
    @SerializedName("CaseType")
    val caseType: CaseType,
    @SerializedName("CreatedDate")
    val createdDate: String,
    @SerializedName("Facility")
    val facility: Facility,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Physician")
    val physician: Physician


) {
    override fun toString(): String {
        return "Case(caseNumber=$caseNumber)"
    }
}