package com.vogella.android.devicefinder.data

data class Employee(
    val id:Long,
    val firstname : String,
    val lastname: String,
){
    var email = "${firstname.lowercase()}.${lastname.lowercase()}@edeka.de"
}
