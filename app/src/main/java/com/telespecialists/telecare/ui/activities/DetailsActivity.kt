package com.telespecialists.telecare.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.telespecialists.telecare.R
import com.telespecialists.telecare.data.CaseDetails
import com.telespecialists.telecare.utils.Constants
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DetailsActivity : AppCompatActivity() {
    private var id: String? = null
    private var caseType: String? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        id = intent.getStringExtra("id")
        caseType = intent.getStringExtra("case_type")
        case_type_tv.text = caseType
        case_type_tv.isSelected = true

        back.setOnClickListener {
            finish()
        }
        progressDialog()
        generateTokenxVolley()
    }

    private fun progressDialog() {
        try {
            progressDialog = ProgressDialog(this)
            progressDialog!!.setMessage("Loading...")
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
        } catch (e: Exception) {
        }
    }


    private fun generateTokenxVolley() {
        val myRequestQueue = Volley.newRequestQueue(this)
        val url = "http://uat.strokealert911.com/api/token"

        val myStringRequest = object :
            StringRequest(Method.POST, url,
                Response.Listener { response ->

                    val obj = JSONObject(response)
                    val token = obj.getString("access_token")
                    Log.e("token", token)
                    getDetails(token)
                }, Response.ErrorListener
                { error ->
                    Log.e("error", error.toString())
                }) {
            override fun getParams(): Map<String, String> {
                val MyData: MutableMap<String, String> =
                    HashMap()
                MyData["username"] = Constants.USER_NAME
                MyData["password"] = Constants.USER_PASSWORD
                MyData["grant_type"] = Constants.GRANT_TYPE
                return MyData
            }
        }


        myRequestQueue.add(myStringRequest)
    }

    private fun getDetails(token: String?) {
        val myRequestQueue = Volley.newRequestQueue(this)
        val builder = Uri.Builder()
        builder.scheme("http")
            .authority("uat.strokealert911.com")
            .appendPath("api")
            .appendPath("case")
            .appendPath("detail")
            .appendPath(id)

        val myUrl: String = builder.build().toString()
        val myStringRequest = object :
            StringRequest(
                Method.GET, myUrl,
                Response.Listener { response ->
                    val model: CaseDetails =
                        Gson().fromJson(response.toString(), CaseDetails::class.java)
                    Log.e("data", model.toString())
                    updateUI(model)

                }, com.android.volley.Response.ErrorListener
                { error ->
                    progressDialog!!.dismiss()
                }) {

            override fun getHeaders(): MutableMap<String, String> {
                val params: MutableMap<String, String> =
                    HashMap()
                params["Authorization"] = "bearer $token"
                return params
            }
        }


        myRequestQueue.add(myStringRequest)
    }

    private fun getAge(year: Int, month: Int, day: Int): String? {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }

    private fun updateUI(model: CaseDetails?) {
        if (model != null) {
            Log.e("model", "" + model.toString())

            try {
                val currentTime = Calendar.getInstance().time
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val date1: Date = dateFormat.parse(model!!.casResponseTsNotification)
                val date2: Date = dateFormat.parse(model!!.casMetricDoorTimeEst)
                val date3: Date = dateFormat.parse(model!!.casBillingDob)
                val formatter: DateFormat = SimpleDateFormat("MMM d, yyyy h:mm a")
                val formatter2: DateFormat = SimpleDateFormat("MMM d, yyyy")
                val dateStr1: String = formatter.format(date1)
                val dateStr2: String = formatter.format(date2)
                val dateStr3: String = formatter2.format(date3)
                val dateStr4: String = formatter.format(currentTime)

                //toolbarTitle.text = "Case# ${model!!.casCaseNumber.toString()}"
                cas_patient.text = model.casPatient
                cas_patient.isSelected = true

                cas_identification_type.text = model.casIdentificationNumber
                cas_identification_type.isSelected = true

                cas_billing_dob.text = dateStr3
                cas_billing_dob.isSelected = true

                cas_cart.text = model.casCart
                cas_cart.isSelected = true

                fac_name.text = model.facName
                fac_name.isSelected = true

                cas_response_ts_notification.text = dateStr1
                cas_response_ts_notification.isSelected = true

                cas_metric_door_time_est.text = dateStr2
                cas_metric_door_time_est.isSelected = true

                PhysicianName.text = model.physicianName
                PhysicianName.isSelected = true

                cas_case_number2.text = model.casCaseNumber.toString()
                cas_case_number2.isSelected = true

                cas_created_by_name.text = model.casCreatedByName
                val yearFormat: DateFormat = SimpleDateFormat("yyyy")
                val monthFormat: DateFormat = SimpleDateFormat("MM")
                val dayFormat: DateFormat = SimpleDateFormat("dd")

                val y: Int = yearFormat.format(date3).toInt()
                val m: Int = monthFormat.format(date3).toInt()
                val d: Int = dayFormat.format(date3).toInt()
                cas_billing_dob2.text = "${getAge(y, m, d)} ${model.casMetricPatientGender}"
                cas_billing_dob2.isSelected = true

                facTime.text = dateStr4
                facTime.isSelected = true

                progressDialog!!.hide()

                btn3.setOnClickListener {
                    val intent = Intent(this, MailDetailsActivity::class.java)
                    intent.putExtra("casex", model.casCallback)
                    startActivity(intent)
                }
                /*btn2.setOnClickListener {
                    val intent = Intent(this, MailDetailsActivity::class.java)
                    intent.putExtra("casex", model.facEmr)
                    startActivity(intent)
                }*/
            } catch (e: Exception) {
                Log.e("e", e.toString())
            }
        }

    }
}
