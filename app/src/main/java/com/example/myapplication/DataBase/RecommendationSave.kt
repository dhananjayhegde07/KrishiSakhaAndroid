package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.myapplication.DTO.CropRecReq
import com.example.myapplication.DTO.CropRecommendationModel


@Entity("RecommendationSave")
class RecommendationSave{
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
    var username: String=""
    var timestamp: String=""
    var input: CropRecReq?=null
    var result : CropRecommendationModel?=null
}

@Dao
interface RecommendationDao{
    @Insert
    fun save(data: RecommendationSave)

    @Query("select * from RecommendationSave where username = :username")
    fun get(username: String): List<RecommendationSave>
}