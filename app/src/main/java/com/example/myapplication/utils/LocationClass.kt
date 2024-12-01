package com.example.myapplication.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationClass(
    var longitude:Double,
    var latitude:Double
): Parcelable
