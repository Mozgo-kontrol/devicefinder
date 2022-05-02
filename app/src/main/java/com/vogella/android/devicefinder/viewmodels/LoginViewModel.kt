package com.vogella.android.devicefinder.viewmodels

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vogella.android.devicefinder.Tools.EMAILPATTERN
import com.vogella.android.devicefinder.Tools.PASSWORDPATTERN
import java.util.regex.Pattern

class LoginViewModel(context: Context) : ViewModel() {


    private val TAG: String = LoginViewModel::class.java.simpleName
    var email: MutableLiveData<String> = MutableLiveData("")
    var isValidEmail: MutableLiveData<Boolean> = MutableLiveData(false)
    var isError: MutableLiveData<Boolean> = MutableLiveData(false)

    var password: MutableLiveData<String> = MutableLiveData("")
    var isValidPassword: MutableLiveData<Boolean> = MutableLiveData(false)
    var isErrorPassword: MutableLiveData<Boolean> = MutableLiveData(false)

    var isButtonEnabled : MutableLiveData<Boolean> = MutableLiveData(false)


    fun buttonValid(){
        val x : Boolean = isValidEmail.value!!&&isValidPassword.value!!
        Log.wtf(TAG, "Button Login valid  buttonValid $x")
       // isButtonEnabled.postValue(x)
    }

    fun isValidEmail() : Boolean {
        val pattern = Pattern.compile(EMAILPATTERN)
        val matcher = pattern.matcher(email.value!!)

        Log.wtf(TAG, "email: ${email.value!!}")

        val valid = matcher.matches()
        Log.wtf(TAG, "isValid Email:: $valid")
        isError.postValue(!valid)
        return valid
    }


    fun isValidPassword(): Boolean {
        val pattern = Pattern.compile(PASSWORDPATTERN)
        val matcher = pattern.matcher(password.value!!)
        val validPassword = matcher.matches()

        isErrorPassword.postValue(!validPassword)

        Log.wtf(TAG, "isValidPassword: $password.value!!")

        return validPassword
    }


}

class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

 }
