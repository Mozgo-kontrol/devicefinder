package com.vogella.android.devicefinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources

class FireBaseDataSource (val context: Context, val resources: Resources){





    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: FireBaseDataSource? = null
        //const
        private const val FILE_NAME = ""
        private const val FILE_NAME_ASSETS = ""

        fun getFireBaseDataSource(context: Context, resources: Resources): FireBaseDataSource {
            return synchronized(FileDataSource::class) {
                val newInstance = INSTANCE ?: FireBaseDataSource(context, resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
