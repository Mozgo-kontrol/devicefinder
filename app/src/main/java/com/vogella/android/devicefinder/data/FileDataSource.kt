package com.vogella.android.devicefinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.*
import java.lang.Exception
import java.nio.charset.StandardCharsets

import java.util.stream.Collectors

class FileDataSource(private val context: Context, val resources: Resources) {

    private val TAG: String = FileDataSource::class.java.simpleName

    fun loadDevicesList(filename: String = FILE_NAME_ASSETS): Observable<List<Device>> {
         return Observable.fromCallable {
             val jsonString = Utils.getJsonFromAssets(context, filename)
             jsonString?.let { parseJSONStringToList(jsonString = it) } ?: emptyList()
         }
    }

    fun saveFile(jsonString: String) : Completable {
       return Completable.create {emitter ->
            val filename = FILE_NAME
            val fileOutputStream: FileOutputStream
            try {
                fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
                fileOutputStream.write(jsonString.toByteArray())
                fileOutputStream.close()
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            } }
    }

    fun loadFile(): Single<String> {
        return Single.create { emitter ->
            val filename = FILE_NAME
            var data = ""
            val fileInputStream: FileInputStream
            try {
                fileInputStream = context.openFileInput(filename)
                val stream = BufferedInputStream(fileInputStream)

                data = BufferedReader(
                    InputStreamReader(stream, StandardCharsets.UTF_8)
                ).lines()
                    .collect(Collectors.joining("\n"))
                fileInputStream.close()
                //Producer
                emitter.onSuccess(data)

            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: FileDataSource? = null
        //const
        private const val FILE_NAME = "DevicesStringJson.json"
        private const val FILE_NAME_ASSETS = "devices.json"

        fun getFileDataSource(context: Context, resources: Resources): FileDataSource {

            return synchronized(FileDataSource::class) {
                val newInstance = INSTANCE ?: FileDataSource(context, resources)
                INSTANCE = newInstance
                newInstance
            }
        }


    }
}