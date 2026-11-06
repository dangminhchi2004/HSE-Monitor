package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HseScreen
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandPrimaryLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.InputSurface
import com.example.ui.theme.MsBlue
import com.example.ui.theme.MsGreen
import com.example.ui.theme.MsRed
import com.example.ui.theme.MsYellow
import com.example.ui.theme.SemanticFail
import com.example.ui.theme.SemanticFailLight
import com.example.ui.theme.SemanticPass
import com.example.ui.theme.SemanticPassLight
import com.example.ui.theme.SemanticWarning
import com.example.ui.theme.SemanticWarningBorder
import com.example.ui.theme.SemanticWarningLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Custom vector Canvas drawing for the ADC HSE Industrial Shield (#0F4C81 Navy shield + #15803D Safety cross).
 * Zero placeholder dependency.
 */
@Composable
fun AdcHseShieldLogo(
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val shieldPath = Path().apply {
            moveTo(w * 0.5f, h * 0.06f)
            lineTo(w * 0.88f, h * 0.22f)
            lineTo(w * 0.88f, h * 0.52f)
            cubicTo(
                w * 0.88f, h * 0.74f,
                w * 0.72f, h * 0.88f,
                w * 0.5f, h * 0.95f
            )
            cubicTo(
                w * 0.28f, h * 0.88f,
                w * 0.12f, h * 0.74f,
                w * 0.12f, h * 0.52f
            )
            lineTo(w * 0.12f, h * 0.22f)
            close()
        }

        // Outer Navy Shield
        drawPath(path = shieldPath, color = BrandPrimary)
        drawPath(path = shieldPath, color = BrandPrimaryDark, style = Stroke(width = w * 0.04f))

        // Inner White Circle Badge
        drawCircle(
            color = Color.White,
            radius = w * 0.24f,
            center = Offset(w * 0.5f, h * 0.49f)
        )

        // Green Safety Cross (#15803D)
        val crossArmWidth = w * 0.11f
        val crossArmLength = w * 0.28f
        val cx = w * 0.5f
        val cy = h * 0.49f

        drawRect(
            color = SemanticPass,
            topLeft = Offset(cx - crossArmWidth / 2f, cy - crossArmLength / 2f),
            size = Size(crossArmWidth, crossArmLength)
        )
        drawRect(
            color = SemanticPass,
            topLeft = Offset(cx - crossArmLength / 2f, cy - crossArmWidth / 2f),
            size = Size(crossArmLength, crossArmWidth)
        )
    }
}

/**
 * 4-color Microsoft Windows vector icon for SSO button
 */
@Composable
fun MicrosoftFourColorIcon(modifier: Modifier = Modifier, size: Dp = 20.dp) {
    Canvas(modifier = modifier.size(size)) {
        val gap = this.size.width * 0.08f
        val box = (this.size.width - gap) / 2f
        drawRect(color = MsRed, topLeft = Offset(0f, 0f), size = Size(box, box))
        drawRect(color = MsGreen, topLeft = Offset(box + gap, 0f), size = Size(box, box))
        drawRect(color = MsBlue, topLeft = Offset(0f, box + gap), size = Size(box, box))
        drawRect(color = MsYellow, topLeft = Offset(box + gap, box + gap), size = Size(box, box))
    }
}

/**
 * Mobile Stack Top Header with Back Button, Title, Subtitle ("Quy chuẩn An toàn ADC #HSE-402"), and optional Step Badge.
 */
