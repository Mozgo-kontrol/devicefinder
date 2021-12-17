package com.vogella.android.devicefinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import io.reactivex.*
import io.reactivex.Observable
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets

import java.util.*
import java.util.Observer
import java.util.stream.Collectors

class DataSource(private val context: Context,val resources: Resources) {

    private val TAG: String = DataSource::class.java.simpleName

    fun loadDevicesList(filename: String = "devices.json"): Observable<List<Device>> {

       // val jsonString = Utils.getJsonFromAssets(context, filename)
         return Observable.fromCallable {
             val jsonString = Utils.getJsonFromAssets(context, filename)
             val parseJSONString = jsonString?.let { parseJSONStringToList(jsonString = it) }
             parseJSONString
         }

        /*  return if ("" != null) {
                Observable.fromCallable {
                    val jsonString = Utils.getJsonFromAssets(context, filename)
                    val parseJSONString = jsonString?.let { parseJSONString(jsonString = it) }
                    parseJSONString
                }
            } else {
                Observable.empty()
            }*/
    }

    fun saveFile(jsonString : String) {

        val tag = "saveFile"
        showMeThread(tag)

        val filename = "DevicesStringJson.json"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fileOutputStream.write(jsonString.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFile(): String {

        val tag = "loadFile"
        showMeThread(tag)

         val filename = "DevicesStringJson.json"
         var data = ""
         val fileInputStream: FileInputStream
         try {
             fileInputStream = context.openFileInput(filename)
             val stream = BufferedInputStream(fileInputStream)

             data = BufferedReader(
                 InputStreamReader(stream, StandardCharsets.UTF_8)
             )   .lines()
                 .collect(Collectors.joining("\n"))
             fileInputStream.close()
         }
         catch (e: Exception) {
             e.printStackTrace()
         }
         return data
     }


   /* fun loadFile(): Observable<String> {
        val filename = "DevicesStringJson.json"
        var data = ""
        //val fileInputStream: FileInputStream
        return Observable.fromCallable {getFile(context)}
            .map {convertStreamToString(it)}
    }

    private fun getFile(context: Context): InputStream? {

        val filename = "DevicesStringJson.json"
        try {
            val fileInputStream = context.openFileInput(filename)
           val steam = BufferedInputStream(fileInputStream)
            fileInputStream.close()
            return steam
        }
        catch (e :Exception){
            println(e.stackTrace)
        }
        return null
    }*/

    //example 1

    fun removeDevice(device: Device) {}

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: DataSource? = null
        fun getDataSource(context: Context, resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(context, resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}