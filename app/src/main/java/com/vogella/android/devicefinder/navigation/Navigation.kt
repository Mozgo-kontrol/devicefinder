package com.vogella.android.devicefinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
    fun Navigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginFragmentScreen.route){

        composable(route = Screen.LoginFragmentScreen.route){
            LoginScreen(navController = navController)

        }
        composable(route = Screen.SignUpFragmentScreen.route){
            SignUpScreen(navController = navController)
        }
    }
}

@Composable
fun LoginScreen(navController: NavController) {
   // navController.navigate()
}

@Composable
fun SignUpScreen(navController: NavController) {
    //navController.navigate()
}
