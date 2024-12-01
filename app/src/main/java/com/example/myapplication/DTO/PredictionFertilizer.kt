package com.example.myapplication.DTO

data class PredictionFerReq(

    val n: Double,  // Nitrogen content
    val p: Double,  // Phosphorus content
    val k: Double   // Potassium content
)


data class FertilizerRecommendModel(
    val name: String,

    val scientificName: String? = null,
    val NPK_Composition: String? = null,
    val recommendedCrops: List<String>? = null,
    val applicationRate: String? = null,
    val bestTimeToApply: String? = null,
    val benefits: List<String>? = null,
    val potentialRisks: List<String>? = null,
    val storageAndHandling: String? = null,
    val costEstimate: String? = null,
    val organicVsSynthetic: String? = null,
    val tips: Tips? = null
) {
    data class Tips(
        val kannada: List<String>? = null,
        val english: List<String>? = null
    )
}

data class PredictionFerRes(
    val recommend: FertilizerRecommendModel? = null,
    val isAvailable: Boolean? = null

)