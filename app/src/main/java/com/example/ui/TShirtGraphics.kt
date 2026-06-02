package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

@Composable
fun TShirtMockup(
    shirtColor: Color,
    designIndex: Int,
    slogan: String,
    scale: Float,
    opacity: Float,
    fit: String,
    placement: String,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121214), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF2B2B30), RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        // Size adjustments based on Shirt Fit Selection
        val widthMultiplier = when (fit) {
            "Oversized" -> 1.05f
            "Cropped" -> 0.98f
            else -> 0.90f // Regular
        }
        val heightMultiplier = when (fit) {
            "Oversized" -> 0.98f
            "Cropped" -> 0.80f
            else -> 0.92f // Regular
        }

        // Draw T-Shirt Outline & Shadows using Jetpack Compose Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scaleX = size.width / 400f * widthMultiplier
            val scaleY = size.height / 450f * heightMultiplier
            
            // Re-centering calculations
            val dx = (size.width - (400f * scaleX)) / 2f
            val dy = (size.height - (450f * scaleY)) / 2f

            // Create highly smooth Vector Path for T-Shirt structure
            val tShirtPath = Path().apply {
                // Top neckline (starting left collar)
                moveTo(dx + 150f * scaleX, dy + 50f * scaleY)
                // Left collar curve
                quadraticTo(dx + 200f * scaleX, dy + 65f * scaleY, dx + 250f * scaleX, dy + 50f * scaleY)
                // Right shoulder
                lineTo(dx + 330f * scaleX, dy + 70f * scaleY)
                // Right sleeve top
                lineTo(dx + 390f * scaleX, dy + 130f * scaleY)
                // Right sleeve opening bottom
                lineTo(dx + 350f * scaleX, dy + 175f * scaleY)
                // Right sleeve underarm curve
                quadraticTo(dx + 310f * scaleX, dy + 150f * scaleY, dx + 310f * scaleX, dy + 180f * scaleY)
                // Right side bodice
                lineTo(dx + 305f * scaleX, dy + 410f * scaleY)
                // Right bottom hemline
                quadraticTo(dx + 200f * scaleX, dy + 418f * scaleY, dx + 95f * scaleX, dy + 410f * scaleY)
                // Left side bodice upward
                lineTo(dx + 90f * scaleX, dy + 180f * scaleY)
                // Left sleeve underarm curve
                quadraticTo(dx + 90f * scaleX, dy + 150f * scaleY, dx + 50f * scaleX, dy + 175f * scaleY)
                // Left sleeve opening bottom
                lineTo(dx + 10f * scaleX, dy + 130f * scaleY)
                // Left shoulder top
                lineTo(dx + 70f * scaleX, dy + 70f * scaleY)
                close()
            }

            // Draw shadow under the shirt
            drawPath(
                path = tShirtPath,
                color = Color.Black.copy(alpha = 0.25f),
                style = Stroke(width = 8f)
            )

            // Fill actual T-Shirt fabric color
            drawPath(
                path = tShirtPath,
                color = shirtColor
            )

            // Draw collar border ribbing line
            val collarPath = Path().apply {
                moveTo(dx + 148f * scaleX, dy + 48f * scaleY)
                quadraticTo(dx + 200f * scaleX, dy + 68f * scaleY, dx + 252f * scaleX, dy + 48f * scaleY)
            }
            drawPath(
                path = collarPath,
                color = Color.Black.copy(alpha = 0.15f),
                style = Stroke(width = 5f)
            )

            // Draw sleeve cuff lining
            val leftCuff = Path().apply {
                moveTo(dx + 10f * scaleX, dy + 130f * scaleY)
                lineTo(dx + 50f * scaleX, dy + 175f * scaleY)
            }
            drawPath(leftCuff, Color.Black.copy(alpha = 0.12f), style = Stroke(width = 3f))

            val rightCuff = Path().apply {
                moveTo(dx + 390f * scaleX, dy + 130f * scaleY)
                lineTo(dx + 350f * scaleX, dy + 175f * scaleY)
            }
            drawPath(rightCuff, Color.Black.copy(alpha = 0.12f), style = Stroke(width = 3f))

            // Draw subtle shading (fabric creases) to mimic a premium realistic garment
            // Sleeve folds-crease shadows
            drawLine(
                color = Color.Black.copy(alpha = 0.08f),
                start = Offset(dx + 95f * scaleX, dy + 160f * scaleY),
                end = Offset(dx + 125f * scaleX, dy + 130f * scaleY),
                strokeWidth = 6f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.12f),
                start = Offset(dx + 93f * scaleX, dy + 158f * scaleY),
                end = Offset(dx + 123f * scaleX, dy + 128f * scaleY),
                strokeWidth = 4f
            )

            drawLine(
                color = Color.Black.copy(alpha = 0.08f),
                start = Offset(dx + 305f * scaleX, dy + 160f * scaleY),
                end = Offset(dx + 275f * scaleX, dy + 130f * scaleY),
                strokeWidth = 6f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.12f),
                start = Offset(dx + 307f * scaleX, dy + 158f * scaleY),
                end = Offset(dx + 277f * scaleX, dy + 128f * scaleY),
                strokeWidth = 4f
            )

            // Left side fabric waist draping creases
            drawLine(
                color = Color.Black.copy(alpha = 0.06f),
                start = Offset(dx + 90f * scaleX, dy + 320f * scaleY),
                end = Offset(dx + 140f * scaleX, dy + 340f * scaleY),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.Black.copy(alpha = 0.06f),
                start = Offset(dx + 310f * scaleX, dy + 300f * scaleY),
                end = Offset(dx + 260f * scaleX, dy + 320f * scaleY),
                strokeWidth = 5f
            )

            // Left chest pocket line mock (if pocket placement is selected)
            if (placement == "Pocket Size") {
                val pocketPath = Path().apply {
                    moveTo(dx + 120f * scaleX, dy + 120f * scaleY)
                    lineTo(dx + 160f * scaleX, dy + 120f * scaleY)
                    lineTo(dx + 160f * scaleX, dy + 165f * scaleY)
                    lineTo(dx + 140f * scaleX, dy + 180f * scaleY)
                    lineTo(dx + 120f * scaleX, dy + 165f * scaleY)
                    close()
                }
                drawPath(pocketPath, Color.Black.copy(alpha = 0.08f))
                drawPath(pocketPath, Color.Black.copy(alpha = 0.15f), style = Stroke(width = 2f))
            }
        }

        // Overlay the procedural Graphic inside the t-shirt print area
        val containerScaleX = width / 400f * widthMultiplier
        val containerScaleY = height / 450f * heightMultiplier
        
        val contentDx = (width - (400f * containerScaleX)) / 2f
        val contentDy = (height - (450f * containerScaleY)) / 2f

        // Adjust dimensions and positions based on "Placement" toggle choice
        val graphicWidth = when (placement) {
            "Large Back" -> 220.dp
            "Pocket Size" -> 55.dp
            else -> 155.dp // Chest Central
        }

        val yPivotOffset = when (placement) {
            "Large Back" -> (contentDy + 170f * containerScaleY).dp
            "Pocket Size" -> (contentDy + 130f * containerScaleY).dp
            else -> (contentDy + 145f * containerScaleY).dp // Chest
        }

        val xPivotOffset = when (placement) {
            "Pocket Size" -> (contentDx + 135f * containerScaleX).dp
            else -> (width / 2).dp // Centered relative to container
        }

        // Overlay UI Content Container
        Box(
            modifier = Modifier
                .offset(
                    x = if (placement == "Pocket Size") xPivotOffset else 0.dp,
                    y = yPivotOffset
                )
                .width(graphicWidth)
                .aspectRatio(1f)
                .then(
                    if (placement != "Pocket Size") Modifier.align(Alignment.TopCenter) else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            // Container constraint check for graphic styling
            Box(
                modifier = Modifier
                    .size(graphicWidth)
                    .alpha(opacity)
            ) {
                DesignGraphic(
                    designIndex = designIndex,
                    scale = scale,
                    slogan = slogan,
                    sizeCategory = placement
                )
            }
        }
    }
}

