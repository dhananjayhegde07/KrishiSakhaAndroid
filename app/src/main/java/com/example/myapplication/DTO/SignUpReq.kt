package com.example.myapplication.DTO



data class SignUpReq(
    var username: String? = null,
    var password: String? = null,
    var repassword: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address: AddressDTO? = null
)

data class AddressDTO(
    var state: String? = null,
    var district: String? = null,
    var pin: String? = null,
)