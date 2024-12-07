package com.example.myapplication.DataBase

import androidx.room.TypeConverter
import com.example.myapplication.DTO.CropRecReq
import com.example.myapplication.DTO.CropRecommendationModel
import com.example.myapplication.DTO.DiseasesModel
import com.example.myapplication.DTO.FertilizerRecommendModel
import com.example.myapplication.DTO.PredictionFerReq
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DiseaseModelTypeConverter{
    private val gson = Gson()

    @TypeConverter
    fun fromDiseasesModel(diseasesModel: DiseasesModel?): String? {
        return gson.toJson(diseasesModel)
    }

    @TypeConverter
    fun toDiseasesModel(data: String?): DiseasesModel? {
        return gson.fromJson(data, object : TypeToken<DiseasesModel?>() {}.type)
    }
}

class CropRecReqTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCropRecReq(cropRecReq: CropRecReq?): String? {
        return gson.toJson(cropRecReq) // Serialize to JSON
    }

    @TypeConverter
    fun toCropRecReq(json: String?): CropRecReq? {
        return gson.fromJson(json, CropRecReq::class.java) // Deserialize back to object
    }
}

class CropRecommendationModelTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromCropRecommendationModel(model: CropRecommendationModel?): String? {
        return gson.toJson(model) // Serialize to JSON
    }

    @TypeConverter
    fun toCropRecommendationModel(json: String?): CropRecommendationModel? {
        return gson.fromJson(json, CropRecommendationModel::class.java) // Deserialize back to object
    }
}

class PredictionFerReqTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromPredictionFerReq(req: PredictionFerReq?): String? {
        return gson.toJson(req) // Convert to JSON
    }

    @TypeConverter
    fun toPredictionFerReq(json: String?): PredictionFerReq? {
        return gson.fromJson(json, PredictionFerReq::class.java) // Convert back to object
    }
}

class FertilizerRecommendModelTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromFertilizerRecommendModel(model: FertilizerRecommendModel?): String? {
        return gson.toJson(model) // Convert the entire object, including nested fields, to JSON
    }

    @TypeConverter
    fun toFertilizerRecommendModel(json: String?): FertilizerRecommendModel? {
        return gson.fromJson(json, FertilizerRecommendModel::class.java) // Convert JSON back to the object
    }
}