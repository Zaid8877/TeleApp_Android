package com.telespecialists.telecare.utils

object Constants {
    const val USER_NAME = "telecare-api-001"
    const val USER_PASSWORD = "dGVsZWNhcmUtYXBpLTAwMTpOYXRpb25hbCQwOnZjYTZ0MXkhdzdeZW4wdTg0eDNt"
    const val GRANT_TYPE = "password"
    const val SAVED_TOKEN = "bearer_token"

    const val BASE_URL = "http://uat.strokealert911.com/"
    const val TOKEN_URL = "api/token"
    const val LOGIN_URL = "api/physician/Auth"
    const val CASES_BY_PHYS_URL = "api/cases/list/by-physicain"
    const val CASES_BY_PHYS_SCH = "api/physician/schedule"

    const val ID = "phys_id"
    const val NPI_NUMBER = "nPINumber"
}