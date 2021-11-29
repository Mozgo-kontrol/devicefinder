package com.vogella.android.devicefinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val TAG: String = LoginFragment::class.java.simpleName


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var a = 12

        //goToListFragment("firstname","lastname")
    }

    fun goToListFragment(firstName: String, lastName:String){

        val action = LoginFragmentDirections.actionLoginFragmentToListFragment(firstName, lastName)
        findNavController().navigate(action)

    }
}