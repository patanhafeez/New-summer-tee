package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.DesignConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudioScreen(viewModel: StudioViewModel, modifier: Modifier = Modifier) {
    val savedConfigs by viewModel.savedConfigs.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    // Text field for naming the saved design
    var designSaveName by remember { mutableStateOf("") }
    var activeVibePrompt by remember { mutableStateOf("") }

    val fabricColors = listOf(
        Pair("Off-White", "#EAEDED"),
        Pair("Obsidian", "#1C1C1E"),
        Pair("Slate Gray", "#6B7280"),
        Pair("Blush Pink", "#F472B6"),
        Pair("Moss Green", "#1B4332"),
        Pair("Deep Indigo", "#1E293B")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "THREADS & TRENDS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isWideScreen = maxWidth > 680.dp

            if (isWideScreen) {
                // Adaptive horizontal split for tablets or landscape screens
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Column: T-Shirt preview Canvas pinned
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        TShirtMockup(
                            shirtColor = Color(android.graphics.Color.parseColor(viewModel.shirtColorHex)),
                            designIndex = viewModel.selectedPresetIndex,
                            slogan = viewModel.sloganText,
                            scale = viewModel.designScale,
                            opacity = viewModel.designFade,
                            fit = viewModel.shirtFit,
                            placement = viewModel.placement,
                            modifier = Modifier.testTag("tshirt_preview_canvas")
                        )
                    }

                    // Right Column: Controls and AI engines
                    LazyColumn(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            ArtisticFlairHeader()
                        }
                        item {
                            PresetSelectionSection(viewModel)
                        }
                        item {
                            FabricControlsSection(
                                viewModel = viewModel,
                                colors = fabricColors
                            )
                        }
                        item {
                            AiGeneratorsSection(
                                viewModel = viewModel,
                                activeVibePrompt = activeVibePrompt,
                                onPromptChange = { activeVibePrompt = it },
                                clearFocus = { focusManager.clearFocus() }
                            )
                        }
                        item {
                            SaveDesignSection(
                                designSaveName = designSaveName,
                                onNameChange = { designSaveName = it },
                                onSave = {
                                    viewModel.saveCurrentDesign(designSaveName)
                                    designSaveName = ""
                                    focusManager.clearFocus()
                                },
                                savedConfigs = savedConfigs,
                                onLoad = { viewModel.loadSavedConfig(it) },
                                onDelete = { viewModel.deleteDesign(it) },
                                onClearAll = { viewModel.clearAllDesigns() }
                            )
                        }
                    }
                }
            } else {
                // Portrait single column for standard mobile screens
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 48.dp)
                ) {
                    item {
                        ArtisticFlairHeader()
                    }
                    // Mobile Canvas Mockup View
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(380.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TShirtMockup(
                                shirtColor = Color(android.graphics.Color.parseColor(viewModel.shirtColorHex)),
                                designIndex = viewModel.selectedPresetIndex,
                                slogan = viewModel.sloganText,
                                scale = viewModel.designScale,
                                opacity = viewModel.designFade,
                                fit = viewModel.shirtFit,
                                placement = viewModel.placement,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .testTag("tshirt_preview_canvas")
                            )
                        }
                    }

                    item {
                        PresetSelectionSection(viewModel)
                    }

                    item {
                        FabricControlsSection(
                            viewModel = viewModel,
                            colors = fabricColors
                        )
                    }

                    item {
                        AiGeneratorsSection(
                            viewModel = viewModel,
                            activeVibePrompt = activeVibePrompt,
                            onPromptChange = { activeVibePrompt = it },
                            clearFocus = { focusManager.clearFocus() }
                        )
                    }

                    item {
                        SaveDesignSection(
                            designSaveName = designSaveName,
                            onNameChange = { designSaveName = it },
                            onSave = {
                                viewModel.saveCurrentDesign(designSaveName)
                                designSaveName = ""
                                focusManager.clearFocus()
                            },
                            savedConfigs = savedConfigs,
                            onLoad = { viewModel.loadSavedConfig(it) },
                            onDelete = { viewModel.deleteDesign(it) },
                            onClearAll = { viewModel.clearAllDesigns() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PresetSelectionSection(viewModel: StudioViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("style_preset_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE1E2E1))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "1. CHOOSE DESIGN preset",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF8F4C38)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
 
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.presets.size) { index ->
                    val preset = viewModel.presets[index]
                    val isSelected = viewModel.selectedPresetIndex == index
 
                    Box(
                        modifier = Modifier
                            .width(135.dp)
                            .height(85.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) Color(0xFFFFDAD1) else Color(0xFFFAF9F6))
                            .border(
                                1.5.dp,
                                if (isSelected) Color(0xFF8F4C38) else Color(0xFFE1E2E1),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { viewModel.updatePreset(index) }
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column {
                            Text(
                                text = preset.title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1C1B1F)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = preset.styleName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFF8F4C38) else Color(0xFF8E8D8F)
                                )
                            )
                        }
                    }
                }
            }
 
            Spacer(modifier = Modifier.height(14.dp))
 
            // Info box about selected design
            val selectedPreset = viewModel.presets[viewModel.selectedPresetIndex]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF3F0EE), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = selectedPreset.title + " Details",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF8F4C38),
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedPreset.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF1C1B1F),
                        lineHeight = 16.sp
                    )
                )
            }
        }
    }
}

