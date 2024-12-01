package com.example.myapplication.DTO

import com.google.gson.annotations.SerializedName


data class DetectRes(
    val isHealthy: Boolean,
    val detected: Boolean,
    val confidence: Double,
    val disease: DiseasesModel? = null
)

data class DiseasesModel(
    @SerializedName("class_name")
    val className: String,

    @SerializedName("kannada_name")
    val kannadaName: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("kannada_description")
    val kannadaDescription: String,

    @SerializedName("cause")
    val cause: String,

    @SerializedName("kannada_cause")
    val kannadaCause: String,

    @SerializedName("recommended_actions")
    val recommendedActions: List<ActionDTO>
) {
    data class ActionDTO(
        @SerializedName("action")
        val action: String,

        @SerializedName("kannada_action")
        val kannadaAction: String
    )
}
