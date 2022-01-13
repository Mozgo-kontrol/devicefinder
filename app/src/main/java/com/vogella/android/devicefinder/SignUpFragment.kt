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
                SignUp()
            }

        }
    }

    @Composable
    @Preview(showBackground = true, name = "Light Mode")
    fun SignUp() {
        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize()) {
             //   TopAppBar(title = {
               //     Text("Device Finder")
             //   })
                Title("SignUp")
                DefaultRecipeCard()
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
    //  @Preview
    fun DefaultRecipeCard() {
        MaterialTheme {
            RecipeCard(Modifier.padding(4.dp))
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
    fun inputFields() {

        val (focusRequester) = FocusRequester.createRefs()
        Column(modifier = Modifier.fillMaxWidth()) {
            nameInputField(focusRequester)
            emailInputField(focusRequester)
            passwordsInputField(focusRequester)
            SignUPButton()

        }
    }

    @Composable
    fun nameInputField(focusRequester: FocusRequester) {
        var username by rememberSaveable { mutableStateOf("") }
        TextField(
            value = username,
            label = { Text("Name") },
            textStyle = TextStyle(fontWeight = FontWeight.Bold),
            onValueChange = { username = it },
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
        )

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

        var confirmPassword by rememberSaveable { mutableStateOf("") }

        var passwordHidden by rememberSaveable { mutableStateOf(true) }

        var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

        var isPasswordTheSame by rememberSaveable { mutableStateOf(true) }

        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
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
                .focusRequester(focusRequester),
            singleLine = true
        )
        if (!password.isValidPassword() && password.count()>0) {
            if(!isPasswordTheSame){
                ErrorMessage("Password and confirm password are different!")
            }
            ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")
        }
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
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
                .focusRequester(focusRequester),
            singleLine = true
        )
        if (!confirmPassword.isValidPassword()&&confirmPassword.count()>0) {
            ErrorMessage("Password consist of numbers, uppercase, lowercase, special and at least 7")

        }
        isPasswordTheSame = if (password != confirmPassword && confirmPassword.count()>0) {
            ErrorMessage("Password and confirm password are different!")
            false
        } else true
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
        Text(text = "If you already have an account: here!",
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { Log.wtf(TAG, "go to Login")
                    goToLoginFragment()
                           },
            color = Color.Blue,
            textAlign = TextAlign.Center,
        )
    }

}