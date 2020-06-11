package com.telespecialists.telecare.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Schedule {
    @SerializedName("NPI")
    @Expose
    var nPI: String? = null

    @SerializedName("FirstName")
    @Expose
    var firstName: String? = null

    @SerializedName("LastName")
    @Expose
    var lastName: String? = null

    @SerializedName("StartTime")
    @Expose
    var startTime: String? = null

    @SerializedName("EndTime")
    @Expose
    var endTime: String? = null

    /**
     * No args constructor for use in serialization
     *
     */
    constructor() {}

    /**
     *
     * @param firstName
     * @param lastName
     * @param nPI
     * @param startTime
     * @param endTime
     */
    constructor(
        nPI: String?,
        firstName: String?,
        lastName: String?,
        startTime: String?,
        endTime: String?
    ) : super() {
        this.nPI = nPI
        this.firstName = firstName
        this.lastName = lastName
        this.startTime = startTime
        this.endTime = endTime
    }

    override fun toString(): String {
        return "Schedule{" +
                "nPI='" + nPI + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}'
    }
}