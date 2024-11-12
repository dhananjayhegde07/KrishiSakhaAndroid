package com.example.myapplication.singleton

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions

class NavViewModel: ViewModel() {
    var navController: NavController?=null

    fun navigate(route:String, navOptions: NavOptions?=null){
        navController?.navigate(route, navOptions)
    }
}