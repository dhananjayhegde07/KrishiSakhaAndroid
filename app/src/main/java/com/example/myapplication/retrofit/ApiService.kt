package com.example.myapplication.retrofit

import com.example.myapplication.DTO.DetectReq
import com.example.myapplication.DTO.LoginReq
import com.example.myapplication.DTO.LoginRes
import com.example.myapplication.DTO.OtpReq
import com.example.myapplication.DTO.OtpRes
import com.example.myapplication.DTO.ResendOtpReq
import com.example.myapplication.DTO.SignUpReq
import com.example.myapplication.DTO.SignUpRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("/public/login")
    suspend fun login(@Body loginReq: LoginReq): LoginRes

    @POST("/public/signup")
    suspend fun signup(@Body signUpReq: SignUpReq) : SignUpRes

    @POST("/public/varify")
    suspend fun validate(@Body otpReq: OtpReq): OtpRes

    @POST("/public/resendotp")
    suspend fun resendOtp(@Body re:ResendOtpReq): Response<Unit>

    @POST("/getUser")
    suspend fun getUser(): Map<String, Any>

    @Multipart
    @POST("/detect")
    suspend fun detect(
        @Part() image: MultipartBody.Part,
        @Part("data") data: DetectReq
    ): Response<Unit>
}