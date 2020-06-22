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
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        id = intent.getStringExtra("id")
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

    private fun updateUI(model: CaseDetails?) {
        if (model != null) {
            try {
                val currentTime = Calendar.getInstance().time
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val date1: Date = dateFormat.parse(model!!.casResponseTsNotification)
                val date2: Date = dateFormat.parse(model!!.casMetricDoorTimeEst)
                val date3: Date = dateFormat.parse(model!!.casBillingDob)
                val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateStr1: String = formatter.format(date1)
                val dateStr2: String = formatter.format(date2)
                val dateStr3: String = formatter.format(date3)
                val dateStr4: String = formatter.format(currentTime)

                toolbarTitle.text = "Case# ${model!!.casCaseNumber.toString()}"
                cas_patient.text = model.casPatient
                cas_identification_type.text = model.casIdentificationNumber
                cas_billing_dob.text = dateStr3
                cas_cart.text = model.casCart
                fac_name.text = model.facName
                cas_response_ts_notification.text = dateStr1
                cas_metric_door_time_est.text = dateStr2
                PhysicianName.text = model.physicianName
                cas_case_number2.text = model.casCaseNumber.toString()
                cas_created_by_name.text = model.casCreatedByName
                cas_billing_dob2.text = dateStr3
                facTime.text = dateStr4
                progressDialog!!.hide()

                btn3.setOnClickListener {
                    val intent = Intent(this, MailDetailsActivity::class.java)
                    intent.putExtra("casex", model.casCallback)
                    startActivity(intent)
                }
                btn2.setOnClickListener {
                    val intent = Intent(this, MailDetailsActivity::class.java)
                    intent.putExtra("casex", model.facEmr)
                    startActivity(intent)
                }
            } catch (e: Exception) {
            }
        }

    }
}
