package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.myapplication.DTO.FertilizerRecommendModel
import com.example.myapplication.DTO.PredictionFerReq


@Entity("FertilizerSave")
class FertilizerSave{
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
    var username: String=""
    var timestamp: String=""
    var input: PredictionFerReq?=null
    var result:FertilizerRecommendModel?=null
}

@Dao
interface FertilizerDao{
    @Insert
    fun save(data: FertilizerSave)

    @Query("select * from fertilizersave where username = :username")
    fun get(username: String): List<FertilizerSave>
}