@Composable
fun HseStackTopBar(
    title: String,
    subtitle: String = "Quy chuẩn An toàn ADC #HSE-402",
    stepBadge: String? = null,
    screenCode: String? = null,
    onBackClick: () -> Unit
) {
    Surface(
        color = BrandPrimary,
        contentColor = Color.White,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("stack_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontSize = 17.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (screenCode != null) "$screenCode • $subtitle" else subtitle,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (stepBadge != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .border(1.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = stepBadge,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

/**
 * Mobile Tab Header for SCR-002, SCR-003, SCR-012 with ADC Shield, Auditor Badge, and Cloud Sync Pill
 */
@Composable
fun HseTabTopBar(
    title: String,
    subtitle: String = "Nguyễn Văn A (Mã NV: ADC-8842)",
    pendingSyncCount: Int,
    onSyncBadgeClick: () -> Unit
) {
    Surface(
        color = BrandPrimary,
        contentColor = Color.White,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    AdcHseShieldLogo(size = 38.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                    }
                }

                // Semantic Sync Badge: strictly Amber (#FEF3C7 / #B45309) for Pending Sync, Green for Synced
                val isPending = pendingSyncCount > 0
                val badgeBg = if (isPending) SemanticWarningLight else SemanticPassLight
                val badgeFg = if (isPending) SemanticWarning else SemanticPass
                val badgeText = if (isPending) "⟳ $pendingSyncCount Chờ Sync" else "✓ Đã đồng bộ"

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(badgeBg)
                        .border(
                            1.dp,
                            if (isPending) SemanticWarningBorder else SemanticPass.copy(alpha = 0.4f),
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { onSyncBadgeClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("top_sync_status_badge"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isPending) Icons.Default.CloudSync else Icons.Default.CloudDone,
                        contentDescription = badgeText,
                        tint = badgeFg,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = badgeFg,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

/**
 * 5-Tab Bottom Navigation Bar for Mobile Tab Shell (SCR-002, SCR-003, SCR-004 shortcut, SCR-011, SCR-012)
 */
@Composable
fun HseBottomNavigationBar(
    currentScreen: HseScreen,
    pendingSyncCount: Int,
    onSelectScreen: (HseScreen) -> Unit
) {
    NavigationBar(
        containerColor = CardSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, CardBorder))
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val items = listOf(
            Triple(HseScreen.SCR_002_DASHBOARD, "Trang chủ", Icons.Default.Home),
            Triple(HseScreen.SCR_003_HISTORY, "Lịch sử", Icons.Default.History),
            Triple(HseScreen.SCR_004_GENERAL_INFO, "Tạo phiếu", Icons.Default.AddCircle),
            Triple(HseScreen.SCR_011_SYNC_CENTER, if (pendingSyncCount > 0) "Sync ($pendingSyncCount)" else "Đồng bộ", Icons.Default.CloudSync),
            Triple(HseScreen.SCR_012_SETTINGS, "Cài đặt", Icons.Default.Settings)
        )

        items.forEach { (screen, label, icon) ->
            val selected = currentScreen == screen
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectScreen(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandPrimary,
                    selectedTextColor = BrandPrimary,
                    indicatorColor = BrandPrimaryLight,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                ),
                modifier = Modifier.testTag("bottom_tab_${screen.code.lowercase()}")
            )
        }
    }
}

/**
 * Semantic Status Badge strictly adhering to Field Ops Industrial rules:
 * Pending Sync MUST use Amber (#FEF3C7 / #B45309), NEVER Red.
 * Pass/Synced uses Green (#DCFCE7 / #15803D).
 * Fail/High Hazard uses Red (#FEE2E2 / #B91C1C).
 */
@Composable
fun HseSyncStatusBadge(syncStatus: String) {
    val isSynced = syncStatus == "SYNCED"
    val bg = if (isSynced) SemanticPassLight else SemanticWarningLight
    val fg = if (isSynced) SemanticPass else SemanticWarning
    val label = when (syncStatus) {
        "SYNCED" -> "✓ Đã đồng bộ"
        "SYNCING" -> "⟳ Đang tải lên..."
        "CONFLICT" -> "⚠ Xung đột phiên bản"
        else -> "⟳ Chờ Sync"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, fg.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 9.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun HseResultPill(
    text: String,
    isPass: Boolean? // true = Pass (Green), false = Fail (Red), null = Neutral/NA
) {
    val bg = when (isPass) {
        true -> SemanticPassLight
        false -> SemanticFailLight
        null -> InputSurface
    }
    val fg = when (isPass) {
        true -> SemanticPass
        false -> SemanticFail
        null -> TextSecondary
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

/**
 * One-thumb Zone Bottom Action Bar for Wizard Screens SCR-004 through SCR-011.
 * Enforces minimum 48.dp touch target for gloved industrial operators.
 */
@Composable
fun HseBottomCtaBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    primaryTestTag: String = "bottom_primary_cta",
    secondaryText: String? = null,
    onSecondaryClick: (() -> Unit)? = null,
    progressSummary: String? = null,
    enabled: Boolean = true
) {
    Surface(
        color = CardSurface,
        shadowElevation = 10.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, CardBorder))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (progressSummary != null) {
                Text(
                    text = progressSummary,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = BrandPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (secondaryText != null && onSecondaryClick != null) {
                    OutlinedButton(
                        onClick = onSecondaryClick,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, BrandPrimary),
                        modifier = Modifier
                            .weight(0.42f)
                            .heightIn(min = 50.dp)
                            .testTag("${primaryTestTag}_secondary")
                    ) {
                        Text(
                            text = secondaryText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Button(
                    onClick = onPrimaryClick,
                    enabled = enabled,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPrimary,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(if (secondaryText != null) 0.58f else 1f)
                        .heightIn(min = 50.dp)
                        .testTag(primaryTestTag)
                ) {
                    Text(
                        text = primaryText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Interactive 12-Screen Quick Switcher Strip so the user/evaluator can jump directly to any of SCR-001..SCR-012
 * or follow the natural wizard flow seamlessly.
 */
@Composable
fun HseScreenNavigatorStrip(
    currentScreen: HseScreen,
    onSelectScreen: (HseScreen) -> Unit
) {
    Surface(
        color = BrandPrimaryDark,
        contentColor = Color.White
    ) {
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val allScreens = HseScreen.entries
            items(allScreens.size) { index ->
                val scr = allScreens[index]
                val isSelected = scr == currentScreen
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.14f))
                        .clickable { onSelectScreen(scr) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("quick_nav_${scr.code.lowercase()}")
                ) {
                    Text(
                        text = scr.code,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSelected) BrandPrimaryDark else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

/**
 * Styled Card matching Field Ops Industrial specification (#FFFFFF with 1dp #E2E8F0 border, 12dp radius)
 */
@Composable
fun FieldOpsCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CardBorder,
    containerColor: Color = CardSurface,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = if (onClick != null) {
            modifier.clickable { onClick() }
        } else {
            modifier
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        content()
    }
}

/**
 * Realistic Industrial Evidence Thumbnail (renders visual oil leak / safety ruler / machine gauge vector illustration)
 */
@Composable
fun EvidenceThumbnailCard(
    fileName: String,
    caption: String,
    uploadStatus: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(150.dp),
        shape = RoundedCornerShape(10.dp),
        color = InputSurface,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp)
                    .background(Color(0xFF1E293B))
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    // Industrial floor grid lines
                    drawLine(Color(0xFF334155), Offset(0f, h * 0.7f), Offset(w, h * 0.7f), strokeWidth = 2f)
                    drawLine(Color(0xFF334155), Offset(w * 0.3f, 0f), Offset(w * 0.3f, h), strokeWidth = 2f)
                    // Simulated hydraulic oil leak puddle or safety ruler
                    drawOval(
                        color = Color(0xFFB45309).copy(alpha = 0.75f),
                        topLeft = Offset(w * 0.22f, h * 0.42f),
                        size = Size(w * 0.52f, h * 0.36f)
                    )
                    // Yellow/Black safety ruler bar at bottom
                    drawRect(
                        color = Color(0xFFFACC15),
                        topLeft = Offset(w * 0.1f, h * 0.82f),
                        size = Size(w * 0.8f, h * 0.1f)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (uploadStatus == "UPLOADED") SemanticPass else SemanticWarning)
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (uploadStatus == "UPLOADED") "CLOUD" else "LOCAL",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = caption,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = fileName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 10.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
