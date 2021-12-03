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

  //  private val TAG: String = LoginFragment::class.java.simpleName

   // var enterMail = view?.findViewById<TextView>(R.id.tv_email)
   // private var inputMail = view?.findViewById<EditText>(R.id.ti_et)

   // var input = inputMail.toString()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_login, container, false)

        var inputMail = v.findViewById<EditText>(R.id.ti_et)

        val login = v.findViewById<Button>(R.id.button)

        login.setOnClickListener {

            var splitEmail = inputMail.text.toString().split(".", "@")
            var firstName = splitEmail[0]
            var lastName = splitEmail[1]
            val afterAt = "@edeka.de"


            if (inputMail.text.toString().isNotEmpty() && inputMail.text.toString() == "$firstName.$lastName$afterAt") {

                goToListFragment(firstName,lastName)
            } else

                Toast.makeText(
                    activity, "E-Mail ungültig", Toast.LENGTH_SHORT
                ).show()

        }

        return v
    }


/**
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)


        login?.setOnClickListener {
            var splitEmail = input.split(".")
            var firstName = splitEmail[0]
           var lastName = splitEmail[1]
           // var  afterAt = splitEmail[2] + splitEmail[3]
           val afterAt = "@edeka.de"


              enterMail?.setText("E-Mail ungültig").toString()
            val textViewValue = enterMail?.text


             if ( input != "$firstName.$lastName$afterAt") {
                 Toast.makeText(
                  activity, "E-Mail ungültig", Toast.LENGTH_SHORT
                ).show()
            }

            goToListFragment("firstname","lastname")
         }

    }
     */

    private fun goToListFragment(firstName: String, lastName:String){

        val action = LoginFragmentDirections.actionLoginFragmentToListFragment(firstName, lastName)
        findNavController().navigate(action)

    }


}





