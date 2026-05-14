package com.aipostermaker.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aipostermaker.domain.model.BackgroundStyle
import com.aipostermaker.domain.model.PosterContent
import com.aipostermaker.domain.model.PosterElement
import com.aipostermaker.domain.usecase.GenerateAiTextUseCase
import com.aipostermaker.domain.usecase.SavePosterUseCase
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

@Immutable
data class PosterEditorUiState(
    val title: String = "Namma Poster",
    val backgroundColorArgb: Int = 0xFFFFFFFF.toInt(),
    val elements: List<PosterElement> = emptyList(),
    val selectedElementId: String? = null,
    val isGeneratingAiText: Boolean = false,
    val isSavingToDb: Boolean = false,
    val message: String? = null,
    val error: String? = null,
)

enum class PosterTemplatePreset {
    SALE_BLAST,
    GRAND_OPENING,
    CAFE_SPECIAL,
    KANNADA_FESTIVAL,
    KANNADA_SONG,
    KANNADA_AD_RETAIL,
    KANNADA_AD_FOOD,
    KANNADA_AD_EDUCATION,
    KANNADA_BEAUTY_SALON,
    KANNADA_REAL_ESTATE,
    KANNADA_HEALTH_CAMP,
    KANNADA_BIRTHDAY,
}

enum class TextStylePreset {
    HEADING,
    SUBHEADING,
    BODY,
}

