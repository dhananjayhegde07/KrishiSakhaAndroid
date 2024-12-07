package com.example.myapplication.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities =
    [User::class, DiseaseSave::class, RecommendationSave::class, FertilizerSave::class],
    version = 3, exportSchema = false)
@TypeConverters(
    DiseaseModelTypeConverter::class,
    CropRecReqTypeConverter::class,
    CropRecommendationModelTypeConverter::class,
    PredictionFerReqTypeConverter::class,
    FertilizerRecommendModelTypeConverter::class)
abstract class DataBase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun diseaseDao(): DiseaseSaveDAO
    abstract fun recommendationDao(): RecommendationDao
    abstract fun fertilizerDao(): FertilizerDao
}



