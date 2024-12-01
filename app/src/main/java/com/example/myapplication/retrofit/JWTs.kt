package com.example.myapplication.retrofit

import android.util.Log
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.userDetail
import okhttp3.Interceptor
import okhttp3.Response

class JWTs: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val req=chain.request();
        val newReq=req.newBuilder().addHeader("Authentication","bearer ${userDetail.jwt}").build()
        val res=chain.proceed(newReq);
        Log.d("TAG", "intercept: ${res.body()}")
        if (res.code()==401){
            val newJwt=res.header("Authentication")
            Log.d("TAG", "intercept: ${newJwt}")
            if (newJwt==null){
                GlobalStates.globalStates.navController?.navigate("login")
                return res
            }
            userDetail.jwt=newJwt.substring(7);
            SharedPreference.saveData("jwt",userDetail.jwt)
            val newReq=req.newBuilder().addHeader("Authentication","bearer ${userDetail.jwt}").build()
            return chain.proceed(newReq);
        }
        return res
    }

}