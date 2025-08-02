package com.example.sp.network


import com.example.sp.model.AIAnalysis
import kotlinx.coroutines.delay

class AIService {

    suspend fun analyzeMood(caption: String, photoUri: String): AIAnalysis {
        // Simülasyon: 1 saniye gecikme, sonra sahte analiz döndür
        delay(1000)

        // Basit bir örnek analiz — daha sonra burada OpenAI, Replicate, Hugging Face vs. kullanılabilir
        val pattern = if (caption.contains("nature", ignoreCase = true)) {
            "You feel better in nature"
        } else {
            "You tend to feel reflective indoors"
        }

        val summary = "In the past months, you mentioned '$pattern' multiple times. Keep exploring these environments."

        return AIAnalysis(pattern = pattern, summary = summary)
    }
}
