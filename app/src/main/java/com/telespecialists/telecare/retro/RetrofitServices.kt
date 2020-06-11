package com.telespecialists.telecare.retro

import com.telespecialists.telecare.data.*
import com.telespecialists.telecare.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface RetrofitServices {
    @FormUrlEncoded
    @POST(Constants.TOKEN_URL)
    fun getToken(@FieldMap map: HashMap<String?, Any?>): Call<TokenX>


    @POST(Constants.LOGIN_URL)
    fun login(@QueryMap map: HashMap<String?, Any?>): Call<Login>

    @GET(Constants.CASES_BY_PHYS_URL)
    fun cases(@QueryMap map: HashMap<String?, Any?>): Call<Cases>

    @GET(Constants.CASES_BY_PHYS_SCH)
    fun scheduleCases(@QueryMap map: HashMap<String?, Any?>): Call<List<ScheduleeItem>>
}