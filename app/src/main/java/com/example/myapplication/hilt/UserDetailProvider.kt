package com.example.myapplication.hilt

import javax.inject.Inject

class UserDetailProvider  {
    @Inject
    lateinit var userDetailsModel: UserDetailsModel

}