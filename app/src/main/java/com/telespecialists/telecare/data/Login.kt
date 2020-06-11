package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("AddressBlock")
    val addressBlock: String,
    @SerializedName("CaseReviewer")
    val caseReviewer: Boolean,
    @SerializedName("ContractDate")
    val contractDate: String,
    @SerializedName("CredentialCount")
    val credentialCount: Int,
    @SerializedName("CredentialIndex")
    val credentialIndex: Double,
    @SerializedName("Email")
    val email: String,
    @SerializedName("EnableFive9")
    val enableFive9: Boolean,
    @SerializedName("FirstName")
    val firstName: String,
    @SerializedName("Gender")
    val gender: String,
    @SerializedName("Id")
    val id: String,
    @SerializedName("IsActive")
    val isActive: Boolean,
    @SerializedName("IsStrokeAlert")
    val isStrokeAlert: Boolean,
    @SerializedName("LastName")
    val lastName: String,
    @SerializedName("MobilePhone")
    val mobilePhone: String,
    @SerializedName("NHAlert")
    val nHAlert: Boolean,
    @SerializedName("NPINumber")
    val nPINumber: String,
    @SerializedName("PhoneNumber")
    val phoneNumber: Any,
    @SerializedName("status_change_cas_key")
    val statusChangeCasKey: Any,
    @SerializedName("status_change_date")
    val statusChangeDate: String,
    @SerializedName("status_change_date_forAll")
    val statusChangeDateForAll: String,
    @SerializedName("status_key")
    val statusKey: Int,
    @SerializedName("UserInitial")
    val userInitial: String,
    @SerializedName("UserName")
    val userName: String


) {
    override fun toString(): String {
        return "Login(addressBlock='$addressBlock', caseReviewer=$caseReviewer, contractDate='$contractDate', credentialCount=$credentialCount, credentialIndex=$credentialIndex, email='$email', enableFive9=$enableFive9, firstName='$firstName', gender='$gender', id='$id', isActive=$isActive, isStrokeAlert=$isStrokeAlert, lastName='$lastName', mobilePhone='$mobilePhone', nHAlert=$nHAlert, nPINumber='$nPINumber', phoneNumber=$phoneNumber, statusChangeCasKey=$statusChangeCasKey, statusChangeDate='$statusChangeDate', statusChangeDateForAll='$statusChangeDateForAll', statusKey=$statusKey, userInitial='$userInitial', userName='$userName')"
    }
}