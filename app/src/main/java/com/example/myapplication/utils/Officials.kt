package com.example.myapplication.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Officials(
    var id: String? = null,
    var name: String? = null,
    var pin: String? = null,
    var phone: String? = null,
    var email: String? = null
): Parcelable