class PosterEditorViewModel(
    private val savePosterUseCase: SavePosterUseCase,
    private val generateAiTextUseCase: GenerateAiTextUseCase,
    private val gson: Gson = Gson(),
) : ViewModel() {

    private val _uiState = MutableStateFlow(PosterEditorUiState())
    val uiState: StateFlow<PosterEditorUiState> = _uiState.asStateFlow()

    fun setTitle(title: String) {
        _uiState.update { it.copy(title = title, error = null, message = null) }
    }

    fun addTextElement() {
        val id = UUID.randomUUID().toString()
        val new =
            PosterElement.Text(
                id = id,
                text = "Tap to edit",
                colorArgb = 0xFF111111.toInt(),
                fontSizeSp = 28f,
                fontFamilyName = "Default",
                offsetX = 0f,
                offsetY = 0f,
                scale = 1f,
                rotationDeg = 0f,
            )
        _uiState.update { it.copy(elements = it.elements + new, selectedElementId = id, error = null, message = null) }
    }

    fun addImageElement(uriString: String, cropRatio: Float? = null) {
        val id = UUID.randomUUID().toString()
        val new =
            PosterElement.Image(
                id = id,
                uriString = uriString,
                sizeDp = 220f,
                cropRatio = cropRatio,
                cornerRadiusDp = 12f,
                borderWidthDp = 1f,
                borderColorArgb = 0x66FFFFFF,
                offsetX = 24f,
                offsetY = 90f,
                scale = 1f,
                rotationDeg = 0f,
            )
        _uiState.update { it.copy(elements = it.elements + new, selectedElementId = id, error = null, message = null) }
    }

    fun setSelectedImageCropRatio(ratio: Float?) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el else (el as? PosterElement.Image)?.copy(cropRatio = ratio) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun setSelectedImageCornerRadius(radiusDp: Float) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else (el as? PosterElement.Image)?.copy(cornerRadiusDp = radiusDp.coerceIn(0f, 40f)) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun setSelectedImageBorder(widthDp: Float, colorArgb: Int? = null) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else {
                        val image = el as? PosterElement.Image ?: return@map el
                        image.copy(
                            borderWidthDp = widthDp.coerceIn(0f, 12f),
                            borderColorArgb = colorArgb ?: image.borderColorArgb,
                        )
                    }
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun selectElement(id: String?) {
        _uiState.update { it.copy(selectedElementId = id, error = null, message = null) }
    }

    fun updateElementOffset(id: String, dx: Float, dy: Float) {
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else {
                        when (el) {
                            is PosterElement.Text -> el.copy(offsetX = el.offsetX + dx, offsetY = el.offsetY + dy)
                            is PosterElement.Image -> el.copy(offsetX = el.offsetX + dx, offsetY = el.offsetY + dy)
                        }
                    }
                }
            state.copy(elements = updated)
        }
    }

    fun updateSelectedText(newText: String) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else (el as? PosterElement.Text)?.copy(text = newText) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun changeSelectedTextColor(nextColorArgb: Int) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else (el as? PosterElement.Text)?.copy(colorArgb = nextColorArgb) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun changeSelectedTextFontSize(nextSizeSp: Float) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el
                    else (el as? PosterElement.Text)?.copy(fontSizeSp = nextSizeSp) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun changeBackgroundColor(nextColorArgb: Int) {
        _uiState.update { it.copy(backgroundColorArgb = nextColorArgb, error = null, message = null) }
    }

    fun applyBrandColor(nextColorArgb: Int) {
        _uiState.update { it.copy(backgroundColorArgb = nextColorArgb, error = null, message = null) }
    }

    private data class TemplateConfig(
        val title: String,
        val backgroundColorArgb: Int,
        val elements: List<PosterElement>,
    )

    private fun textLayer(
        text: String,
        colorArgb: Int,
        fontSizeSp: Float,
        x: Float,
        y: Float,
        fontFamilyName: String = "SansSerif",
        scale: Float = 1f,
    ): PosterElement.Text =
        PosterElement.Text(
            id = UUID.randomUUID().toString(),
            text = text,
            colorArgb = colorArgb,
            fontSizeSp = fontSizeSp,
            fontFamilyName = fontFamilyName,
            offsetX = x,
            offsetY = y,
            scale = scale,
            rotationDeg = 0f,
        )

    private fun imageLayer(
        drawableName: String,
        x: Float,
        y: Float,
        sizeDp: Float = 190f,
        cropRatio: Float = 4f / 3f,
    ): PosterElement.Image =
        PosterElement.Image(
            id = UUID.randomUUID().toString(),
            uriString = "android.resource://com.aipostermaker/drawable/$drawableName",
            sizeDp = sizeDp,
            cropRatio = cropRatio,
            cornerRadiusDp = 18f,
            borderWidthDp = 2f,
            borderColorArgb = 0xAAFFFFFF.toInt(),
            offsetX = x,
            offsetY = y,
            scale = 1f,
            rotationDeg = 0f,
        )

    private fun templateConfig(template: PosterTemplatePreset): TemplateConfig =
        when (template) {
            PosterTemplatePreset.SALE_BLAST ->
                TemplateConfig(
                    title = "Flash Sale Poster",
                    backgroundColorArgb = 0xFF6A1B9A.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_shop", 46f, 168f, 210f),
                            textLayer("FLASH SALE", 0xFFFFFFFF.toInt(), 40f, 24f, 36f),
                            textLayer("UP TO 50% OFF", 0xFFFFF176.toInt(), 26f, 24f, 98f),
                            textLayer("This weekend only", 0xFFFFFFFF.toInt(), 18f, 24f, 138f, "Serif"),
                        ),
                )
            PosterTemplatePreset.GRAND_OPENING ->
                TemplateConfig(
                    title = "Grand Opening Poster",
                    backgroundColorArgb = 0xFFE8EAF6.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_event", 46f, 170f, 210f),
                            textLayer("GRAND OPENING", 0xFF1A237E.toInt(), 36f, 22f, 44f, "Serif"),
                            textLayer("Sunday 10:00 AM", 0xFF283593.toInt(), 22f, 24f, 104f),
                            textLayer("123 Main Street, Your City", 0xFF303F9F.toInt(), 17f, 24f, 144f, "Monospace"),
                        ),
                )
            PosterTemplatePreset.CAFE_SPECIAL ->
                TemplateConfig(
                    title = "Cafe Special Poster",
                    backgroundColorArgb = 0xFFFFF3E0.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_food", 48f, 166f, 210f),
                            textLayer("CAFE SPECIAL", 0xFF3E2723.toInt(), 35f, 24f, 38f, "Serif"),
                            textLayer("Buy 1 Get 1 Free", 0xFF5D4037.toInt(), 24f, 24f, 96f),
                            textLayer("Freshly brewed all day", 0xFF6D4C41.toInt(), 18f, 24f, 136f, "Cursive"),
                        ),
                )
            PosterTemplatePreset.KANNADA_FESTIVAL ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಹಬ್ಬ ಪೋಸ್ಟರ್",
                    backgroundColorArgb = 0xFF1B5E20.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_event", 48f, 170f, 210f),
                            textLayer("ಕನ್ನಡ ರಾಜ್ಯೋತ್ಸವದ ಶುಭಾಶಯಗಳು", 0xFFFFFFFF.toInt(), 27f, 20f, 34f, "Serif"),
                            textLayer("ನಮ್ಮ ನಾಡು, ನಮ್ಮ ಹೆಮ್ಮೆ", 0xFFFFF176.toInt(), 23f, 20f, 94f),
                            textLayer("ಕನ್ನಡದಲ್ಲಿ ಮಾತನಾಡಿ, ಕನ್ನಡವನ್ನು ಉಳಿಸಿ", 0xFFFFFFFF.toInt(), 18f, 20f, 138f),
                        ),
                )
            PosterTemplatePreset.KANNADA_SONG ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಸಂಗೀತ ಪೋಸ್ಟರ್",
                    backgroundColorArgb = 0xFFFFF8E1.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_event", 48f, 166f, 210f),
                            textLayer("ಕನ್ನಡ ಹಾಡು ವಿಶೇಷ", 0xFF212121.toInt(), 31f, 18f, 30f, "Cursive"),
                            textLayer("ಹಾಡೇ ಹಾಡು, ಹೃದಯದ ನಾದ", 0xFF4E342E.toInt(), 22f, 18f, 88f, "Serif"),
                            textLayer("ಇಂದು ಸಂಜೆ 6ಕ್ಕೆ - ಲೈವ್ ಸಂಗೀತ", 0xFF6D4C41.toInt(), 18f, 18f, 130f),
                        ),
                )
            PosterTemplatePreset.KANNADA_AD_RETAIL ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಅಂಗಡಿ ಆಫರ್",
                    backgroundColorArgb = 0xFFB71C1C.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_shop", 48f, 170f, 210f),
                            textLayer("ಮೆಗಾ ಆಫರ್ ಆರಂಭ!", 0xFFFFFFFF.toInt(), 34f, 18f, 28f),
                            textLayer("ಎಲ್ಲಾ ವಸ್ತುಗಳ ಮೇಲೆ 40% ವರೆಗೆ ರಿಯಾಯಿತಿ", 0xFFFFF176.toInt(), 20f, 18f, 86f, "Serif"),
                            textLayer("ಇಂದು ಮಾತ್ರ | ನಿಮ್ಮ ನಗರದ ಶಾಖೆ", 0xFFFFFFFF.toInt(), 18f, 18f, 130f),
                        ),
                )
            PosterTemplatePreset.KANNADA_AD_FOOD ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಹೋಟೆಲ್ ಆಫರ್",
                    backgroundColorArgb = 0xFFFFF3E0.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_food", 48f, 166f, 210f),
                            textLayer("ರುಚಿಯ ಹೊಸ ಸವಿರುಚಿ!", 0xFF3E2723.toInt(), 32f, 18f, 28f, "Cursive"),
                            textLayer("ಫ್ಯಾಮಿಲಿ ಕಾಂಬೋ ಕೇವಲ ₹299", 0xFF5D4037.toInt(), 22f, 18f, 84f),
                            textLayer("ಈ ವಾರ ವಿಶೇಷ ಆಫರ್ - ಈಗಲೇ ಭೇಟಿ ನೀಡಿ", 0xFF6D4C41.toInt(), 18f, 18f, 128f, "Serif"),
                        ),
                )
            PosterTemplatePreset.KANNADA_AD_EDUCATION ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಪ್ರವೇಶಾತಿ ಜಾಹೀರಾತು",
                    backgroundColorArgb = 0xFF0D47A1.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_education", 48f, 170f, 210f),
                            textLayer("ಪ್ರವೇಶ ಆರಂಭವಾಗಿದೆ", 0xFFFFFFFF.toInt(), 32f, 18f, 30f, "Serif"),
                            textLayer("SSLC | PUC | CET ವಿಶೇಷ ತರಗತಿಗಳು", 0xFFFFF176.toInt(), 20f, 18f, 88f),
                            textLayer("ಸೀಮಿತ ಸೀಟುಗಳು - ಇಂದೇ ನೋಂದಣಿ ಮಾಡಿ", 0xFFFFFFFF.toInt(), 18f, 18f, 132f),
                        ),
                )
            PosterTemplatePreset.KANNADA_BEAUTY_SALON ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಬ್ಯೂಟಿ ಸಲೂನ್ ಆಫರ್",
                    backgroundColorArgb = 0xFFFCE4EC.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_event", 48f, 166f, 210f),
                            textLayer("ಬ್ಯೂಟಿ ಸಲೂನ್ ಸ್ಪೆಷಲ್", 0xFF880E4F.toInt(), 30f, 18f, 28f, "Serif"),
                            textLayer("ಬ್ರೈಡಲ್ ಮೇಕಪ್ ಬುಕ್ಕಿಂಗ್ ಆರಂಭ", 0xFFC2185B.toInt(), 21f, 18f, 86f),
                            textLayer("ಇಂದೇ ಕರೆ ಮಾಡಿ | ಸೀಮಿತ ಸ್ಲಾಟ್‌ಗಳು", 0xFF4A102A.toInt(), 18f, 18f, 128f),
                        ),
                )
            PosterTemplatePreset.KANNADA_REAL_ESTATE ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ರಿಯಲ್ ಎಸ್ಟೇಟ್ ಪೋಸ್ಟರ್",
                    backgroundColorArgb = 0xFFE8F5E9.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_shop", 48f, 166f, 210f),
                            textLayer("ಹೊಸ ಸೈಟ್‌ಗಳು ಮಾರಾಟಕ್ಕೆ", 0xFF1B5E20.toInt(), 30f, 18f, 28f, "Serif"),
                            textLayer("ನಗರದ ಹತ್ತಿರ | ಸುಲಭ EMI", 0xFF2E7D32.toInt(), 22f, 18f, 86f),
                            textLayer("ಇಂದು ಭೇಟಿ ನೀಡಿ, ನಾಳೆಯ ಮನೆ ಕಟ್ಟಿರಿ", 0xFF1B5E20.toInt(), 18f, 18f, 128f),
                        ),
                )
            PosterTemplatePreset.KANNADA_HEALTH_CAMP ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಆರೋಗ್ಯ ಶಿಬಿರ",
                    backgroundColorArgb = 0xFFE3F2FD.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_education", 48f, 166f, 210f),
                            textLayer("ಉಚಿತ ಆರೋಗ್ಯ ತಪಾಸಣೆ", 0xFF0D47A1.toInt(), 30f, 18f, 30f, "Serif"),
                            textLayer("ಭಾನುವಾರ ಬೆಳಿಗ್ಗೆ 9ರಿಂದ", 0xFFC62828.toInt(), 22f, 18f, 88f),
                            textLayer("ನಿಮ್ಮ ಕುಟುಂಬದ ಆರೋಗ್ಯಕ್ಕೆ ಮೊದಲ ಆದ್ಯತೆ", 0xFF0D47A1.toInt(), 18f, 18f, 130f),
                        ),
                )
            PosterTemplatePreset.KANNADA_BIRTHDAY ->
                TemplateConfig(
                    title = "ಕನ್ನಡ ಹುಟ್ಟುಹಬ್ಬ ಪೋಸ್ಟರ್",
                    backgroundColorArgb = 0xFFFFF8E1.toInt(),
                    elements =
                        listOf(
                            imageLayer("template_event", 48f, 168f, 210f),
                            textLayer("ಹುಟ್ಟುಹಬ್ಬದ ಶುಭಾಶಯಗಳು", 0xFFC62828.toInt(), 31f, 18f, 28f, "Cursive"),
                            textLayer("ನಿಮ್ಮ ದಿನ ಸಂತೋಷದಿಂದ ತುಂಬಿರಲಿ", 0xFF6D4C41.toInt(), 21f, 18f, 88f, "Serif"),
                            textLayer("ಪ್ರೀತಿಯಿಂದ - ನಿಮ್ಮ ಸ್ನೇಹಿತರು", 0xFF1B5E20.toInt(), 18f, 18f, 130f),
                        ),
                )
        }

    fun applyTemplate(template: PosterTemplatePreset) {
        val config = templateConfig(template)
        _uiState.update {
            it.copy(
                title = config.title,
                backgroundColorArgb = config.backgroundColorArgb,
                elements = config.elements,
                selectedElementId = config.elements.firstOrNull()?.id,
                error = null,
                message = "Template applied. Use fingers to drag, pinch zoom, and rotate.",
            )
        }
    }

