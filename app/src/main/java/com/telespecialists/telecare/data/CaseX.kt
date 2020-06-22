package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName


data class CaseX(
    @SerializedName("Attachement_html")
    val attachementHtml: String,
    @SerializedName("Attachements")
    val attachements: Int,
    @SerializedName("Body")
    val body: String,
    @SerializedName("CreatedBy")
    val createdBy: String,
    @SerializedName("CreatedDate")
    val createdDate: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("From")
    val from: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("IsRead")
    val isRead: Boolean,
    @SerializedName("Logs")
    val logs: String,
    @SerializedName("Subject")
    val subject: String,
    @SerializedName("To")
    val to: String,
    @SerializedName("user_id")
    val userId: String
)