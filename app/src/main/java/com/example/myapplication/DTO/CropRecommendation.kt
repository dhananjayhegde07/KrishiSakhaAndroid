package com.example.myapplication.DTO

data class CropRecReq(

    val n: Double, // Nitrogen content

    val p: Double, // Phosphorus content

    val k: Double, // Potassium content

    val temperature: Double, // Temperature in Celsius

    val humidity: Double, // Relative Humidity in percentage

    val ph: Double, // pH value of the soil

    val rainfall: Double // Rainfall in mm
)

data class CropRecommendationReq(
    var params: Boolean,
    var data: CropRecReq?
)

data class CropRecommendationModel(
    val name: String,
    val kannadaName: String,
    val scientificName: String,
    val climateRequirements: String,
    val soilType: String,
    val wateringNeeds: String,
    val growingSeason: String,
    val tips: Tips
){
    data class Tips(
        val kannada: List<String>,
        val english: List<String>
    )
}

data class CropRecommendationRes(
    val isAvailable: Boolean,
    val recommend: CropRecommendationModel?
)