/*
        val elements =
            when (template) {
                PosterTemplatePreset.SALE_BLAST ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "FLASH SALE",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 36f,
                            fontFamilyName = "SansSerif",
                            offsetX = 24f,
                            offsetY = 44f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "UP TO 50% OFF",
                            colorArgb = 0xFFFFF176.toInt(),
                            fontSizeSp = 24f,
                            fontFamilyName = "SansSerif",
                            offsetX = 24f,
                            offsetY = 102f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "This weekend only",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Serif",
                            offsetX = 24f,
                            offsetY = 148f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.GRAND_OPENING ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "GRAND OPENING",
                            colorArgb = 0xFF1A237E.toInt(),
                            fontSizeSp = 34f,
                            fontFamilyName = "Serif",
                            offsetX = 24f,
                            offsetY = 50f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "Sunday 10:00 AM",
                            colorArgb = 0xFF283593.toInt(),
                            fontSizeSp = 22f,
                            fontFamilyName = "SansSerif",
                            offsetX = 24f,
                            offsetY = 110f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "123 Main Street, Your City",
                            colorArgb = 0xFF303F9F.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Monospace",
                            offsetX = 24f,
                            offsetY = 152f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.CAFE_SPECIAL ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "CAFE SPECIAL",
                            colorArgb = 0xFF3E2723.toInt(),
                            fontSizeSp = 34f,
                            fontFamilyName = "Serif",
                            offsetX = 24f,
                            offsetY = 44f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "Buy 1 Get 1 Free",
                            colorArgb = 0xFF5D4037.toInt(),
                            fontSizeSp = 24f,
                            fontFamilyName = "SansSerif",
                            offsetX = 24f,
                            offsetY = 102f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "Freshly brewed all day",
                            colorArgb = 0xFF6D4C41.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Cursive",
                            offsetX = 24f,
                            offsetY = 146f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.KANNADA_FESTIVAL ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಕನ್ನಡ ರಾಜ್ಯೋತ್ಸವದ ಶುಭಾಶಯಗಳು",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 28f,
                            fontFamilyName = "Serif",
                            offsetX = 20f,
                            offsetY = 34f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ನಮ್ಮ ನಾಡು, ನಮ್ಮ ಹೆಮ್ಮೆ",
                            colorArgb = 0xFFFFF176.toInt(),
                            fontSizeSp = 22f,
                            fontFamilyName = "SansSerif",
                            offsetX = 20f,
                            offsetY = 92f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಕನ್ನಡದಲ್ಲಿ ಮಾತಾಡಿ, ಕನ್ನಡವನ್ನು ಉಳಿಸಿ",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Default",
                            offsetX = 20f,
                            offsetY = 136f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.KANNADA_SONG ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಕನ್ನಡ ಹಾಡು ವಿಶೇಷ",
                            colorArgb = 0xFF212121.toInt(),
                            fontSizeSp = 30f,
                            fontFamilyName = "Cursive",
                            offsetX = 18f,
                            offsetY = 30f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಹಾಡೇ ಹಾಡು, ಹೃದಯದ ನಾದ",
                            colorArgb = 0xFF4E342E.toInt(),
                            fontSizeSp = 22f,
                            fontFamilyName = "Serif",
                            offsetX = 18f,
                            offsetY = 86f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಇಂದು ಸಂಜೆ 6ಕ್ಕೆ - ಲೈವ್ ಸಂಗೀತ ಕಾರ್ಯಕ್ರಮ",
                            colorArgb = 0xFF6D4C41.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "SansSerif",
                            offsetX = 18f,
                            offsetY = 130f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.KANNADA_AD_RETAIL ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಮೆಗಾ ಆಫರ್ ಆರಂಭ!",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 34f,
                            fontFamilyName = "SansSerif",
                            offsetX = 18f,
                            offsetY = 28f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಎಲ್ಲಾ ವಸ್ತುಗಳ ಮೇಲೆ 40% ವರೆಗೆ ರಿಯಾಯಿತಿ",
                            colorArgb = 0xFFFFF176.toInt(),
                            fontSizeSp = 20f,
                            fontFamilyName = "Serif",
                            offsetX = 18f,
                            offsetY = 86f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಇಂದು ಮಾತ್ರ | ನಿಮ್ಮ ನಗರದ ಶಾಖೆ",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Default",
                            offsetX = 18f,
                            offsetY = 130f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.KANNADA_AD_FOOD ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ರುಚಿಯ ಹೊಸ ಸವಿರುಚಿ!",
                            colorArgb = 0xFF3E2723.toInt(),
                            fontSizeSp = 32f,
                            fontFamilyName = "Cursive",
                            offsetX = 18f,
                            offsetY = 28f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಫ್ಯಾಮಿಲಿ ಕಾಂಬೋ ಕೇವಲ ₹299",
                            colorArgb = 0xFF5D4037.toInt(),
                            fontSizeSp = 22f,
                            fontFamilyName = "SansSerif",
                            offsetX = 18f,
                            offsetY = 84f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಈ ವಾರ ವಿಶೇಷ ಆಫರ್ - ಈಗಲೇ ಭೇಟಿ ನೀಡಿ",
                            colorArgb = 0xFF6D4C41.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Serif",
                            offsetX = 18f,
                            offsetY = 128f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                PosterTemplatePreset.KANNADA_AD_EDUCATION ->
                    listOf(
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಪ್ರವೇಶ ಆರಂಭವಾಗಿದೆ",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 32f,
                            fontFamilyName = "Serif",
                            offsetX = 18f,
                            offsetY = 30f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "SSLC | PUC | CET ವಿಶೇಷ ತರಗತಿಗಳು",
                            colorArgb = 0xFFFFF176.toInt(),
                            fontSizeSp = 20f,
                            fontFamilyName = "SansSerif",
                            offsetX = 18f,
                            offsetY = 88f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                        PosterElement.Text(
                            id = UUID.randomUUID().toString(),
                            text = "ಸೀಮಿತ ಸೀಟುಗಳು - ಇಂದೇ ನೋಂದಣಿ ಮಾಡಿ",
                            colorArgb = 0xFFFFFFFF.toInt(),
                            fontSizeSp = 18f,
                            fontFamilyName = "Default",
                            offsetX = 18f,
                            offsetY = 132f,
                            scale = 1f,
                            rotationDeg = 0f,
                        ),
                    )
                else -> config.elements
            }

        val (title, background) =
            when (template) {
                PosterTemplatePreset.SALE_BLAST -> "Flash Sale Poster" to 0xFF6A1B9A.toInt()
                PosterTemplatePreset.GRAND_OPENING -> "Grand Opening Poster" to 0xFFE8EAF6.toInt()
                PosterTemplatePreset.CAFE_SPECIAL -> "Cafe Special Poster" to 0xFFFFF3E0.toInt()
                PosterTemplatePreset.KANNADA_FESTIVAL -> "ಕನ್ನಡ ಹಬ್ಬ ಪೋಸ್ಟರ್" to 0xFF1B5E20.toInt()
                PosterTemplatePreset.KANNADA_SONG -> "ಕನ್ನಡ ಸಂಗೀತ ಪೋಸ್ಟರ್" to 0xFFFFF8E1.toInt()
                PosterTemplatePreset.KANNADA_AD_RETAIL -> "ಕನ್ನಡ ಅಂಗಡಿ ಆಫರ್" to 0xFFB71C1C.toInt()
                PosterTemplatePreset.KANNADA_AD_FOOD -> "ಕನ್ನಡ ಹೋಟೆಲ್ ಆಫರ್" to 0xFFFFF3E0.toInt()
                PosterTemplatePreset.KANNADA_AD_EDUCATION -> "ಕನ್ನಡ ಪ್ರವೇಶಾತಿ ಜಾಹೀರಾತು" to 0xFF0D47A1.toInt()
                else -> config.title to config.backgroundColorArgb
            }

        _uiState.update {
            it.copy(
                title = title,
                backgroundColorArgb = background,
                elements = elements,
                selectedElementId = elements.firstOrNull()?.id,
                error = null,
                message = "Template applied. Customize it as needed.",
            )
        }
    }

*/

    fun applyTextStylePreset(preset: TextStylePreset) {
        val id = _uiState.value.selectedElementId ?: return
        val fontSize =
            when (preset) {
                TextStylePreset.HEADING -> 40f
                TextStylePreset.SUBHEADING -> 28f
                TextStylePreset.BODY -> 18f
            }
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el else (el as? PosterElement.Text)?.copy(fontSizeSp = fontSize) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun applySelectedFontFamily(fontFamilyName: String) {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) el else (el as? PosterElement.Text)?.copy(fontFamilyName = fontFamilyName) ?: el
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun transformSelectedElement(scaleDelta: Float = 1f, rotationDelta: Float = 0f) {
        val id = _uiState.value.selectedElementId ?: return
        transformElement(id, scaleDelta, rotationDelta)
    }

    fun transformElement(id: String, scaleDelta: Float = 1f, rotationDelta: Float = 0f) {
        _uiState.update { state ->
            val updated =
                state.elements.map { el ->
                    if (el.id != id) {
                        el
                    } else {
                        val nextScale = (el.scale * scaleDelta).coerceIn(0.4f, 2.8f)
                        val nextRotation = el.rotationDeg + rotationDelta
                        when (el) {
                            is PosterElement.Text -> el.copy(scale = nextScale, rotationDeg = nextRotation)
                            is PosterElement.Image -> el.copy(scale = nextScale, rotationDeg = nextRotation)
                        }
                    }
                }
            state.copy(elements = updated, error = null, message = null)
        }
    }

    fun bringSelectedForward() {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val index = state.elements.indexOfFirst { it.id == id }
            if (index == -1 || index == state.elements.lastIndex) return@update state
            val reordered = state.elements.toMutableList()
            val selected = reordered.removeAt(index)
            reordered.add(index + 1, selected)
            state.copy(elements = reordered, error = null, message = null)
        }
    }

    fun sendSelectedBackward() {
        val id = _uiState.value.selectedElementId ?: return
        _uiState.update { state ->
            val index = state.elements.indexOfFirst { it.id == id }
            if (index <= 0) return@update state
            val reordered = state.elements.toMutableList()
            val selected = reordered.removeAt(index)
            reordered.add(index - 1, selected)
            state.copy(elements = reordered, error = null, message = null)
        }
    }

    fun generateAiText(businessType: String, offerDetails: String) {
        if (businessType.isBlank() || offerDetails.isBlank()) {
            _uiState.update { it.copy(error = "Business type and offer details are required.", message = null) }
            return
        }
        if (BuildConfigWrapper.geminiKeyIsMissing()) {
            _uiState.update {
                it.copy(
                    error = "Missing GEMINI_API_KEY. Add it to local.properties then Sync.",
                    message = null,
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingAiText = true, error = null, message = null) }
            try {
                val text = generateAiTextUseCase(businessType = businessType, offerDetails = offerDetails)
                val id = UUID.randomUUID().toString()
                val new =
                    PosterElement.Text(
                        id = id,
                        text = text,
                        colorArgb = 0xFF111111.toInt(),
                        fontSizeSp = 22f,
                        fontFamilyName = "Default",
                        offsetX = 24f,
                        offsetY = 44f,
                        scale = 1f,
                        rotationDeg = 0f,
                    )
                _uiState.update { it.copy(elements = it.elements + new, selectedElementId = id, message = "AI text added.", error = null) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = "AI request failed: ${t.message ?: "Unknown error"}", message = null) }
            } finally {
                _uiState.update { it.copy(isGeneratingAiText = false) }
            }
        }
    }

    fun savePosterToDatabase() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Title cannot be empty.", message = null) }
            return
        }
        if (state.elements.isEmpty()) {
            _uiState.update { it.copy(error = "Add at least one element before saving.", message = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSavingToDb = true, error = null, message = null) }
            try {
                val content =
                    PosterContent(
                        background = BackgroundStyle.Solid(state.backgroundColorArgb),
                        elements = state.elements,
                    )
                val json = gson.toJson(content)
                savePosterUseCase(title = state.title, contentJson = json, createdAt = System.currentTimeMillis())
                _uiState.update { it.copy(message = "Saved to Namma Posters.", error = null) }
            } catch (t: Throwable) {
                _uiState.update { it.copy(error = "Save failed: ${t.message ?: "Unknown error"}", message = null) }
            } finally {
                _uiState.update { it.copy(isSavingToDb = false) }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * Keeps ViewModel independent from Android BuildConfig import in domain layer.
 * (Presentation layer can still call BuildConfig through this wrapper.)
 */
private object BuildConfigWrapper {
    fun geminiKeyIsMissing(): Boolean = com.aipostermaker.BuildConfig.GEMINI_API_KEY.isBlank()
}

