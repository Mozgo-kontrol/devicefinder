package com.vogella.android.devicefinder.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Single

class FireBaseDataSource (val context: Context, val resources: Resources){

    private val TAG: String = FireBaseDataSource::class.java.simpleName
    private val _databaseRefer: DatabaseReference = Firebase.database.reference.child("devices")

    init {
    // Write a message to the database
    }

    fun addDeviceToFireBase(device: Device): Completable {
        showMeThread(TAG)
        return Completable.create { emitter ->
            _databaseRefer.child(device.id.toString()).setValue(device)
            .addOnCompleteListener {
        showMeThread(TAG)
                emitter.onComplete()
                Log.wtf(TAG, "Complete : $it")}
            .addOnFailureListener {
                emitter.onError(it)
                Log.wtf(TAG, "Failure : $it")
            }

        }
    }

    fun getDeviceListFromFireBase() : Single<List<Device>> {
        return Single.create { emitter ->
            val list = mutableListOf<Device>()
            val query = _databaseRefer.orderByChild("id")
            query.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                  snapshot.children.forEach {
                      val id = it.child("id").value as Long
                      val model = it.child("model").value.toString()
                      val currentStatus = it.child("currentStatus").value.toString()
                      var status = Status.Free
                      if(currentStatus!=status.toString()){
                          status = Status.InUse
                      }
                      val idEmployee = it.child("employee").child("id").value as Long
                      val firstNameEmployee = it.child("employee").child("firstname").value.toString()
                      val lastNameEmployee = it.child("employee").child("lastname").value.toString()
                      val employee = Employee(idEmployee, firstNameEmployee, lastNameEmployee)
                      val device = Device(id, model, employee, status)
                      list.add(device)
                      Log.d(TAG, "Device id: ${device.id}")
                      Log.d(TAG, "Device model: ${device.model} ")
                      Log.d(TAG, "Device current status: ${device.currentStatus}")
                      Log.d(TAG, "Device firstname: ${device.employee.firstname}")
                  }
                    Log.d(TAG, "List length: ${list.size}")
                    emitter.onSuccess(list)

                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read value.", error.toException())
                    emitter.onError(error.toException())
                }
            })
        }
    }

    fun updateDeviceInFireBase(device: Device): Completable{
        TODO("Not yet implemented")
    }



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
