package com.vogella.android.devicefinder

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.regex.Pattern


object Tools {

   fun makeToHighFirstLetter(name : String) : String{
       return name[0].uppercase()+ name.substring(1, name.length)
   }

    @SuppressLint("HardwareIds")
    fun getSystemDetail(context : Context): String {
        return  "Brand: ${Build.BRAND} \n"+
                "Manufacture: ${Build.MANUFACTURER} \n"+
                getDeviceId(context)+
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
  fun getDeviceId(context: Context): String {
     return "DeviceID: ${
         Settings.Secure.getString(
             context!!.contentResolver,
             Settings.Secure.ANDROID_ID
         )} \n"
  }

  fun CharSequence.isValidPassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPattern)
        println(pattern)
        val matcher = pattern.matcher(this)
        println(matcher)
        return matcher.matches()&& this.count() > 6
  }
    const val PASSWORDPATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    const val EMAILPATTERN = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"

    /*fun CharSequence.isValidEmail(): Boolean {
        val emailPattern = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"
        val pattern = Pattern.compile(emailPattern)
        //println(pattern)
        val matcher = pattern.matcher(this)
       // println(matcher)
        return matcher.matches()&& this.count() > 6
    }*/


}