package com.aipostermaker.presentation.screen

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.View
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.aipostermaker.domain.model.Poster
import com.aipostermaker.domain.model.PosterElement
import com.aipostermaker.presentation.viewmodel.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.math.roundToInt

private data class TemplatePreview(val name: String, val sub: String, val bg: Color, val accent: Color, val preset: PosterTemplatePreset)
private data class BrandPalette(val name: String, val colors: List<Color>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterEditorScreen(editorViewModel: PosterEditorViewModel, listViewModel: PosterListViewModel) {
    val context = LocalContext.current
    val rootView = LocalView.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by editorViewModel.uiState.collectAsState()
    val posters by listViewModel.posters.collectAsState()

    var canvasRectInRoot by remember { mutableStateOf<Rect?>(null) }
    var showTextEditor by remember { mutableStateOf(false) }
    var showAiDialog by remember { mutableStateOf(false) }
    var showPosterList by remember { mutableStateOf(false) }
    var showTemplateLibrary by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }
    var tempText by remember { mutableStateOf("") }
    var tempFontSize by remember { mutableStateOf(28f) }
    var aiLanguage by remember { mutableStateOf("Kannada") }
    var aiBusinessType by remember { mutableStateOf("") }
    var aiOfferDetails by remember { mutableStateOf("") }
    var pendingImageUri by remember { mutableStateOf<String?>(null) }
    var showImportCropDialog by remember { mutableStateOf(false) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                pendingImageUri = uri.toString()
                showImportCropDialog = true
            }
        }
    val writePermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted -> if (!granted) scope.launch { snackbarHostState.showSnackbar("Storage permission denied.") } }

    LaunchedEffect(uiState.message) { uiState.message?.let { snackbarHostState.showSnackbar(it); editorViewModel.clearMessage() } }
    LaunchedEffect(uiState.error) { uiState.error?.let { snackbarHostState.showSnackbar(it); editorViewModel.clearError() } }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AppLogoMark()
                        Column {
                            Text("NAMMA POSTER APP", style = MaterialTheme.typography.titleMedium)
                            Text("Poster Studio", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                },
                actions = {
                    TextButton(onClick = { showInstructions = true }) { Text("How to use") }
                    TextButton(onClick = { showPosterList = true }) { Text("Projects") }
                    TextButton(onClick = { editorViewModel.savePosterToDatabase() }) { Text("Save") }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomToolbar(
                isGenerating = uiState.isGeneratingAiText,
                onAddText = { editorViewModel.addTextElement(); tempText = "Tap to edit"; tempFontSize = 28f; showTextEditor = true },
                onAddImage = { pickImageLauncher.launch("image/*") },
                onGenerateAiText = { showAiDialog = true },
                onChangeColor = { editorViewModel.changeBackgroundColor(nextPaletteColor(uiState.backgroundColorArgb)) },
                onSavePosterImage = {
                    if (Build.VERSION.SDK_INT <= 28) {
                        val granted = context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED
                        if (!granted) { writePermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE); return@BottomToolbar }
                    }
                    val rect = canvasRectInRoot ?: run { scope.launch { snackbarHostState.showSnackbar("Canvas not ready.") }; return@BottomToolbar }
                    val bmp = captureViewBitmap(rootView)
                    val uri = saveBitmapToGallery(context, cropBitmapToRect(bmp, rect), "namma_poster_${System.currentTimeMillis()}")
                    scope.launch { snackbarHostState.showSnackbar(if (uri != null) "Poster saved to gallery." else "Failed to save poster image.") }
                },
                onSharePng = {
                    val bitmap = capturePosterBitmap(rootView, canvasRectInRoot)
                        ?: run { scope.launch { snackbarHostState.showSnackbar("Canvas not ready.") }; return@BottomToolbar }
                    val uri = saveBitmapToCache(context, bitmap, "namma_poster_${System.currentTimeMillis()}.png")
                    if (uri != null) {
                        shareFile(context, uri, "image/png", "Share poster PNG")
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Failed to prepare PNG share.") }
                    }
                },
                onSharePdf = {
                    val bitmap = capturePosterBitmap(rootView, canvasRectInRoot)
                        ?: run { scope.launch { snackbarHostState.showSnackbar("Canvas not ready.") }; return@BottomToolbar }
                    val uri = saveBitmapAsPdfToCache(context, bitmap, "namma_poster_${System.currentTimeMillis()}.pdf")
                    if (uri != null) {
                        shareFile(context, uri, "application/pdf", "Share poster PDF")
                    } else {
                        scope.launch { snackbarHostState.showSnackbar("Failed to prepare PDF share.") }
                    }
                },
                onSaveToDb = { editorViewModel.savePosterToDatabase() },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFF7E6), Color(0xFFFFFBF6), Color(0xFFEFF7EF)),
                    ),
                )
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            ModeToggle(
                showTemplateLibrary = showTemplateLibrary,
                onShowEditor = { showTemplateLibrary = false },
                onShowTemplates = { showTemplateLibrary = true },
            )
            if (showTemplateLibrary) {
                TemplateLibraryScreen(
                    onSelectTemplate = {
                        editorViewModel.applyTemplate(it)
                        showTemplateLibrary = false
                    },
                )
            } else {
                CanvasStage(
                    modifier = Modifier.weight(1f),
                    backgroundColorArgb = uiState.backgroundColorArgb,
                    elements = uiState.elements,
                    selectedId = uiState.selectedElementId,
                    isGenerating = uiState.isGeneratingAiText,
                    onCanvasPositioned = { canvasRectInRoot = it },
                    onSelect = editorViewModel::selectElement,
                    onDragBy = editorViewModel::updateElementOffset,
                    onTransform = editorViewModel::transformElement,
                    onTextTap = { editorViewModel.selectElement(it.id); tempText = it.text; tempFontSize = it.fontSizeSp; showTextEditor = true },
                )
                SimpleWorkspaceControls(
                    title = uiState.title,
                    selectedId = uiState.selectedElementId,
                    elements = uiState.elements,
                    onTitleChange = editorViewModel::setTitle,
                    onApplyPalette = editorViewModel::applyBrandColor,
                    onPreset = editorViewModel::applyTextStylePreset,
                    onFontFamily = editorViewModel::applySelectedFontFamily,
                    onSelect = editorViewModel::selectElement,
                )
            }
        }
    }

    if (showTextEditor) {
        TextEditDialog(
            text = tempText, fontSizeSp = tempFontSize, onTextChange = { tempText = it }, onFontSizeChange = { tempFontSize = it },
            onChangeColor = { editorViewModel.changeSelectedTextColor(nextPaletteColorForText()) },
            onApply = { editorViewModel.updateSelectedText(tempText); editorViewModel.changeSelectedTextFontSize(tempFontSize); showTextEditor = false },
            onDismiss = { showTextEditor = false },
        )
    }
    if (showAiDialog) {
        AiPromptDialog(
            language = aiLanguage,
            businessType = aiBusinessType,
            offerDetails = aiOfferDetails,
            onLanguageSelect = { aiLanguage = it },
            onBusinessTypeChange = { aiBusinessType = it },
            onOfferDetailsChange = { aiOfferDetails = it },
            onGenerateTagline = {
                val languageInstruction =
                    if (aiLanguage == "English") {
                        "Generate in English."
                    } else {
                        "Generate only in Kannada script. Keep it natural for Karnataka local business posters."
                    }
                editorViewModel.generateAiText(
                    businessType = aiBusinessType,
                    offerDetails = "$aiOfferDetails. Create one short poster headline or ad tagline with a clear call to action. $languageInstruction",
                )
                showAiDialog = false
            },
            onDismiss = { showAiDialog = false },
        )
    }
    if (showPosterList) {
        PosterListDialog(posters = posters, onDelete = { listViewModel.deletePoster(it) }, onDismiss = { showPosterList = false })
    }
    if (showInstructions) {
        InstructionsDialog(onDismiss = { showInstructions = false })
    }
    if (showImportCropDialog && pendingImageUri != null) {
        CropOnImportDialog(
            onPickRatio = { ratio ->
                editorViewModel.addImageElement(
                    uriString = pendingImageUri ?: return@CropOnImportDialog,
                    cropRatio = ratio,
                )
                showImportCropDialog = false
                pendingImageUri = null
            },
            onDismiss = {
                showImportCropDialog = false
                pendingImageUri = null
            },
        )
    }
}

