package com.example.myapplication.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UserDetails {

    @Provides
    @Singleton
    fun provideUserDetails(): UserDetailsModel {
        return UserDetailsModel()
    }

}