@Composable
fun FabricControlsSection(viewModel: StudioViewModel, colors: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("fabric_controls_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFADBD3)),
        border = BorderStroke(1.dp, Color(0xFFE9C6BC))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "2. FABRIC & PLACEMENT CONFIG",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF1C1B1F)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Fabric Color Selector
            Text(
                text = "FABRIC COLOR",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF8F4C38), fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(colors) { config ->
                    val colorName = config.first
                    val colorHex = config.second
                    val isSelected = viewModel.shirtColorHex.equals(colorHex, ignoreCase = true)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { viewModel.shirtColorHex = colorHex }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(colorHex)))
                                .border(
                                    if (isSelected) 3.dp else 1.dp,
                                    if (isSelected) Color(0xFF8F4C38) else Color(0xFFE9C6BC),
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = colorName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF1C1B1F) else Color(0xFF5A585B)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GARMENT FIT",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF8F4C38), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val fits = listOf("Regular", "Oversized", "Cropped")
                    fits.forEach { fit ->
                        val isSelected = viewModel.shirtFit == fit
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF8F4C38) else Color(0xFFFAF9F6))
                                .border(1.dp, if (isSelected) Color.Transparent else Color(0xFFE9C6BC), RoundedCornerShape(12.dp))
                                .clickable { viewModel.shirtFit = fit }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = fit,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) Color.White else Color(0xFF1C1B1F),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRINT PLACEMENT",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF8F4C38), fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val placements = listOf("Chest", "Large Back", "Pocket Size")
                    placements.forEach { pl ->
                        val isSelected = viewModel.placement == pl
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .height(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF8F4C38) else Color(0xFFFAF9F6))
                                .border(1.dp, if (isSelected) Color.Transparent else Color(0xFFE9C6BC), RoundedCornerShape(12.dp))
                                .clickable { viewModel.placement = pl }
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pl,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isSelected) Color.White else Color(0xFF1C1B1F),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Graphic transform configs
            Text(
                text = "GRAPHIC SCALE (" + String.format("%.0f%%", viewModel.designScale * 100) + ")",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF1C1B1F), fontWeight = FontWeight.Bold)
            )
            Slider(
                value = viewModel.designScale,
                onValueChange = { viewModel.designScale = it },
                valueRange = 0.40f..0.95f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF8F4C38),
                    activeTrackColor = Color(0xFF8F4C38),
                    inactiveTrackColor = Color(0xFFE9C6BC)
                ),
                modifier = Modifier.height(26.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "GRAPHIC OPACITY (" + String.format("%.0f%%", viewModel.designFade * 100) + ")",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF1C1B1F), fontWeight = FontWeight.Bold)
            )
            Slider(
                value = viewModel.designFade,
                onValueChange = { viewModel.designFade = it },
                valueRange = 0.20f..1.00f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF8F4C38),
                    activeTrackColor = Color(0xFF8F4C38),
                    inactiveTrackColor = Color(0xFFE9C6BC)
                ),
                modifier = Modifier.height(26.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiGeneratorsSection(
    viewModel: StudioViewModel,
    activeVibePrompt: String,
    onPromptChange: (String) -> Unit,
    clearFocus: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ai_prompter_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1B1F)),
        border = BorderStroke(1.5.dp, Color(0xFF8F4C38))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3. AI DESIGNER & PROMPTER",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "AI Powered",
                    tint = Color(0xFFFACC15),
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Text field for current slogan text
            Text(
                text = "T-SHIRT BRANDING TEXT",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFFFDAD1), fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = viewModel.sloganText,
                onValueChange = { viewModel.sloganText = it.uppercase() },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                placeholder = { Text("SLOGAN") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8F4C38),
                    unfocusedBorderColor = Color(0xFF2D2928),
                    focusedContainerColor = Color(0xFF09090B),
                    unfocusedContainerColor = Color(0xFF09090B)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Style advice output card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D2928), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "AI DESIGN TIPS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFFFDAD1),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.aiStyleAdvice,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White,
                            lineHeight = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Vibe / Prompt customization input
            OutlinedTextField(
                value = activeVibePrompt,
                onValueChange = onPromptChange,
                placeholder = {
                    Text(
                        "Describe custom brand vibe... (e.g. vintage punk rock, anime synth, Tokyo neon)",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8E8D8F))
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall.copy(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8F4C38),
                    unfocusedBorderColor = Color(0xFF2D2928),
                    focusedContainerColor = Color(0xFF09090B),
                    unfocusedContainerColor = Color(0xFF09090B)
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(14.dp))

            // AI Trigger Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.triggerAiSloganGeneration(activeVibePrompt)
                        clearFocus()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFDAD1),
                        contentColor = Color(0xFF1C1B1F)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !viewModel.isAiGenerating
                ) {
                    if (viewModel.isAiGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFF1C1B1F),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "AI GEN SLOGAN",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1C1B1F)
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        viewModel.triggerAiSloganGeneration("")
                        onPromptChange("")
                        clearFocus()
                    },
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2D2928))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                }
            }

            AnimatedVisibility(visible = viewModel.aiError != null) {
                viewModel.aiError?.let { err ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = err,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = Color(0xFFF87171)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF7F1D1D).copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveDesignSection(
    designSaveName: String,
    onNameChange: (String) -> Unit,
    onSave: () -> Unit,
    savedConfigs: List<DesignConfig>,
    onLoad: (DesignConfig) -> Unit,
    onDelete: (DesignConfig) -> Unit,
    onClearAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("save_designs_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE1E2E1))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "4. SAVE DESIGN VARIATION",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF8F4C38)
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = designSaveName,
                    onValueChange = onNameChange,
                    placeholder = {
                        Text(
                            "Design Name (e.g., Summer Neon Drop)",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF8E8D8F))
                        )
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1C1B1F)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8F4C38),
                        unfocusedBorderColor = Color(0xFFE1E2E1),
                        focusedContainerColor = Color(0xFFFAF9F6),
                        unfocusedContainerColor = Color(0xFFFAF9F6)
                    ),
                    singleLine = true
                )

                Button(
                    onClick = onSave,
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8F4C38)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "SAVE",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                }
            }

            if (savedConfigs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SAVED TREND VARIATIONS (${savedConfigs.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF1C1B1F),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                    Text(
                        text = "CLEAR ALL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFEF4444),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.clickable { onClearAll() }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    savedConfigs.forEach { config ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF3F0EE))
                                .border(1.dp, Color(0xFFE1E2E1), RoundedCornerShape(16.dp))
                                .clickable { onLoad(config) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = config.name,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF1C1B1F),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row {
                                    Text(
                                        text = config.shirtFit + " • " + config.placement + " • " + config.slogan,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = Color(0xFF5A585B)),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDelete(config) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete config",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtisticFlairHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "NEW SEASON",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = Color(0xFF8F4C38)
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Vaporwave",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Light,
                        fontSize = 26.sp,
                        color = Color(0xFF1C1B1F),
                        style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "GLITCH",
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = (-1.5).sp,
                        fontFamily = FontFamily.SansSerif,
                        color = Color(0xFF1C1B1F)
                    )
                }
            }
            
            // DROPPING NOW Pill Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFFFDAD1))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Bolt",
                    tint = Color(0xFF8F4C38),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DROPPING NOW",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8F4C38),
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}