@Composable
private fun AppLogoMark(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(38.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(26.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center,
        ) {
        }
    }
}

@Composable
private fun StudioHero() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFFC62828), Color(0xFF1B5E20)),
                    ),
                )
                .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppLogoMark()
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("Kannada templates, AI captions, export-ready design tools", color = Color.White.copy(alpha = 0.82f), style = MaterialTheme.typography.bodySmall)
        }
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary),
            contentAlignment = Alignment.Center,
        ) {
            Text("AI", color = Color(0xFF3A2300), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun CanvasStage(
    modifier: Modifier,
    backgroundColorArgb: Int,
    elements: List<PosterElement>,
    selectedId: String?,
    isGenerating: Boolean,
    onCanvasPositioned: (Rect) -> Unit,
    onSelect: (String?) -> Unit,
    onDragBy: (String, Float, Float) -> Unit,
    onTransform: (String, Float, Float) -> Unit,
    onTextTap: (PosterElement.Text) -> Unit,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
                        .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
                        .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(backgroundColorArgb))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                            .onGloballyPositioned { onCanvasPositioned(it.boundsInRoot()) },
                ) {
                    PosterCanvas(
                        elements = elements,
                        selectedId = selectedId,
                        onSelect = onSelect,
                        onDragBy = onDragBy,
                        onTransform = onTransform,
                        onTextTap = onTextTap,
                    )
                    if (isGenerating) {
                        Box(Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.34f)), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    if (elements.isEmpty()) {
                        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                "Choose a template or add text to start",
                                color = contentTextColor(Color(backgroundColorArgb)).copy(alpha = 0.72f),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimpleWorkspaceControls(
    title: String,
    selectedId: String?,
    elements: List<PosterElement>,
    onTitleChange: (String) -> Unit,
    onApplyPalette: (Int) -> Unit,
    onPreset: (TextStylePreset) -> Unit,
    onFontFamily: (String) -> Unit,
    onSelect: (String?) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.96f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            item { AssistChip(onClick = { onApplyPalette(Color(0xFFFFFFFF).toArgb()) }, label = { Text("White") }) }
            item { AssistChip(onClick = { onApplyPalette(Color(0xFFFFF3E0).toArgb()) }, label = { Text("Warm") }) }
            item { AssistChip(onClick = { onApplyPalette(Color(0xFFE3F2FD).toArgb()) }, label = { Text("Blue") }) }
            item { AssistChip(onClick = { onApplyPalette(Color(0xFF1B5E20).toArgb()) }, label = { Text("Green") }) }
            item { AssistChip(onClick = { onPreset(TextStylePreset.HEADING) }, enabled = selectedId != null, label = { Text("Heading") }) }
            item { AssistChip(onClick = { onPreset(TextStylePreset.BODY) }, enabled = selectedId != null, label = { Text("Body") }) }
            item { AssistChip(onClick = { onFontFamily("SansSerif") }, enabled = selectedId != null, label = { Text("Kannada Bold") }) }
            item { AssistChip(onClick = { onFontFamily("Serif") }, enabled = selectedId != null, label = { Text("Classic") }) }
        }
        if (elements.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(elements.reversed()) { el ->
                    val label = if (el is PosterElement.Text) el.text.take(14) else "Image"
                    FilterChip(
                        selected = el.id == selectedId,
                        onClick = { onSelect(el.id) },
                        label = { Text(label) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeToggle(
    showTemplateLibrary: Boolean,
    onShowEditor: () -> Unit,
    onShowTemplates: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilledTonalButton(
            onClick = onShowEditor,
            colors =
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (!showTemplateLibrary) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) { Text("Editor") }
        FilledTonalButton(
            onClick = onShowTemplates,
            colors =
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (showTemplateLibrary) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                ),
        ) { Text("Templates") }
    }
}

@Composable
private fun TemplateLibraryScreen(
    onSelectTemplate: (PosterTemplatePreset) -> Unit,
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Template Library", style = MaterialTheme.typography.titleMedium)
        TemplateStrip(modifier = Modifier.weight(1f), onSelectTemplate = onSelectTemplate)
    }
}

@Composable
private fun TemplateStrip(modifier: Modifier = Modifier, onSelectTemplate: (PosterTemplatePreset) -> Unit) {
    val templates = listOf(
        TemplatePreview("Sale Blast", "Bold discount campaign", Color(0xFF6A1B9A), Color(0xFFFFEB3B), PosterTemplatePreset.SALE_BLAST),
        TemplatePreview("Grand Opening", "Elegant launch flyer", Color(0xFFE8EAF6), Color(0xFF1A237E), PosterTemplatePreset.GRAND_OPENING),
        TemplatePreview("Cafe Promo", "Warm food & drink offer", Color(0xFFFFF3E0), Color(0xFF5D4037), PosterTemplatePreset.CAFE_SPECIAL),
        TemplatePreview("ಕನ್ನಡ ಹಬ್ಬ", "ರಾಜ್ಯೋತ್ಸವ ಶುಭಾಶಯ", Color(0xFF1B5E20), Color(0xFFFFEB3B), PosterTemplatePreset.KANNADA_FESTIVAL),
        TemplatePreview("ಸಂಗೀತ ರಾತ್ರಿ", "ಕನ್ನಡ ಹಾಡು ಕಾರ್ಯಕ್ರಮ", Color(0xFFFFF8E1), Color(0xFF6D4C41), PosterTemplatePreset.KANNADA_SONG),
        TemplatePreview("ಅಂಗಡಿ ಆಫರ್", "ರಿಟೇಲ್ ಜಾಹೀರಾತು", Color(0xFFB71C1C), Color(0xFFFFF176), PosterTemplatePreset.KANNADA_AD_RETAIL),
        TemplatePreview("ಹೋಟೆಲ್ ಸ್ಪೆಷಲ್", "ಫುಡ್ ಆಫರ್ ಪೋಸ್ಟರ್", Color(0xFFFFF3E0), Color(0xFF8D6E63), PosterTemplatePreset.KANNADA_AD_FOOD),
        TemplatePreview("ಪ್ರವೇಶಾತಿ", "ಶಿಕ್ಷಣ ಜಾಹೀರಾತು", Color(0xFF0D47A1), Color(0xFFFFF176), PosterTemplatePreset.KANNADA_AD_EDUCATION),
        TemplatePreview("ಬ್ಯೂಟಿ ಸಲೂನ್", "ಮೇಕಪ್ ಮತ್ತು ಸಲೂನ್ ಆಫರ್", Color(0xFFFCE4EC), Color(0xFFC2185B), PosterTemplatePreset.KANNADA_BEAUTY_SALON),
        TemplatePreview("ರಿಯಲ್ ಎಸ್ಟೇಟ್", "ಸೈಟ್ ಮಾರಾಟ ಪೋಸ್ಟರ್", Color(0xFFE8F5E9), Color(0xFF1B5E20), PosterTemplatePreset.KANNADA_REAL_ESTATE),
        TemplatePreview("ಆರೋಗ್ಯ ಶಿಬಿರ", "ಹೆಲ್ತ್ ಕ್ಯಾಂಪ್ ಜಾಹೀರಾತು", Color(0xFFE3F2FD), Color(0xFF0D47A1), PosterTemplatePreset.KANNADA_HEALTH_CAMP),
        TemplatePreview("ಹುಟ್ಟುಹಬ್ಬ", "ವಿಶೇಷ ಶುಭಾಶಯ ಪೋಸ್ಟರ್", Color(0xFFFFF8E1), Color(0xFFC62828), PosterTemplatePreset.KANNADA_BIRTHDAY),
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Ready templates", style = MaterialTheme.typography.titleMedium)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(templates) { t ->
                Card(Modifier.fillMaxWidth().height(214.dp).clickable { onSelectTemplate(t.preset) }) {
                    Column(Modifier.fillMaxSize().background(t.bg).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(112.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(t.accent.copy(alpha = 0.28f))
                                .border(1.dp, t.accent.copy(alpha = 0.55f), RoundedCornerShape(10.dp)),
                        ) {
                            Box(Modifier.align(Alignment.Center).fillMaxWidth(0.72f).height(42.dp).clip(RoundedCornerShape(8.dp)).background(t.accent.copy(alpha = 0.72f)))
                            Box(Modifier.align(Alignment.BottomEnd).padding(12.dp).size(30.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.78f)))
                        }
                        Box(Modifier.fillMaxWidth(0.72f).height(12.dp).clip(RoundedCornerShape(6.dp)).background(t.accent.copy(alpha = 0.45f)))
                        Text(t.name, color = contentTextColor(t.bg), style = MaterialTheme.typography.titleSmall)
                        Text(t.sub, color = contentTextColor(t.bg), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandPaletteStrip(onApplyPalette: (Int) -> Unit) {
    val brands = listOf(
        BrandPalette("Bold", listOf(Color(0xFF101828), Color(0xFF6A1B9A), Color(0xFFE53935))),
        BrandPalette("Clean", listOf(Color(0xFFFFFFFF), Color(0xFFE3F2FD), Color(0xFFBBDEFB))),
        BrandPalette("Warm", listOf(Color(0xFFFFF3E0), Color(0xFFFFCC80), Color(0xFFFFA726))),
    )
    Column {
        Text("Brand color palettes", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(brands) { brand ->
                Row(Modifier.clip(RoundedCornerShape(20.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(6.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    brand.colors.forEach { swatch -> Box(Modifier.size(22.dp).clip(RoundedCornerShape(11.dp)).background(swatch).clickable { onApplyPalette(swatch.toArgb()) }) }
                }
            }
        }
    }
}

@Composable
private fun ImageAdjustPanel(
    selectedImage: PosterElement.Image?,
    onEdgeSoft: () -> Unit,
    onEdgeSharp: () -> Unit,
    onBorderThin: () -> Unit,
    onBorderBold: () -> Unit,
) {
    val enabled = selectedImage != null
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Image tools", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { FilledTonalButton(onClick = onEdgeSoft, enabled = enabled) { Text("Soft Edge") } }
            item { FilledTonalButton(onClick = onEdgeSharp, enabled = enabled) { Text("Sharp Edge") } }
            item { FilledTonalButton(onClick = onBorderThin, enabled = enabled) { Text("Thin Border") } }
            item { FilledTonalButton(onClick = onBorderBold, enabled = enabled) { Text("Bold Border") } }
        }
    }
}

@Composable
private fun CropOnImportDialog(
    onPickRatio: (Float?) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Image Crop") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Choose crop ratio before adding image")
                FilledTonalButton(onClick = { onPickRatio(null) }, modifier = Modifier.fillMaxWidth()) { Text("Original") }
                FilledTonalButton(onClick = { onPickRatio(1f) }, modifier = Modifier.fillMaxWidth()) { Text("Square 1:1") }
                FilledTonalButton(onClick = { onPickRatio(4f / 5f) }, modifier = Modifier.fillMaxWidth()) { Text("Portrait 4:5") }
                FilledTonalButton(onClick = { onPickRatio(3f / 4f) }, modifier = Modifier.fillMaxWidth()) { Text("Portrait 3:4") }
                FilledTonalButton(onClick = { onPickRatio(16f / 9f) }, modifier = Modifier.fillMaxWidth()) { Text("Wide 16:9") }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun TextStyleControls(selectedId: String?, onPreset: (TextStylePreset) -> Unit, onFontFamily: (String) -> Unit) {
    val enabled = selectedId != null
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Text style presets + Kannada fonts", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item { FilledTonalButton(onClick = { onPreset(TextStylePreset.HEADING) }, enabled = enabled) { Text("Heading") } }
            item { FilledTonalButton(onClick = { onPreset(TextStylePreset.SUBHEADING) }, enabled = enabled) { Text("Subheading") } }
            item { FilledTonalButton(onClick = { onPreset(TextStylePreset.BODY) }, enabled = enabled) { Text("Body") } }
            item { FilledTonalButton(onClick = { onFontFamily("SansSerif") }, enabled = enabled) { Text("Sans") } }
            item { FilledTonalButton(onClick = { onFontFamily("Serif") }, enabled = enabled) { Text("Serif") } }
            item { FilledTonalButton(onClick = { onFontFamily("Monospace") }, enabled = enabled) { Text("Mono") } }
            item { FilledTonalButton(onClick = { onFontFamily("Cursive") }, enabled = enabled) { Text("Cursive") } }
            item { FilledTonalButton(onClick = { onFontFamily("SansSerif") }, enabled = enabled) { Text("Kannada Ad Bold") } }
            item { FilledTonalButton(onClick = { onFontFamily("Serif") }, enabled = enabled) { Text("Kannada Classic") } }
            item { FilledTonalButton(onClick = { onFontFamily("Cursive") }, enabled = enabled) { Text("Kannada Banner") } }
        }
    }
}

@Composable
private fun LayerPanel(
    elements: List<PosterElement>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("Layers", style = MaterialTheme.typography.titleSmall)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(elements.reversed()) { el ->
                val label = if (el is PosterElement.Text) "Text: ${el.text.take(8)}" else "Image"
                FilledTonalButton(onClick = { onSelect(el.id) }, colors = ButtonDefaults.filledTonalButtonColors(containerColor = if (el.id == selectedId) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)) { Text(label) }
            }
        }
    }
}

@Composable
private fun TitleRow(title: String, onTitleChange: (String) -> Unit) {
    Column {
        Text("Poster title", style = MaterialTheme.typography.labelLarge)
        BasicTextField(value = title, onValueChange = onTitleChange, textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp))
    }
}

@Composable
private fun PosterCanvas(
    elements: List<PosterElement>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onDragBy: (String, Float, Float) -> Unit,
    onTransform: (String, Float, Float) -> Unit,
    onTextTap: (PosterElement.Text) -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onSelect(null) })
            },
    ) {
        elements.forEach { el ->
            DraggableBox(
                id = el.id,
                offsetX = el.offsetX,
                offsetY = el.offsetY,
                scale = el.scale,
                rotation = el.rotationDeg,
                isSelected = selectedId == el.id,
                onSelect = { onSelect(el.id) },
                onDragBy = onDragBy,
                onTransform = onTransform,
            ) {
                when (el) {
                    is PosterElement.Text -> Text(text = el.text, color = Color(el.colorArgb), fontSize = el.fontSizeSp.sp, fontFamily = fontFamilyFromName(el.fontFamilyName), modifier = Modifier.clickable { onTextTap(el) })
                    is PosterElement.Image -> {
                        val cropRatio = el.cropRatio
                        val imageWidthDp =
                            if (cropRatio == null) {
                                el.sizeDp
                            } else if (cropRatio >= 1f) {
                                el.sizeDp
                            } else {
                                el.sizeDp * cropRatio
                            }
                        val imageHeightDp =
                            if (cropRatio == null) {
                                el.sizeDp
                            } else if (cropRatio >= 1f) {
                                el.sizeDp / cropRatio
                            } else {
                                el.sizeDp
                            }
                        val baseModifier =
                            Modifier
                                .then(
                                    if (cropRatio != null) {
                                        Modifier
                                            .width(imageWidthDp.dp)
                                            .height(imageHeightDp.dp)
                                    } else {
                                        Modifier.size(el.sizeDp.dp)
                                    },
                                )
                                .clip(RoundedCornerShape(el.cornerRadiusDp.dp))
                                .background(Color.White.copy(alpha = 0.14f))
                                .border(el.borderWidthDp.dp, Color(el.borderColorArgb), RoundedCornerShape(el.cornerRadiusDp.dp))
                        AsyncImage(
                            model = Uri.parse(el.uriString),
                            contentDescription = "Poster image",
                            modifier = baseModifier.clickable { onSelect(el.id) },
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DraggableBox(
    id: String,
    offsetX: Float,
    offsetY: Float,
    scale: Float,
    rotation: Float,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDragBy: (String, Float, Float) -> Unit,
    onTransform: (String, Float, Float) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .scale(scale)
            .rotate(rotation)
            .pointerInput(id) {
                detectTransformGestures { _, pan, zoom, rotationChange ->
                    onSelect()
                    onDragBy(id, pan.x, pan.y)
                    onTransform(id, zoom, rotationChange)
                }
            }
            .then(if (isSelected) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)) else Modifier)
            .padding(6.dp),
    ) { content() }
}

@Composable
private fun BottomToolbar(
    isGenerating: Boolean,
    onAddText: () -> Unit,
    onAddImage: () -> Unit,
    onGenerateAiText: () -> Unit,
    onChangeColor: () -> Unit,
    onSavePosterImage: () -> Unit,
    onSharePng: () -> Unit,
    onSharePdf: () -> Unit,
    onSaveToDb: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface).padding(top = 4.dp)) {
        Divider()
        LazyRow(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            item { FilledTonalButton(onClick = onAddText, enabled = !isGenerating) { Text("Add Text") } }
            item { FilledTonalButton(onClick = onAddImage, enabled = !isGenerating) { Text("Add Image") } }
            item { FilledTonalButton(onClick = onGenerateAiText, enabled = !isGenerating) { Text("AI Text") } }
            item { FilledTonalButton(onClick = onChangeColor, enabled = !isGenerating) { Text("Canvas Color") } }
            item { Button(onClick = onSavePosterImage, enabled = !isGenerating) { Text("Export PNG") } }
            item { FilledTonalButton(onClick = onSharePng, enabled = !isGenerating) { Text("Share PNG") } }
            item { FilledTonalButton(onClick = onSharePdf, enabled = !isGenerating) { Text("Share PDF") } }
            item { FilledTonalButton(onClick = onSaveToDb, enabled = !isGenerating) { Text("Save Project") } }
        }
    }
}

@Composable
private fun TextEditDialog(text: String, fontSizeSp: Float, onTextChange: (String) -> Unit, onFontSizeChange: (Float) -> Unit, onChangeColor: () -> Unit, onApply: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Text") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BasicTextField(value = text, onValueChange = onTextChange, textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface), modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surfaceVariant).padding(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Size")
                    Spacer(Modifier.width(10.dp))
                    FilledTonalButton(onClick = { onFontSizeChange((fontSizeSp - 2f).coerceAtLeast(12f)) }) { Text("-") }
                    Spacer(Modifier.width(8.dp))
                    Text("${fontSizeSp.roundToInt()}sp")
                    Spacer(Modifier.width(8.dp))
                    FilledTonalButton(onClick = { onFontSizeChange((fontSizeSp + 2f).coerceAtMost(72f)) }) { Text("+") }
                }
                FilledTonalButton(onClick = onChangeColor) { Text("Change text color") }
            }
        },
        confirmButton = { TextButton(onClick = onApply) { Text("Apply") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )
}

