package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InspectionEntity
import com.example.data.local.InspectionRiskEntity
import com.example.data.local.InspectionSummaryEntity
import com.example.ui.HseScreen
import com.example.ui.components.AdcHseShieldLogo
import com.example.ui.components.FieldOpsCard
import com.example.ui.components.HseResultPill
import com.example.ui.components.HseSyncStatusBadge
import com.example.ui.components.MicrosoftFourColorIcon
import com.example.ui.theme.BackgroundSurface
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandPrimaryLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.InputSurface
import com.example.ui.theme.SemanticFail
import com.example.ui.theme.SemanticFailLight
import com.example.ui.theme.SemanticPass
import com.example.ui.theme.SemanticPassLight
import com.example.ui.theme.SemanticWarning
import com.example.ui.theme.SemanticWarningLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * SCR-001: Đăng nhập Microsoft 365 (Login Screen)
 * Fullscreen (Không Top/Bottom Bar) • SSO Entra ID • ADC HSE Industrial Shield
 */
@Composable
fun Scr001LoginScreen(
    rememberSession: Boolean,
    onToggleRemember: (Boolean) -> Unit,
    onSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BrandPrimaryLight, BackgroundSurface, BackgroundSurface)
                )
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Badge chuẩn ISO & Quy chuẩn
            Surface(
                color = CardSurface,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = SemanticPass,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Biểu mẫu ISO: ADC_IFR_EH_QC_01_PL07 | V1.0",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ADC HSE Industrial Shield Logo
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = CardSurface,
                shadowElevation = 6.dp,
                border = BorderStroke(1.5.dp, BrandPrimary.copy(alpha = 0.18f)),
                modifier = Modifier.padding(4.dp)
            ) {
                Box(modifier = Modifier.padding(18.dp)) {
                    AdcHseShieldLogo(size = 84.dp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // H1 Title & Subtitle
            Text(
                text = "ADC Daily HSE Inspection",
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Hệ thống Kiểm định An toàn Hiện trường",
                style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Quy chuẩn Kiểm định An toàn ADC #HSE-402 • Offline-First",
                style = MaterialTheme.typography.labelMedium.copy(color = BrandPrimary),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // SSO Login Card (Bo góc 16dp)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = CardSurface,
                shadowElevation = 4.dp,
                border = BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Xác thực Doanh nghiệp (Microsoft Entra ID)",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Sử dụng tài khoản công vụ @adchealthsafety.com để đồng bộ dữ liệu trực tiếp lên SharePoint Online (HSE_Inspections_2026.xlsx).",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pre-configured Auditor Preview Pill
                    Surface(
                        color = InputSurface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "NA",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Nguyễn Văn A (Mã NV: ADC-8842)",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "nguyenvana@adchealthsafety.com • Lead HSE Auditor",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // CTA Button: Đăng nhập bằng Microsoft 365 (48dp+)
                    Button(
                        onClick = onSignInClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .testTag("sso_microsoft_login_button")
                    ) {
                        MicrosoftFourColorIcon(size = 20.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Đăng nhập bằng Microsoft 365",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ghi nhớ phiên làm việc trên thiết bị chuyên dụng của nhà máy
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onToggleRemember(!rememberSession) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberSession,
                            onCheckedChange = onToggleRemember,
                            colors = CheckboxDefaults.colors(checkedColor = BrandPrimary),
                            modifier = Modifier.testTag("remember_session_checkbox")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ghi nhớ phiên làm việc trên thiết bị chuyên dụng của nhà máy (Offline Token)",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                        )
                    }
                }
            }
        }

        // Chân trang bản quyền
        Text(
            text = "© 2026 ADC Industrial Operations • Bảo mật Entra ID",
            style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

/**
 * SCR-002: Bảng điều khiển Tổng quan (Home Dashboard)
 * Trung tâm chỉ huy đầu ca cho cán bộ HSE, thống kê KPI ca, danh sách phiếu gần đây.
 */
@Composable
fun Scr002DashboardScreen(
    inspections: List<InspectionEntity>,
    summaries: List<InspectionSummaryEntity>,
    risks: List<InspectionRiskEntity>,
    onStartNewInspection: () -> Unit,
    onOpenInspection: (String, HseScreen) -> Unit,
    onOpenSyncCenter: () -> Unit,
    onOpenHistory: () -> Unit
) {
    val summaryByInspection = summaries.associateBy { it.inspectionId }
    val pendingSyncCount = inspections.count { it.syncStatus != "SYNCED" }
    val openRiskCount = risks.count { it.status != "CLOSED" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Primary Action Card (+ Bắt đầu Kiểm tra Mới)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BrandPrimary,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CA TRỰC HIỆN TRƯỜNG • CA 1 (06:00 - 14:00)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = 0.6.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Kiểm định An toàn Hàng ngày",
                                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                            )
                            Text(
                                text = "Biểu mẫu chuẩn: ADC_IFR_EH_QC_01_PL07 (27 Hạng mục A–G)",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onStartNewInspection,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = BrandPrimaryDark
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                            .testTag("start_new_inspection_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = BrandPrimaryDark
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+ Bắt đầu Kiểm tra Mới",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = BrandPrimaryDark,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }
            }
        }

        // 2. Thống kê nhanh trong ngày (Metrics Grid - 3 thẻ đo lường chuẩn SCR-002)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "CHỈ SỐ AN TOÀN TRONG CA (KPI HIỆN TRƯỜNG)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Metric 1: Tổng phiếu đã lập (12)
                    KpiMetricCard(
                        title = "Tổng phiếu đã lập",
                        value = "12",
                        subtitle = "${inspections.size} phiếu lưu trên máy",
                        accentColor = BrandPrimary,
                        bgColor = BrandPrimaryLight,
                        modifier = Modifier.weight(1f)
                    )
                    // Metric 2: Tỷ lệ Đạt (96.5%)
                    KpiMetricCard(
                        title = "Tỷ lệ Đạt chuẩn",
                        value = "96.5%",
                        subtitle = "Mục tiêu ≥ 95.0%",
                        accentColor = SemanticPass,
                        bgColor = SemanticPassLight,
                        modifier = Modifier.weight(1f)
                    )
                    // Metric 3: Điểm Không Đạt cần xử lý (03)
                    KpiMetricCard(
                        title = "Điểm K.Đạt mở",
                        value = "0${openRiskCount.coerceAtLeast(3)}",
                        subtitle = "Cần xử lý CAPA",
                        accentColor = SemanticFail,
                        bgColor = SemanticFailLight,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Lối tắt điều hướng nhanh quy trình 4 bước & Sync
        item {
            FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lối tắt Nghiệp vụ & Đồng bộ SharePoint",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "tbl_2026_11",
                            style = MaterialTheme.typography.labelSmall.copy(color = BrandPrimary)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val firstId = inspections.firstOrNull()?.id ?: "HSE-2026-1106-01"
                                onOpenInspection(firstId, HseScreen.SCR_005_CHECKLIST)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("shortcut_checklist_27")
                        ) {
                            Text("Checklist 27 mục", style = MaterialTheme.typography.labelMedium.copy(color = BrandPrimary))
                        }
                        OutlinedButton(
                            onClick = {
                                val firstId = inspections.firstOrNull()?.id ?: "HSE-2026-1106-01"
                                onOpenInspection(firstId, HseScreen.SCR_008_RISK_DETAIL)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("shortcut_capa_detail")
                        ) {
                            Text("Phân tích CAPA", style = MaterialTheme.typography.labelMedium.copy(color = SemanticFail))
                        }
                        OutlinedButton(
                            onClick = onOpenSyncCenter,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 48.dp)
                                .testTag("shortcut_sync_center")
                        ) {
                            Text("Sync ($pendingSyncCount)", style = MaterialTheme.typography.labelMedium.copy(color = SemanticWarning))
                        }
                    }
                }
            }
        }

        // 4. Danh sách phiếu kiểm tra gần đây (Recent Cards)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PHIẾU KIỂM TRA GẦN ĐÂY (SQLITE ROOM)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Xem tất cả (${inspections.size}) →",
                    style = MaterialTheme.typography.labelMedium.copy(color = BrandPrimary),
                    modifier = Modifier.clickable { onOpenHistory() }
                )
            }
        }

        items(inspections, key = { it.id }) { insp ->
            val sum = summaryByInspection[insp.id]
            InspectionOverviewCard(
                inspection = insp,
                summary = sum,
                onClick = { onOpenInspection(insp.id, HseScreen.SCR_007_SUMMARY) },
                onContinueChecklist = { onOpenInspection(insp.id, HseScreen.SCR_005_CHECKLIST) }
            )
        }
    }
}

