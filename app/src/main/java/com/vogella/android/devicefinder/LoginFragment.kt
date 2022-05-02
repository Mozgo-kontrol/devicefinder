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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vogella.android.devicefinder.viewmodels.LoginViewModel
import com.vogella.android.devicefinder.viewmodels.LoginViewModelFactory


/**
 * A simple [Fragment] subclass.
 *
 */
@ExperimentalComposeUiApi
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val TAG: String = LoginFragment::class.java.simpleName

    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory(this.context!!)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(this.context!!).apply {

            setContent {
                //goToListFragment()
                Greeting()
            }

        }
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
                //Title("Login")
                ScreenCard(Modifier.padding(4.dp))
            }
        }

    }

    @Composable
    fun ScreenCard(modifier: Modifier) {
        Surface(shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputFields(loginViewModel)
                }
            }
        }
    }
    @Composable
    fun InputFields(loginViewModel: LoginViewModel) {
       // val (focusRequester) = FocusRequester.createRefs()
        LazyColumn(modifier = Modifier.fillMaxWidth()){

           item{
               Title("Login")
           }
            item{
                emailInputField(loginViewModel)
                passwordsInputField(loginViewModel)
            }
            item{
                LoginButton(loginViewModel)
            }
            item{
                InfoText("Don't have an account: here!")
            }


        }
    }

    @Composable
    fun InfoText(text: String){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text,
                modifier = Modifier
                    .clickable {
                        Log.wtf(TAG, "go to SignUp")
                        goToSignUpFragment()
                    },
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun LoginButton(loginViewModel: LoginViewModel) {
         Box(
             Modifier
                 .fillMaxWidth()
                 .padding(4.dp),
                 contentAlignment = Alignment.Center,
             ) {
                Button(
                    onClick = {

                        if(loginViewModel.isValidEmail()&&loginViewModel.isValidPassword()){
                            Log.wtf(TAG, "Button Login : login User with: ${loginViewModel.email} " +
                                    "password: ${loginViewModel.password}")
                        }

                    },
                    enabled = true,
                    modifier = Modifier.size(width = 150.dp, height = 35.dp),
                    content = {
                        Text(text = "Login")
                    })
         }
           // Spacer(modifier = Modifier.padding(top = 8.dp))

    }

    @Composable
    fun emailInputField(loginViewModel: LoginViewModel){

        var email by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(loginViewModel.isError.value!!) }
            TextField(
                value = email,
                label = {
                    val label = if (!isError) "Email*" else "Email"
                    Text(label)},

                textStyle = TextStyle(fontWeight = FontWeight.Bold),
                onValueChange = {
                    email = it
                    loginViewModel.email.value = it
                    isError = false
                },
                isError = isError,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                keyboardActions = KeyboardActions (
                    onNext = {
                        //    if(isError){
                        // loginViewModel.isValidEmail(email)
                        //  }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .onFocusChanged {
                    },
                singleLine = true,
            )
        if (isError!!) {
            ErrorMessage("Requires '@' and at least 6 symbols")
        }
    }

    @Composable
    fun passwordsInputField(loginViewModel: LoginViewModel) {

        var password by rememberSaveable { mutableStateOf("") }

        var passwordHidden by rememberSaveable { mutableStateOf(true) }

       // var isError by rememberSaveable { mutableStateOf(false) }

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = password,
            onValueChange = {
                password = it
                loginViewModel.password.value = it
                loginViewModel.isErrorPassword.value = false
            },
            label = {
                val label = if (!loginViewModel.isErrorPassword.value!!) "Password*" else "Password"
                Text(label)
            },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = loginViewModel.isErrorPassword.value!!,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
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
                .padding(5.dp),
            singleLine = true
        )
        if (loginViewModel.isErrorPassword.value!!) {
            ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")
        }
    }
}