@Composable
private fun AiPromptDialog(
    language: String,
    businessType: String,
    offerDetails: String,
    onLanguageSelect: (String) -> Unit,
    onBusinessTypeChange: (String) -> Unit,
    onOfferDetailsChange: (String) -> Unit,
    onGenerateTagline: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("AI Suggestions") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Language")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilledTonalButton(
                        onClick = { onLanguageSelect("English") },
                        colors =
                            ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (language == "English") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                            ),
                    ) { Text("English") }
                    FilledTonalButton(
                        onClick = { onLanguageSelect("Kannada") },
                        colors =
                            ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (language == "Kannada") MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                            ),
                    ) { Text("Kannada") }
                }
                Text("Business type")
                BasicTextField(
                    value = businessType,
                    onValueChange = onBusinessTypeChange,
                    textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(10.dp),
                )
                Text("Offer details")
                BasicTextField(
                    value = offerDetails,
                    onValueChange = onOfferDetailsChange,
                    textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(10.dp),
                )
                Text("Quick ideas", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        AssistChip(
                            onClick = {
                                onBusinessTypeChange("Clothing store")
                                onOfferDetailsChange("Festival sale with up to 40% discount")
                            },
                            label = { Text("Shop sale") },
                        )
                    }
                    item {
                        AssistChip(
                            onClick = {
                                onBusinessTypeChange("Restaurant")
                                onOfferDetailsChange("Family combo offer available today")
                            },
                            label = { Text("Food offer") },
                        )
                    }
                    item {
                        AssistChip(
                            onClick = {
                                onBusinessTypeChange("Coaching center")
                                onOfferDetailsChange("Admissions open with limited seats")
                            },
                            label = { Text("Admissions") },
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onGenerateTagline) { Text("Generate Text") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun InstructionsDialog(onDismiss: () -> Unit) {
    val steps =
        listOf(
            "Login or register to enter your poster workspace.",
            "Tap Templates to choose a ready design, or stay in Editor to start from a blank poster.",
            "Use Add Text to place editable words on the poster. Tap any text on the canvas to edit it.",
            "Use Add Image to import a photo from your phone. Choose a crop size before placing it.",
            "Touch any poster item to select it. Drag with one finger to move it.",
            "Use two fingers on the selected item to zoom in, zoom out, or rotate.",
            "Use AI Text to generate a short poster caption from your business and offer details.",
            "Use the color chips below the canvas to change the poster background.",
            "Use the layer chips to quickly select text or image items.",
            "Tap Export PNG to save the poster image to your gallery, or Save to keep the project in the app.",
        )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("How to use NAMMA POSTER APP") },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 430.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(steps) { step ->
                    Text(step, style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Got it") } },
    )
}

@Composable
private fun PosterListDialog(posters: List<Poster>, onDelete: (Int) -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Namma Posters") },
        text = {
            if (posters.isEmpty()) Text("No posters saved yet.")
            else LazyColumn(Modifier.height(360.dp)) { items(posters, key = { it.id }) { poster -> PosterRow(poster = poster, onDelete = { onDelete(poster.id) }); Divider() } }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Close") } },
    )
}

@Composable
private fun PosterRow(poster: Poster, onDelete: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(poster.title, style = MaterialTheme.typography.titleMedium)
            Text("Created: ${poster.createdAt}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        TextButton(onClick = onDelete) { Text("Delete") }
    }
}

private fun contentTextColor(bg: Color): Color = if (bg.luminance() > 0.55f) Color(0xFF111111) else Color.White
private fun fontFamilyFromName(name: String): FontFamily = when (name) { "SansSerif" -> FontFamily.SansSerif; "Serif" -> FontFamily.Serif; "Monospace" -> FontFamily.Monospace; "Cursive" -> FontFamily.Cursive; else -> FontFamily.Default }

private fun captureViewBitmap(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width.coerceAtLeast(1), view.height.coerceAtLeast(1), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

private fun cropBitmapToRect(source: Bitmap, rect: Rect): Bitmap {
    val left = rect.left.roundToInt().coerceIn(0, source.width - 1)
    val top = rect.top.roundToInt().coerceIn(0, source.height - 1)
    val right = rect.right.roundToInt().coerceIn(left + 1, source.width)
    val bottom = rect.bottom.roundToInt().coerceIn(top + 1, source.height)
    return Bitmap.createBitmap(source, left, top, right - left, bottom - top)
}

private fun capturePosterBitmap(rootView: View, canvasRectInRoot: Rect?): Bitmap? {
    val rect = canvasRectInRoot ?: return null
    return cropBitmapToRect(captureViewBitmap(rootView), rect)
}

private fun sharedPosterDir(context: Context): File =
    File(context.cacheDir, "shared_posters").apply { mkdirs() }

private fun saveBitmapToCache(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    val file = File(sharedPosterDir(context), fileName)
    return try {
        FileOutputStream(file).use { out ->
            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) return null
        }
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (_: Throwable) {
        null
    }
}

private fun saveBitmapAsPdfToCache(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    val file = File(sharedPosterDir(context), fileName)
    val pdf = PdfDocument()
    return try {
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdf.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdf.finishPage(page)
        FileOutputStream(file).use { out -> pdf.writeTo(out) }
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (_: Throwable) {
        null
    } finally {
        pdf.close()
    }
}

private fun shareFile(context: Context, uri: Uri, mimeType: String, chooserTitle: String) {
    val shareIntent =
        Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
}

private fun saveBitmapToGallery(context: Context, bitmap: Bitmap, displayName: String): Uri? {
    val resolver = context.contentResolver
    val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.png")
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/NAMMA POSTER APP")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }
    val uri = resolver.insert(imageCollection, values) ?: return null
    return try {
        resolver.openOutputStream(uri)?.use { out -> if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) error("Bitmap compress failed") }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }
        uri
    } catch (_: Throwable) {
        resolver.delete(uri, null, null)
        null
    }
}

private fun nextPaletteColor(current: Int): Int {
    val palette = listOf(Color(0xFFFFFFFF).toArgb(), Color(0xFFFFF3E0).toArgb(), Color(0xFFE3F2FD).toArgb(), Color(0xFFE8F5E9).toArgb(), Color(0xFF212121).toArgb())
    val idx = palette.indexOf(current).let { if (it == -1) 0 else it }
    return palette[(idx + 1) % palette.size]
}

private fun nextPaletteColorForText(): Int {
    val palette = listOf(Color(0xFF111111).toArgb(), Color(0xFF1E88E5).toArgb(), Color(0xFF43A047).toArgb(), Color(0xFFE53935).toArgb(), Color(0xFFFF6D00).toArgb(), Color(0xFFFFFFFF).toArgb())
    return palette.random()
}
