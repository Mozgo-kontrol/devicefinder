package com.vogella.android.devicefinder.data

import android.content.Context
import android.content.res.Resources
import android.database.Observable
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.vogella.android.devicefinder.R
import org.json.JSONException

import org.json.JSONObject

import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors


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

    fun getJsonFromAssets(context: Context, fileName: String): String? {
        val b = Looper.getMainLooper().thread == Thread.currentThread()
        val currenttread = Thread.currentThread()
      //  Thread.sleep(5000)
        Log.i("getJsonFromAssets", "Main its current thread: $b : ${currenttread.name}")

        val jsonString: String = try {
            val inputStream: InputStream = fileName.let { context.assets.open(it) }
            val size: Int = inputStream.available()
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

fun parseJSONStringToList(jsonString: String): List<Device> {

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
            val status =
                if (device.getString("currentStatus") == "Free") Status.Free else Status.InUse

            receivedDevicesList.add(
                Device(
                    id, model, Employee(idEmployee, firstname, lastname), status
                )
            )
        }
    } catch (e: JSONException) {

        Log.e("Fehler Json", "JSONException: " + e.message)
    }
    return receivedDevicesList
}


fun showMeThread(tag: String){

    val b = Looper.getMainLooper().thread == Thread.currentThread()
    val currenttread = Thread.currentThread()
    // Thread.sleep(5000)
    Log.i(tag, "Main its current thread: $b : ${currenttread.name}")

}


fun parseListToJsonString(devices: List<Device>): String {

    val tag = "parseListToJsonString"
    showMeThread(tag)

    val gson = Gson()
    return gson.toJson(devices) // Serialization
}

fun parseJSONSStringToList2(jsonString: String): List<Device> {

    val tag = "parseJSONSStringToList2"
    showMeThread(tag)
    val gson = Gson()
    // Deserialization
    val collectionType: Type = object : TypeToken<Collection<Device>>() {}.type
    return gson.fromJson(jsonString, collectionType)
  // Deserialization
}


fun convertStreamToString(stream: InputStream) : String{

    val tag = "convertStreamToString"
    showMeThread(tag)
    var text =""
    try {
        text = BufferedReader(
            InputStreamReader(stream, StandardCharsets.UTF_8)
        )   .lines()
             .collect(Collectors.joining("\n"))
    }
    catch (e: Exception) {
        e.printStackTrace()
    }
    return text
}


