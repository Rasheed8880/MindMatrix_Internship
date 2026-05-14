package com.aipostermaker.domain.model

data class PosterContent(
    val background: BackgroundStyle = BackgroundStyle.Solid(colorArgb = 0xFFFFFFFF.toInt()),
    val elements: List<PosterElement> = emptyList(),
)

sealed class BackgroundStyle {
    data class Solid(val colorArgb: Int) : BackgroundStyle()
}

sealed class PosterElement {
    abstract val id: String
    abstract val offsetX: Float
    abstract val offsetY: Float
    abstract val scale: Float
    abstract val rotationDeg: Float

    data class Text(
        override val id: String,
        val text: String,
        val colorArgb: Int,
        val fontSizeSp: Float,
        val fontFamilyName: String = "Default",
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float = 1f,
        override val rotationDeg: Float = 0f,
    ) : PosterElement()

    data class Image(
        override val id: String,
        val uriString: String,
        val sizeDp: Float,
        val cropRatio: Float? = null,
        val cornerRadiusDp: Float = 12f,
        val borderWidthDp: Float = 1f,
        val borderColorArgb: Int = 0x33FFFFFF,
        override val offsetX: Float,
        override val offsetY: Float,
        override val scale: Float = 1f,
        override val rotationDeg: Float = 0f,
    ) : PosterElement()
}

