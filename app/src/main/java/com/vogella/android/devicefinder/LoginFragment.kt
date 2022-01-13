package com.vogella.android.devicefinder

import ErrorMessage
import Title
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vogella.android.devicefinder.Tools.isValidPassword


/**
 * A simple [Fragment] subclass.
 *
 */
@ExperimentalComposeUiApi
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val TAG: String = LoginFragment::class.java.simpleName


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(this.context!!).apply {

            setContent {
                Greeting()
            }

        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       // val inputMail = view.findViewById<EditText>(R.id.ti_et)
      //  val login = view.findViewById<Button>(R.id.button)

        //goToListFragment("igor", "ferbert")
     //   login.setOnClickListener {
            /*    val splitEmail = inputMail.text.toString().split(".", "@")
                val firstName = splitEmail[0]
                val lastName = splitEmail[1]
                val afterAt = "@edeka.de"
                if (inputMail.text.toString().isNotEmpty() && inputMail.text.toString() == "$firstName.$lastName$afterAt") {

                    goToListFragment(firstName,lastName)
                } else
                    Toast.makeText(
                        activity, "E-Mail ungültig", Toast.LENGTH_SHORT
                    ).show()*/

    //    }

    }

    private fun goToListFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
        findNavController().navigate(action)
    }

    private fun goToSignUpFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }


    @Composable
    @Preview(showBackground = true, name = "Light Mode")
    fun Greeting() {
        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize()) {
               // TopAppBar(title = {
               //     Text("Device Finder")
              //  })
                Title("Login")
                DefaultRecipeCard()
            }
        }

    }

    @Composable
    fun RecipeCard(modifier: Modifier) {
        Surface(shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    inputFields()
                }
            }
        }
    }

    @Composable
  //  @Preview
    fun DefaultRecipeCard() {
        MaterialTheme {
            RecipeCard(Modifier.padding(4.dp))
        }
    }

    @Composable
    fun inputFields() {

        val (focusRequester) = FocusRequester.createRefs()
        Column(modifier = Modifier.fillMaxWidth()) {
            emailInputField(focusRequester)
            passwordsInputField(focusRequester)
            LoginButton()

        }
    }

    @Composable
    fun LoginButton() {

         Box(
             Modifier
                 .fillMaxWidth()
                 .padding(top = 8.dp),
                 contentAlignment = Alignment.Center
             ) {
                Button(
                    onClick = { Log.wtf(TAG, "Button Login")
                        goToListFragment()
                    },
                    modifier = Modifier.size(width = 150.dp, height = 35.dp),
                    content = {
                        Text(text = "Login")
                    })
         }
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Don't have an account: here!",
                modifier = Modifier
                    .clickable {
                        Log.wtf(TAG, "go to SignUp")
                        goToSignUpFragment()
                    },
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
           // Spacer(modifier = Modifier.padding(top = 8.dp))

    }

    @Composable
    fun emailInputField(focusRequester: FocusRequester) {
        var email by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false)}
        val isValid = email.count() > 5 && '@' in email

        fun validate(text: String) {
            isError = text.count() < 5
        }
        TextField(
            value = email,
            label = {
                val label = if (isValid) "Email" else "Email*"
                Text(label)},
            textStyle = TextStyle(fontWeight = FontWeight.Bold),
            onValueChange = {
                email = it
                isError = false
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions (
                onNext = {
                    validate(email)
                    if(!isError){
                    focusRequester.requestFocus()
                    }
                }
            ),
            modifier = Modifier
                .semantics {
                    // Provide localized description of the error
                    if (isError) {
                        // error("Email format is invalid.")
                    }
                }
                .fillMaxWidth()
                .padding(5.dp)
                .focusRequester(focusRequester),
            singleLine = true
        )
        if (isError) {
            ErrorMessage("Requires '@' and at least 5 symbols")
        }
    }

    @Composable
    fun passwordsInputField(focusRequester: FocusRequester) {

        var password by rememberSaveable { mutableStateOf("") }

        var passwordHidden by rememberSaveable { mutableStateOf(true) }

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions (
                onDone = {  keyboardController?.hide()}
            ),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .focusRequester(focusRequester),
            singleLine = true
        )
       if (!password.isValidPassword() && password.count()>0) {

           ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")
      }
    }



}