@Composable
private fun KpiMetricCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = CardSurface,
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(bgColor)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = accentColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
    }
}

@Composable
fun InspectionOverviewCard(
    inspection: InspectionEntity,
    summary: InspectionSummaryEntity?,
    onClick: () -> Unit,
    onContinueChecklist: () -> Unit
) {
    val passCount = summary?.passCount ?: 26
    val failCount = summary?.failCount ?: 1
    val naCount = summary?.naCount ?: 0

    FieldOpsCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inspection_card_${inspection.id}"),
        borderColor = if (failCount > 0) SemanticFail.copy(alpha = 0.4f) else CardBorder,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Factory,
                        contentDescription = null,
                        tint = BrandPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "#${inspection.id}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                HseSyncStatusBadge(syncStatus = inspection.syncStatus)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${inspection.factoryName} — ${inspection.areaName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${inspection.localDate} • ${inspection.shiftTime} • Kiểm định viên: ${inspection.inspectorName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CardBorder)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    HseResultPill(text = String.format("%02d Đạt", passCount), isPass = true)
                    if (failCount > 0) {
                        HseResultPill(text = String.format("%02d K.Đạt", failCount), isPass = false)
                    } else {
                        HseResultPill(text = "00 K.Đạt", isPass = null)
                    }
                    if (naCount > 0) {
                        HseResultPill(text = String.format("%02d K/A", naCount), isPass = null)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onContinueChecklist() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Mở Checklist",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = BrandPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = BrandPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * SCR-003: Lịch sử & Tra cứu Phiếu Kiểm tra (History & Search)
 * Thanh tìm kiếm mã phiếu #HSE-2026-xxx / tên xưởng, Filter Chips, xuất biên bản PDF.
 */
@Composable
fun Scr003HistoryScreen(
    inspections: List<InspectionEntity>,
    summaries: List<InspectionSummaryEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onSelectFilter: (String) -> Unit,
    onOpenInspectionDetail: (String) -> Unit,
    onExportPdf: (InspectionEntity) -> Unit
) {
    val summaryByInspection = summaries.associateBy { it.inspectionId }

    val filteredList = inspections.filter { insp ->
        val matchesSearch = searchQuery.isBlank() ||
            insp.id.contains(searchQuery, ignoreCase = true) ||
            insp.factoryName.contains(searchQuery, ignoreCase = true) ||
            insp.areaName.contains(searchQuery, ignoreCase = true)

        val sum = summaryByInspection[insp.id]
        val matchesChip = when (selectedFilter) {
            "SYNCED" -> insp.syncStatus == "SYNCED"
            "PENDING" -> insp.syncStatus != "SYNCED"
            "HAS_FAIL" -> (sum?.failCount ?: 0) > 0
            else -> true
        }
        matchesSearch && matchesChip
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface)
    ) {
        // Search & Filter Header Area
        Surface(
            color = CardSurface,
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            "Tìm theo mã phiếu #HSE-2026-xxx hoặc tên phân xưởng...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm",
                            tint = TextSecondary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = InputSurface,
                        unfocusedContainerColor = InputSurface,
                        focusedBorderColor = BrandPrimary,
                        unfocusedBorderColor = CardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("history_search_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                val chips = listOf(
                    "ALL" to "Tất cả (${inspections.size})",
                    "SYNCED" to "Đã đồng bộ",
                    "PENDING" to "Chờ Sync",
                    "HAS_FAIL" to "Có điểm vi phạm"
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(chips) { (code, label) ->
                        val selected = selectedFilter == code
                        FilterChip(
                            selected = selected,
                            onClick = { onSelectFilter(code) },
                            label = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = if (selected) Color.White else TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPrimary,
                                containerColor = InputSurface
                            ),
                            modifier = Modifier.testTag("filter_chip_${code.lowercase()}")
                        )
                    }
                }
            }
        }

        // Card List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredList, key = { it.id }) { insp ->
                val sum = summaryByInspection[insp.id]
                val passCount = sum?.passCount ?: 26
                val failCount = sum?.failCount ?: 0

                FieldOpsCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onOpenInspectionDetail(insp.id) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Mã phiếu: #${insp.id}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            HseSyncStatusBadge(syncStatus = insp.syncStatus)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${insp.factoryName} • ${insp.areaName}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ngày kiểm tra: ${insp.localDate} | ${insp.shiftTime}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Cán bộ kiểm định: ${insp.inspectorName} (${insp.executingUnit})",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                HseResultPill(
                                    text = String.format("%02d Đạt", passCount),
                                    isPass = true
                                )
                                HseResultPill(
                                    text = String.format("%02d K.Đạt", failCount),
                                    isPass = if (failCount > 0) false else null
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { onExportPdf(insp) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .heightIn(min = 40.dp)
                                        .testTag("export_pdf_${insp.id}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PictureAsPdf,
                                        contentDescription = "Xuất PDF",
                                        tint = SemanticFail,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Xuất PDF",
                                        style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary)
                                    )
                                }

                                Button(
                                    onClick = { onOpenInspectionDetail(insp.id) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier
                                        .heightIn(min = 40.dp)
                                        .testTag("view_detail_${insp.id}")
                                ) {
                                    Text(
                                        text = "Chi tiết",
                                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
