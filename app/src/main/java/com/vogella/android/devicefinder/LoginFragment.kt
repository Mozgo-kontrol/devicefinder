package com.vogella.android.devicefinder

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController



/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

   private val TAG: String = LoginFragment::class.java.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputMail = view.findViewById<EditText>(R.id.ti_et)
        val login = view.findViewById<Button>(R.id.button)

        //goToListFragment("igor","ferbert")
        login.setOnClickListener {
            val splitEmail = inputMail.text.toString().split(".", "@")
            val firstName = splitEmail[0]
            val lastName = splitEmail[1]
            val afterAt = "@edeka.de"
            if (inputMail.text.toString().isNotEmpty() && inputMail.text.toString() == "$firstName.$lastName$afterAt") {

                goToListFragment(firstName,lastName)
            } else
                Toast.makeText(
                    activity, "E-Mail ungültig", Toast.LENGTH_SHORT
                ).show()

        }

    }

    private fun goToListFragment(firstName: String, lastName:String){


        val action = LoginFragmentDirections.actionLoginFragmentToListFragment(firstName, lastName)
       findNavController().navigate(action)


    }


}





