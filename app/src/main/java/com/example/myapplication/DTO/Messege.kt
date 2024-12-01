package com.example.myapplication.DTO

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Messege(
    var from: String,
    var to: String,
    var msg: String,
    var index: Long
): Parcelable