@Composable
fun DesignGraphic(
    designIndex: Int,
    scale: Float,
    slogan: String,
    sizeCategory: String
) {
    val showText = sizeCategory != "Pocket Size"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        contentAlignment = Alignment.Center
    ) {
        // Procedurally Render Graphic Core
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension / 2f) * scale * 0.90f

            when (designIndex) {
                // 1. Cyber Botanical
                0 -> {
                    // Draw neon pulsing concentric back rings
                    drawCircle(
                        color = Color(0xFF0D9488).copy(alpha = 0.2f),
                        radius = radius * 1.15f,
                        center = center,
                        style = Stroke(width = 2f)
                    )
                    drawCircle(
                        color = Color(0xFF10B981).copy(alpha = 0.1f),
                        radius = radius * 1.35f,
                        center = center,
                        style = Stroke(width = 1f)
                    )

                    // Draw stylized central polygonal flower using circuit lines
                    val points = 8
                    val innerRad = radius * 0.25f
                    val outerRad = radius * 0.85f
                    for (i in 0 until points) {
                        val angle = (i * (2 * Math.PI) / points).toFloat()
                        val leafLeftAngle = angle - 0.25f
                        val leafRightAngle = angle + 0.25f

                        val start = Offset(
                            center.x + innerRad * Math.cos(angle.toDouble()).toFloat(),
                            center.y + innerRad * Math.sin(angle.toDouble()).toFloat()
                        )
                        val tip = Offset(
                            center.x + outerRad * Math.cos(angle.toDouble()).toFloat(),
                            center.y + outerRad * Math.sin(angle.toDouble()).toFloat()
                        )
                        val leftNode = Offset(
                            center.x + radius * 0.55f * Math.cos(leafLeftAngle.toDouble()).toFloat(),
                            center.y + radius * 0.55f * Math.sin(leafLeftAngle.toDouble()).toFloat()
                        )
                        val rightNode = Offset(
                            center.x + radius * 0.55f * Math.cos(leafRightAngle.toDouble()).toFloat(),
                            center.y + radius * 0.55f * Math.sin(leafRightAngle.toDouble()).toFloat()
                        )

                        // Stem center connector line
                        drawLine(
                            color = Color(0xFF34D399),
                            start = center,
                            end = tip,
                            strokeWidth = 3f
                        )
                        // Polygonal bio-leaves
                        val leafPath = Path().apply {
                            moveTo(start.x, start.y)
                            lineTo(leftNode.x, leftNode.y)
                            lineTo(tip.x, tip.y)
                            lineTo(rightNode.x, rightNode.y)
                            close()
                        }
                        drawPath(
                            path = leafPath,
                            color = Color(0xFF059669).copy(alpha = 0.15f)
                        )
                        drawPath(
                            path = leafPath,
                            color = Color(0xFF10B981),
                            style = Stroke(width = 2f)
                        )

                        // Draw circuit logic node dots at ends
                        drawCircle(
                            color = Color.White,
                            radius = 4f,
                            center = tip
                        )
                    }

                    // Tech ring accent
                    drawCircle(
                        brush = Brush.linearGradient(listOf(Color(0xFF047857), Color(0xFF2DD4BF))),
                        radius = radius * 0.50f,
                        center = center,
                        style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f))
                    )
                    drawCircle(
                        color = Color(0xFF0D9488),
                        radius = radius * 0.15f,
                        center = center
                    )
                }

                // 2. Retro-Futurism
                1 -> {
                    // Outrun / Synthwave Sunset dome
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            isAntiAlias = true
                            shader = LinearGradientShader(
                                from = Offset(center.x, center.y - radius),
                                to = Offset(center.x, center.y + radius),
                                colors = listOf(Color(0xFFFF007F), Color(0xFFFF9F0A), Color(0xFF0038FF).copy(alpha = 0.1f)),
                                colorStops = listOf(0.0f, 0.70f, 1.0f)
                            )
                        }
                        canvas.drawCircle(center, radius, paint)
                    }

                    // Draw iconic synthwave horizontal grid cuts
                    val steps = 8
                    for (i in 1..steps) {
                        val yOffset = center.y + (radius * (i.toFloat() / steps))
                        val lineThickness = 3.5f * (i.toFloat() / steps)
                        
                        // Crop lines within circle limits
                        drawLine(
                            color = Color(0xFF121214), // Solid contrast backdrop
                            start = Offset(center.x - radius, yOffset),
                            end = Offset(center.x + radius, yOffset),
                            strokeWidth = lineThickness + 1.5f
                        )
                    }

                    // Draw minimalist mountain vector grids
                    val mountainPath = Path().apply {
                        moveTo(center.x - radius * 0.85f, center.y + radius * 0.90f)
                        lineTo(center.x - radius * 0.25f, center.y + radius * 0.10f)
                        lineTo(center.x + radius * 0.35f, center.y + radius * 0.75f)
                        
                        moveTo(center.x - radius * 0.10f, center.y + radius * 0.90f)
                        lineTo(center.x + radius * 0.40f, center.y + radius * 0.20f)
                        lineTo(center.x + radius * 0.95f, center.y + radius * 0.90f)
                    }
                    drawPath(
                        path = mountainPath,
                        color = Color(0xFF3B82F6).copy(alpha = 0.25f)
                    )
                    drawPath(
                        path = mountainPath,
                        color = Color(0xFF60A5FA),
                        style = Stroke(width = 2.5f)
                    )

                    // Draw futuristic space starfield coordinates (crosshairs)
                    drawLine(Color(0xFFFF007F).copy(alpha = 0.6f), Offset(center.x - radius * 1.2f, center.y), Offset(center.x + radius * 1.2f, center.y), strokeWidth = 1f)
                    drawLine(Color(0xFFFF007F).copy(alpha = 0.6f), Offset(center.x, center.y - radius * 1.2f), Offset(center.x, center.y + radius * 1.2f), strokeWidth = 1f)
                }

                // 3. Scandi Minimal Geo
                2 -> {
                    // Scandinavian minimalist mountains
                    val mountainLeft = Path().apply {
                        moveTo(center.x - radius, center.y + radius * 0.80f)
                        lineTo(center.x - radius * 0.20f, center.y - radius * 0.35f)
                        lineTo(center.x + radius * 0.50f, center.y + radius * 0.80f)
                        close()
                    }
                    val mountainRight = Path().apply {
                        moveTo(center.x - radius * 0.30f, center.y + radius * 0.80f)
                        lineTo(center.x + radius * 0.45f, center.y - radius * 0.15f)
                        lineTo(center.x + radius * 1.10f, center.y + radius * 0.80f)
                        close()
                    }

                    // Large Golden Sun in background
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFEAB308), Color(0xFFF97316).copy(alpha = 0.05f))
                        ),
                        radius = radius * 0.60f,
                        center = Offset(center.x + radius * 0.35f, center.y - radius * 0.25f)
                    )
                    drawCircle(
                        color = Color(0xFFFACC15),
                        radius = radius * 0.40f,
                        center = Offset(center.x + radius * 0.35f, center.y - radius * 0.25f)
                    )

                    // Fill mountain color blocks (Earthy Scandinavian)
                    drawPath(mountainLeft, Color(0xFFC2410C)) // Rust Orange
                    drawPath(mountainRight, Color(0xFF1E3A8A).copy(alpha = 0.85f)) // Muted Prussian Blue

                    // Fine minimalist outline details
                    drawPath(mountainLeft, Color(0xFFFFedd5), style = Stroke(width = 3f))
                    drawPath(mountainRight, Color(0xFFFFedd5), style = Stroke(width = 3f))

                    // Stylized minimal evergreen pine triangles
                    for (i in -3..3 step 2) {
                        val treeX = center.x + (radius * i * 0.25f)
                        val treeY = center.y + radius * 0.76f
                        val treeH = radius * 0.20f
                        val treeW = radius * 0.10f

                        val treePath = Path().apply {
                            moveTo(treeX, treeY - treeH)
                            lineTo(treeX - treeW, treeY)
                            lineTo(treeX + treeW, treeY)
                            close()
                        }
                        drawPath(treePath, Color(0xFF065F46))
                        drawLine(Color(0xFF022C22), Offset(treeX, treeY), Offset(treeX, treeY + 7f), strokeWidth = 3f)
                    }
                }

                // 4. Techwear Cyber
                3 -> {
                    // Grid background
                    val gridSteps = 6
                    val stepSize = (radius * 2f) / gridSteps
                    for (i in 0..gridSteps) {
                        val gridPos = (center.y - radius) + i * stepSize
                        drawLine(
                            color = Color(0xFFEA580C).copy(alpha = 0.15f),
                            start = Offset(center.x - radius, gridPos),
                            end = Offset(center.x + radius, gridPos),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = Color(0xFFEA580C).copy(alpha = 0.15f),
                            start = Offset((center.x - radius) + i * stepSize, center.y - radius),
                            end = Offset((center.x - radius) + i * stepSize, center.y + radius),
                            strokeWidth = 1f
                        )
                    }

                    // Tech core bounding box
                    drawRect(
                        color = Color(0xFFF97316),
                        topLeft = Offset(center.x - radius * 0.85f, center.y - radius * 0.85f),
                        size = androidx.compose.ui.geometry.Size(radius * 1.70f, radius * 1.70f),
                        style = Stroke(width = 2.5f)
                    )

                    // Bounding Box outer notches
                    val notch = radius * 0.15f
                    // Top-Left notch
                    drawLine(Color(0xFFFACC15), Offset(center.x - radius * 0.90f, center.y - radius * 0.85f), Offset(center.x - radius * 0.90f, center.y - radius * 0.85f + notch), strokeWidth = 5f)
                    drawLine(Color(0xFFFACC15), Offset(center.x - radius * 0.90f, center.y - radius * 0.85f), Offset(center.x - radius * 0.90f + notch, center.y - radius * 0.85f), strokeWidth = 5f)

                    // Bottom-Right notch
                    drawLine(Color(0xFFFACC15), Offset(center.x + radius * 0.90f, center.y + radius * 0.85f), Offset(center.x + radius * 0.90f, center.y + radius * 0.85f - notch), strokeWidth = 5f)
                    drawLine(Color(0xFFFACC15), Offset(center.x + radius * 0.90f, center.y + radius * 0.85f), Offset(center.x + radius * 0.90f - notch, center.y + radius * 0.85f), strokeWidth = 5f)

                    // Inner tech circles and crosshair
                    drawCircle(
                        color = Color(0xFFEA580C),
                        radius = radius * 0.50f,
                        center = center,
                        style = Stroke(width = 1.5f)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = radius * 0.10f,
                        center = center
                    )

                    // Center crosshair line tick bars
                    drawLine(Color(0xFFF97316), Offset(center.x - radius * 0.70f, center.y), Offset(center.x - radius * 0.25f, center.y), strokeWidth = 2f)
                    drawLine(Color(0xFFF97316), Offset(center.x + radius * 0.25f, center.y), Offset(center.x + radius * 0.70f, center.y), strokeWidth = 2f)
                    drawLine(Color(0xFFF97316), Offset(center.x, center.y - radius * 0.70f), Offset(center.x, center.y - radius * 0.25f), strokeWidth = 2f)
                    drawLine(Color(0xFFF97316), Offset(center.x, center.y + radius * 0.25f), Offset(center.x, center.y + radius * 0.70f), strokeWidth = 2f)
                }

                // 5. Classical Vaporwave
                4 -> {
                    // Linear high contrast neon backdrop gradient slab
                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            isAntiAlias = true
                            shader = LinearGradientShader(
                                from = Offset(center.x - radius, center.y - radius),
                                to = Offset(center.x + radius, center.y + radius),
                                colors = listOf(Color(0xFF00F5FF), Color(0xFFFF007F)),
                                colorStops = listOf(0.0f, 1.0f)
                            )
                        }
                        val rect = Rect(center.x - radius * 0.8f, center.y - radius * 0.8f, center.x + radius * 0.8f, center.y + radius * 0.8f)
                        canvas.drawRect(rect, paint)
                    }

                    // Greek Key-style grid overlay
                    val gridLineX = center.x - radius * 0.5f
                    drawLine(Color.White.copy(alpha = 0.5f), Offset(gridLineX, center.y - radius * 0.8f), Offset(gridLineX, center.y + radius * 0.8f), strokeWidth = 2f)
                    val gridLineX2 = center.x + radius * 0.5f
                    drawLine(Color.White.copy(alpha = 0.5f), Offset(gridLineX2, center.y - radius * 0.8f), Offset(gridLineX2, center.y + radius * 0.8f), strokeWidth = 2f)

                    // Procedural marble statue head geometric outline
                    val statuePath = Path().apply {
                        // Crown/hair contour outline
                        moveTo(center.x - radius * 0.25f, center.y - radius * 0.50f)
                        quadraticTo(center.x, center.y - radius * 0.65f, center.x + radius * 0.25f, center.y - radius * 0.50f)
                        // Right head outline
                        lineTo(center.x + radius * 0.22f, center.y - radius * 0.10f)
                        // VR visor area bridge
                        lineTo(center.x + radius * 0.35f, center.y)
                        lineTo(center.x - radius * 0.35f, center.y)
                        moveTo(center.x - radius * 0.22f, center.y - radius * 0.10f)
                        lineTo(center.x - radius * 0.25f, center.y - radius * 0.50f)

                        // Visor bottom chin outline connection
                        moveTo(center.x - radius * 0.22f, center.y + radius * 0.10f)
                        lineTo(center.x - radius * 0.18f, center.y + radius * 0.35f) // chin curve
                        lineTo(center.x + radius * 0.18f, center.y + radius * 0.35f)
                        lineTo(center.x + radius * 0.22f, center.y + radius * 0.10f)

                        // Neck pedestal outline base
                        moveTo(center.x - radius * 0.15f, center.y + radius * 0.34f)
                        lineTo(center.x - radius * 0.25f, center.y + radius * 0.55f)
                        lineTo(center.x + radius * 0.25f, center.y + radius * 0.55f)
                        lineTo(center.x + radius * 0.15f, center.y + radius * 0.34f)
                    }
                    drawPath(statuePath, Color.White)
                    drawPath(statuePath, Color(0xFF1E1E24), style = Stroke(width = 3.5f))

                    // Glowing VR Glasses Visor (Aesthetic highlight block)
                    drawRect(
                        brush = Brush.linearGradient(listOf(Color(0xFFFF007F), Color(0xFFD946EF))),
                        topLeft = Offset(center.x - radius * 0.40f, center.y - radius * 0.08f),
                        size = androidx.compose.ui.geometry.Size(radius * 0.80f, radius * 0.20f)
                    )
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(center.x - radius * 0.40f, center.y - radius * 0.08f),
                        size = androidx.compose.ui.geometry.Size(radius * 0.80f, radius * 0.20f),
                        style = Stroke(width = 1.5f)
                    )

                    // Cyber reflection slash on glasses
                    drawLine(
                        color = Color.White,
                        start = Offset(center.x - radius * 0.25f, center.y + radius * 0.08f),
                        end = Offset(center.x - radius * 0.05f, center.y - radius * 0.05f),
                        strokeWidth = 3f
                    )

                    // Floating 3D glitch wireframe cube
                    val cubeX = center.x - radius * 0.55f
                    val cubeY = center.y - radius * 0.40f
                    val cw = radius * 0.20f
                    drawRect(Color(0xFFD946EF).copy(alpha = 0.25f), topLeft = Offset(cubeX, cubeY), size = androidx.compose.ui.geometry.Size(cw, cw))
                    drawRect(Color(0xFF00F5FF), topLeft = Offset(cubeX, cubeY), size = androidx.compose.ui.geometry.Size(cw, cw), style = Stroke(width = 1.5f))
                    drawRect(Color(0xFFFF007F), topLeft = Offset(cubeX + 4f, cubeY - 4f), size = androidx.compose.ui.geometry.Size(cw, cw), style = Stroke(width = 1.5f))
                }
            }
        }

        // Render Slogan text underneath the Graphic procedurally (if toggled/needed)
        if (showText && slogan.isNotBlank()) {
            val alignTop = sizeCategory == "Large Back"
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (alignTop) Modifier.padding(top = 10.dp) else Modifier.padding(bottom = 10.dp)
                    ),
                contentAlignment = if (alignTop) Alignment.TopCenter else Alignment.BottomCenter
            ) {
                Text(
                    text = slogan,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = when (designIndex) {
                            0 -> Color(0xFF34D399) // Circuit green
                            1 -> Color(0xFFF472B6) // Synth pink
                            2 -> Color(0xFFFFF7ED) // Scandi orange white
                            3 -> Color(0xFFFB923C) // Alert neon-orange
                            else -> Color(0xFF22D3EE) // Vaporwave neon-cyan
                        },
                        fontSize = when (sizeCategory) {
                            "Large Back" -> 15.sp
                            else -> 11.sp
                        },
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .background(Color(0xFF1E1E24).copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
