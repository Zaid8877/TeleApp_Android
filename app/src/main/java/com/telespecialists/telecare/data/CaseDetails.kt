package com.telespecialists.telecare.data


import com.google.gson.annotations.SerializedName

data class CaseDetails(
    @SerializedName("cas_billing_dob")
    val casBillingDob: String,
    @SerializedName("cas_callback")
    val casCallback: String,
    @SerializedName("cas_cart")
    val casCart: String,
    @SerializedName("cas_case_number")
    val casCaseNumber: Int,
    @SerializedName("cas_created_by_name")
    val casCreatedByName: String,
    @SerializedName("cas_cst_key")
    val casCstKey: Int,
    @SerializedName("cas_ctp_key")
    val casCtpKey: Int,
    @SerializedName("cas_fac_key")
    val casFacKey: String,
    @SerializedName("cas_identification_number")
    val casIdentificationNumber: String,
    @SerializedName("cas_identification_type")
    val casIdentificationType: String,
    @SerializedName("cas_key")
    val casKey: Int,
    @SerializedName("cas_metric_door_time_est")
    val casMetricDoorTimeEst: String,
    @SerializedName("cas_metric_patient_gender")
    val casMetricPatientGender: String,
    @SerializedName("cas_patient")
    val casPatient: String,
    @SerializedName("cas_phy_key")
    val casPhyKey: String,
    @SerializedName("cas_response_ts_notification")
    val casResponseTsNotification: String,
    @SerializedName("fac_emr")
    val facEmr: String,
    @SerializedName("fac_name")
    val facName: String,
    @SerializedName("fac_timezone")
    val facTimezone: String,
    @SerializedName("PhysicianName")
    val physicianName: String
)