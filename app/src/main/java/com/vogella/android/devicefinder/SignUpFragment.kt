package com.vogella.android.devicefinder

import ErrorMessage
import Title
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.navigation.fragment.findNavController
import com.vogella.android.devicefinder.Tools.isValidPassword

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@ExperimentalComposeUiApi
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val TAG: String = SignUpFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return ComposeView(this.context!!).apply {
            setContent {
                    SignUpScreen()
                }

            }

    }

    @Composable
    @Preview(showBackground = true, name = "Light Mode")
    fun SignUpScreen() {
        val scrollState = rememberScrollState()
      MaterialTheme{
            Column(modifier = Modifier.fillMaxSize()
                .scrollable(
                state = scrollState,
                orientation = Orientation.Vertical)) {
                Title("SignUp")
                ScreenCard(Modifier.padding(4.dp))
            }
        }

    }

    private fun goToListFragment() {
       val action = SignUpFragmentDirections.actionSignUpFragmentToListFragment()
        findNavController().navigate(action)
    }

    private fun goToLoginFragment() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    @Composable
    fun ScreenCard(modifier: Modifier) {
        Surface(shape = RoundedCornerShape(8.dp), elevation = 8.dp, modifier = modifier) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InputFields()

                }
            }
        }
    }

    @Composable
    fun InputFields() {
        val (focusRequester) = FocusRequester.createRefs()
        Column(modifier = Modifier.fillMaxWidth()) {

            nameInputField(focusRequester)
            emailInputField(focusRequester)
             passwordsInputField(focusRequester)



            InfoText("If you already have an account: here!")
        }

    }

    @Composable
    fun nameInputField(focusRequester: FocusRequester){
        var username by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false)}

        fun validate(text: String) {
            isError = text.trim().count() < 6
        }
        TextField(
            value = username,
            label = {
                val label = if (!isError) "Name" else "Name*"
                Text(label)},
            textStyle = TextStyle(fontWeight = FontWeight.Bold),
            onValueChange = {
                username = it
                isError = false
                            },
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions (
                onNext = {focusRequester.requestFocus()}
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (username.count() > 0) {
                        validate(username)
                    }
                }
        )
        if (isError) {
            ErrorMessage("Name requires at least 6 symbols")
        }

    }

    @Composable
    fun emailInputField(focusRequester: FocusRequester) : String{
        var email by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false)}
        //val isValid = email.count() > 6 && '@' in email

        fun validate(text: String) {
            isError = text.count() <= 6 && '@' !in email
        }
        TextField(
            value = email,
            label = {
                val label = if (!isError) "Email" else "Email*"
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
                    //validate(email)
                    if(!isError){
                        focusRequester.requestFocus()
                    }
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (email.count() > 0) {
                        validate(email)
                    }
                    Log.wtf(TAG, "onFocusChanged")
                },
            singleLine = true
        )
        if (isError) {
            ErrorMessage("Email is not Valid")
        }
        //Check if Email is valid!


        return ""
    }

    @Composable
    fun passwordsInputField(focusRequester: FocusRequester) : String {

        var password by rememberSaveable { mutableStateOf("") }

        var confirmPassword by rememberSaveable { mutableStateOf("") }

        var passwordHidden by rememberSaveable { mutableStateOf(true) }

        var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

        var isErrorPassword by rememberSaveable { mutableStateOf(false)}

        var isErrorConfirm by rememberSaveable { mutableStateOf(false)}

        fun validatePassword(text: String) {
            isErrorPassword =!text.isValidPassword()
        }
        fun validateConfirmPassword(text: String) {
            isErrorConfirm =!text.isValidPassword()
        }

        var isPasswordTheSame by rememberSaveable { mutableStateOf(true) }

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = password,
            onValueChange = { password = it
                  isErrorPassword = false},
            label = {
                val label = if (!isErrorPassword) "Password" else "Password*"
                Text(label)},
            isError = isErrorPassword,
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onNext = {focusRequester.requestFocus()}
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
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (password.count() > 0) {
                        validatePassword(password)
                    }
                },
            singleLine = true
        )
        if (!password.isValidPassword() && password.isNotEmpty()) {
            if(!isPasswordTheSame){
                ErrorMessage("Password and confirm password are different!")
            }
            ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")
        }
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = {
                val label = if (!isErrorConfirm) "Confirm Password" else "Confirm Password*"
                Text(label)},
            isError = isErrorConfirm,
            visualTransformation =
            if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions (
                onDone = {
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                    val visibilityIcon =
                        if (confirmPasswordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (confirmPasswordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (confirmPassword.count() > 0) {
                        validateConfirmPassword(confirmPassword)
                    }
                },
            singleLine = true
        )
        if (!confirmPassword.isValidPassword()&&confirmPassword.count()>0) {

            ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")

        }
        isPasswordTheSame = if (password != confirmPassword) {
            ErrorMessage("Password and confirm password are different!")
            false
        } else true

        if(password.isValidPassword()&&isPasswordTheSame){

            return password
        }

        return ""
    }


    @Composable
    fun SignUPButton() {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                  goToListFragment()
                  Log.wtf(TAG, "Button signUp")
                },
                modifier = Modifier.size(width = 150.dp, height = 35.dp),
                content = {
                    Text(text = "Sign Up")
                })
            // Spacer(modifier = Modifier.padding(top = 8.dp))
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
                        goToLoginFragment()
                    },
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
    }
}