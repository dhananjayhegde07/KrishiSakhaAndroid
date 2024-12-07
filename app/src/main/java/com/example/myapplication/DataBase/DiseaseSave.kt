package com.example.myapplication.DataBase

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverters
import com.example.myapplication.DTO.DiseasesModel


@Entity("diseaseResult")
@TypeConverters(DiseaseModelTypeConverter::class)
class DiseaseSave {
    @PrimaryKey(autoGenerate = true)
    var id: Int=0
    var username:String=""
    var timestamp: String=""
    var url: String=""
    var result : DiseasesModel?=null
}

@Dao
interface DiseaseSaveDAO{

    @Insert
    fun save(data: DiseaseSave);

    @Query("select * from diseaseResult where username = :username")
    fun get(username: String): List<DiseaseSave>
}