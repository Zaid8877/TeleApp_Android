package com.telespecialists.telecare.retro

import com.telespecialists.telecare.data.Cases
import com.telespecialists.telecare.data.Login
import com.telespecialists.telecare.data.ScheduleeItem
import com.telespecialists.telecare.data.TokenX
import com.telespecialists.telecare.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface RetroServices {


    @POST(Constants.TOKEN_URL)
    @FormUrlEncoded
    fun getToken(@FieldMap map: HashMap<String, Any>): Call<TokenX>

    @POST(Constants.LOGIN_URL)
    fun login(@QueryMap map: HashMap<String, Any>): Call<Login>


    @GET(Constants.CASES_BY_PHYS_URL)
    fun cases(
        @Header("Authorization") token: String,
        @QueryMap map: HashMap<String?, Any?>
    ): Call<Cases>


    @GET(Constants.CASES_BY_PHYS_SCH)
    fun scheduleCases(
        @Header("Authorization") token: String,
        @QueryMap map: HashMap<String?, Any?>
    ): Call<List<ScheduleeItem>>
}