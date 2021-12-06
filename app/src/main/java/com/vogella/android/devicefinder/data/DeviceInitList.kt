package com.vogella.android.devicefinder.data

import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.vogella.android.devicefinder.LoginFragment
import com.vogella.android.devicefinder.R
import org.json.JSONException

import org.json.JSONObject

import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


fun deviceList(resources: Resources): List<Device> {

    return mutableListOf(
        Device(
            id = 2,
            model = resources.getString(R.string.samsung_s),
            employee = imOfficeEmployee(),
            currentStatus = Status.Free
        ),
        Device(
            id = 3,
            model = resources.getString(R.string.samsung_a),
            employee = imOfficeEmployee(),
            currentStatus = Status.Free
        ),
        Device(
            id = 4,
            model = resources.getString(R.string.samsung_a),
            employee = Employee(2, "Alexander", "Knauf"),
            currentStatus = Status.InUse
        ),
        Device(
            id = 5,
            model = resources.getString(R.string.samsung_a),
            employee = Employee(4, "igor", "ferbert"),
            currentStatus = Status.InUse
        )
    )
}

fun imOfficeEmployee(): Employee = Employee(1, "Im", "Office")

object Utils {

    fun getJsonFromAssets(context: Context, fileName: String?): String? {
        val jsonString: String = try {
            val inputStream: InputStream? = fileName?.let { context.assets.open(it) }
            val size: Int = inputStream!!.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return jsonString
    }
}

fun parseJSONString(jsonString: String?): List<Device>? {

    val receivedDevicesList: MutableList<Device> = ArrayList()
    try {
        val jsonObj = JSONObject(jsonString)

        // Anforderen des JSON-Array Knotens mit den Quotes-Objekten
        val devices = jsonObj.getJSONArray("Devices")
        Log.e("Devices", "${devices.length()}")
        // Durchlaufen des Quotes-Arrays und Auslesen der Daten jedes Quote-Objekts
        for (i in 0 until devices.length()) {
            val device = devices.getJSONObject(i)

            val id = device.getLong("id")
            Log.e("Devices", "$id")
            val model = device.getString("model")
            Log.e("Devices", model)
            val employee = device.getJSONObject("employee")
            val idEmployee = employee.getLong("id")
            val firstname = employee.getString("firstname")
            val lastname = employee.getString("lastname")
            val status = if (device.getString("currentStatus") == "Free") Status.Free else Status.InUse


            receivedDevicesList.add(Device(id, model, Employee(idEmployee, firstname, lastname), status
                )
            )
        }
    } catch (e: JSONException) {

        Log.e("Fehler Json", "JSONException: " + e.message)
    }
    return receivedDevicesList
}




