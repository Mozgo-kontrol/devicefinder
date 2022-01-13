package com.vogella.android.devicefinder.navigation

sealed class Screen(val route:String){
    object LoginFragmentScreen : Screen("login_fragment_screen")
    object SignUpFragmentScreen : Screen("sign_up_fragment_screen")
}
