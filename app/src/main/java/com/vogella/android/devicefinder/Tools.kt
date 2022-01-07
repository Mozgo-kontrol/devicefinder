package com.vogella.android.devicefinder

import android.Manifest.permission
import android.os.Build

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.coroutineContext

import androidx.core.content.ContextCompat.getSystemService

import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import android.content.Context.TELEPHONY_SERVICE as TELEPHONY_SERVICE
import android.Manifest.permission.ACCESS_COARSE_LOCATION

import android.Manifest.permission.READ_PHONE_STATE

import android.Manifest.permission.READ_PHONE_NUMBERS

import android.Manifest.permission.READ_SMS

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService


object Tools {

   fun makeToHighFirstLetter(name : String) : String{
       return name[0].uppercase()+ name.substring(1, name.length)
   }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds")
    fun getSystemDetail(context : Context): String {
        return  "Brand: ${Build.BRAND} \n"+
                "Manufacture: ${Build.MANUFACTURER} \n"+
                //getDeviceId(context)+
                "Model: ${Build.MODEL} \n" +
                //"ID: ${Build.ID} \n" +
                "Android: ${Build.VERSION.RELEASE} \n"+
                "SDK version: ${Build.VERSION.SDK_INT} \n"
                //"Brand: ${Build.BRAND} \n" +
                //"User: ${Build.USER} \n" +
                //"Type: ${Build.TYPE} \n" +
                //"Base: ${Build.VERSION_CODES.BASE} \n" +
                //"Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                //"Board: ${Build.BOARD} \n" +
               // "Host: ${Build.HOST} \n" +
               // "FingerPrint: ${Build.FINGERPRINT} \n" +
               //"Version Code: ${Build.VERSION.RELEASE}"
    }

  @SuppressLint("HardwareIds")
  @RequiresApi(Build.VERSION_CODES.O)
  fun getDeviceId(context: Context): String {
     return "DeviceID: ${
         Settings.Secure.getString(
             context!!.contentResolver,
             Settings.Secure.ANDROID_ID
         )} \n"
     }
}