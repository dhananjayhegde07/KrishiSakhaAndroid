package com.example.myapplication.singleton

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

object TranslatorObject {
    val translator: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.KANNADA)
            .build()
    )
}


fun translateEnglishToKannada(
    text: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val translator = TranslatorObject.translator

    // Download the model if necessary
    translator.downloadModelIfNeeded(DownloadConditions.Builder().build())
        .addOnSuccessListener {
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    onSuccess(translatedText)
                }
                .addOnFailureListener { e ->
                    onError("Translation failed: ${e.message}")
                }
        }
        .addOnFailureListener { e ->
            onError("Model download failed: ${e.message}")
        }
}