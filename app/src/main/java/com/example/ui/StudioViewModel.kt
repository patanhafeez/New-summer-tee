package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.api.Content
import com.example.api.GenerateContentRequest
import com.example.api.Part
import com.example.api.RetrofitClient
import com.example.data.AppDatabase
import com.example.data.DesignConfig
import com.example.data.DesignRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TShirtPreset(
    val title: String,
    val styleName: String,
    val description: String,
    val defaultSlogan: String,
    val defaultScale: Float = 0.75f,
    val defaultFade: Float = 1.0f
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = DesignRepository(database.designConfigDao())

    val savedConfigs: StateFlow<List<DesignConfig>> = repository.allConfigs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val presets = listOf(
        TShirtPreset(
            title = "Cyber Botanical",
            styleName = "Bio-Wireframe",
            description = "Luminous biological flora intersecting with neon circuit board network lines.",
            defaultSlogan = "NEON REWILD"
        ),
        TShirtPreset(
            title = "Retro-Futurism",
            styleName = "Outrun Cyberpunk",
            description = "Galactic neon landscapes featuring astronauts with retro vector grid elements.",
            defaultSlogan = "CHROME HORIZON"
        ),
        TShirtPreset(
            title = "Scandi Minimal Geo",
            styleName = "Nordic Geometric",
            description = "Earthy scandinavian minimal mountain lines under a large flat golden sun.",
            defaultSlogan = "STORLAND"
        ),
        TShirtPreset(
            title = "Techwear Cyber",
            styleName = "Industrial Core",
            description = "Tactical urban cybernetic frame accents with sharp micro-text overlays.",
            defaultSlogan = "M-SYS / UNIT 8"
        ),
        TShirtPreset(
            title = "Classical Vaporwave",
            styleName = "Aesthetic Retro",
            description = "Glitch classical marble statue head wearing sleek neon VR visor accessories.",
            defaultSlogan = "LOST INFOSPHERE"
        )
    )

    // User Interactive states
    var selectedPresetIndex by mutableStateOf(0)
    var shirtColorHex by mutableStateOf("#EAEDED") // Light off-white default
    var shirtFit by mutableStateOf("Oversized") // Regular, Oversized, Cropped
    var placement by mutableStateOf("Chest") // Chest, Large Back, Pocket Size
    var sloganText by mutableStateOf("NEON REWILD")
    var designScale by mutableStateOf(0.70f)
    var designFade by mutableStateOf(1.0f)

    var customAiPrompt by mutableStateOf("")
    var isAiGenerating by mutableStateOf(false)
    var aiError by mutableStateOf<String?>(null)
    var aiStyleAdvice by mutableStateOf("Style Advice: Pair with cargo pants or high-top sneakers.")

    private val apiService = RetrofitClient.service

    init {
        // Initialize default slogan matching the first preset
        updatePreset(0)
    }

    fun updatePreset(index: Int) {
        selectedPresetIndex = index
        val preset = presets[index]
        sloganText = preset.defaultSlogan
        designScale = preset.defaultScale
        designFade = preset.defaultFade
    }

    fun loadSavedConfig(config: DesignConfig) {
        selectedPresetIndex = config.designIndex
        shirtColorHex = config.shirtColorHex
        shirtFit = config.shirtFit
        placement = config.placement
        sloganText = config.slogan
        designScale = config.scale
        designFade = config.fade
        aiStyleAdvice = "Reloaded design: ${config.name}"
    }

    fun triggerAiSloganGeneration(userVibePrompt: String = "") {
        viewModelScope.launch {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (apiKey == "MY_GEMINI_API_KEY" || apiKey.isBlank()) {
                aiError = "API Key is missing! Set GEMINI_API_KEY in the Secrets panel."
                return@launch
            }

            isAiGenerating = true
            aiError = null

            val themeName = presets[selectedPresetIndex].title
            val themeDesc = presets[selectedPresetIndex].description
            
            val instructions = "As an avant-garde apparel fashion designer modeling a t-shirt collection for '$themeName' (representing: $themeDesc). " +
                    "Your task is to write a highly creative, extremely short runway-style slogan (maximum 1 to 3 words) and a 1-sentence streetwear style advice." +
                    if (userVibePrompt.isNotBlank()) " Add this custom vibe requested by the customer: '$userVibePrompt'." else "" +
                    " Return strictly in JSON format as {\"slogan\": \"SLOGAN_TEXT\", \"advice\": \"ADVICE_TEXT\"} with no other markdown wrappers."

            val request = GenerateContentRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = instructions)))
                )
            )

            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.generateContent(apiKey, request)
                }
                val resultText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (resultText != null) {
                    // Clean up potential JSON formatting wrap from LLM
                    val cleanJson = resultText.trim()
                        .removePrefix("```json")
                        .removeSuffix("```")
                        .trim()

                    // Parse manually or using Simple JSON parsing to avoid heavy Moshi boilerplate error-handling
                    if (cleanJson.contains("\"slogan\"") && cleanJson.contains("\"advice\"")) {
                        val parsedSlogan = cleanJson.substringAfter("\"slogan\"").substringAfter(":").substringBefore(",").replace("\"", "").trim()
                        val parsedAdvice = cleanJson.substringAfter("\"advice\"").substringAfter(":").substringBefore("}").replace("\"", "").trim()

                        if (parsedSlogan.isNotBlank()) {
                            sloganText = parsedSlogan.uppercase()
                        }
                        if (parsedAdvice.isNotBlank()) {
                            aiStyleAdvice = "Advice: $parsedAdvice"
                        }
                    } else {
                        // Fallback simple parsing
                        sloganText = resultText.take(15).uppercase()
                        aiStyleAdvice = resultText
                    }
                } else {
                    aiError = "No AI generation response."
                }
            } catch (e: Exception) {
                // If the dynamic prompt fails, use our elegant offline backup generators
                aiError = "API Connection limits. Local fallback generator activated."
                generateMockSlogan()
            } finally {
                isAiGenerating = false
            }
        }
    }

    private fun generateMockSlogan() {
        val backups = listOf(
            listOf("CORTEX DRIVE", "KINETIC BIO", "GRID OVERRIDE", "STEM PHASE"),
            listOf("NEO-FLIGHT", "DRIFT VELOCITY", "RETRO LIGHTSPAN", "PULSE SECTOR"),
            listOf("SENSE LAND", "NORD-GEOM", "FJORD ECLIPSE", "ASPECT BALANCE"),
            listOf("SYS REBOOT", "TACTICAL OUTFLOW", "HEX ENGINE", "CHIP-SET 01"),
            listOf("RECREATION", "PIXEL MONUMENT", "GLITCH VISOR", "ECHO ZONE")
        )
        val selectedBackups = backups[selectedPresetIndex]
        sloganText = selectedBackups.random()
        val advices = listOf(
            "Pair with dark cargo trousers and tech gloves.",
            "Style under an oversized retro denim crop jacket.",
            "Looks phenomenal with high-waisted neutral beige canvas trousers.",
            "Complete the fit with tactical harness and black sleek cargo sneakers.",
            "Sells well styled alongside baggy pastel skate pants and platform boots."
        )
        aiStyleAdvice = "Backup Advice: " + advices[selectedPresetIndex]
    }

    fun saveCurrentDesign(name: String) {
        viewModelScope.launch {
            val finalName = if (name.isBlank()) "Design #${System.currentTimeMillis() % 1000}" else name
            val config = DesignConfig(
                name = finalName,
                designIndex = selectedPresetIndex,
                shirtColorHex = shirtColorHex,
                shirtFit = shirtFit,
                placement = placement,
                slogan = sloganText,
                scale = designScale,
                fade = designFade
            )
            withContext(Dispatchers.IO) {
                repository.saveConfig(config)
            }
            aiStyleAdvice = "Successfully saved '$finalName' to Local Storage!"
        }
    }

    fun deleteDesign(config: DesignConfig) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteConfig(config)
            }
        }
    }

    fun clearAllDesigns() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.clearAll()
            }
        }
    }
}
