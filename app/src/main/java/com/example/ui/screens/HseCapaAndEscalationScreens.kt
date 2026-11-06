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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.InspectionEntity
import com.example.data.local.InspectionRiskEntity
import com.example.data.local.InspectionSummaryEntity
import com.example.data.local.RiskEvidenceEntity
import com.example.ui.components.EvidenceThumbnailCard
import com.example.ui.components.FieldOpsCard
import com.example.ui.components.HseBottomCtaBar
import com.example.ui.theme.BackgroundSurface
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSurface
import com.example.ui.theme.InputSurface
import com.example.ui.theme.SemanticFail
import com.example.ui.theme.SemanticFailDark
import com.example.ui.theme.SemanticFailLight
import com.example.ui.theme.SemanticPass
import com.example.ui.theme.SemanticPassLight
import com.example.ui.theme.SemanticWarning
import com.example.ui.theme.SemanticWarningLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * SCR-007: Kết quả kiểm tra & Đánh giá nhanh (Summary & Fast Assessment)
 * Score Card: Tỷ lệ tuân thủ 96.3% - Đạt tiêu chuẩn ca (26 Đạt, 01 Không đạt, 00 K/A),
 * 4 mức quick_assessment (SAFE, FIX_TODAY, MONITOR, ESCALATE), danh sách cảnh báo Immediate CAPA.
 */
@Composable
fun Scr007SummaryScreen(
    inspection: InspectionEntity?,
    summary: InspectionSummaryEntity?,
    risks: List<InspectionRiskEntity>,
    onOpenRiskDetail: (InspectionRiskEntity?) -> Unit,
    onOpenEscalationScreen: () -> Unit,
    onProceedToApproval: () -> Unit
) {
    val passCount = summary?.passCount ?: 26
    val failCount = summary?.failCount ?: 1
    val naCount = summary?.naCount ?: 0
    val applicable = (passCount + failCount).coerceAtLeast(1)
    val compliancePct = (passCount * 100.0f) / applicable

    var selectedQuickAssessment by remember(summary?.quickAssessment) {
        mutableStateOf(summary?.quickAssessment ?: "FIX_TODAY")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Visual Score Card (96.3% - Đạt tiêu chuẩn ca)
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = CardSurface,
                    border = BorderStroke(1.5.dp, BrandPrimary.copy(alpha = 0.35f)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "MỤC 3. TỔNG HỢP TỶ LỆ TUÂN THỦ CA",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = String.format("%.1f%% - Đạt tiêu chuẩn ca", compliancePct),
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = if (failCount == 0) SemanticPass else BrandPrimary,
                                        fontSize = 22.sp
                                    )
                                )
                            }
                            Surface(
                                color = SemanticPassLight,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "#HSE-402",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = SemanticPass,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 3 Counters Grid: 26 Đạt (Xanh), 01 Không đạt (Đỏ), 00 K/A (Xám)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            SummaryCounterBox(
                                label = "Đạt chuẩn (PASS)",
                                count = String.format("%02d Đạt", passCount),
                                bgColor = SemanticPassLight,
                                textColor = SemanticPass,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCounterBox(
                                label = "Vi phạm (FAIL)",
                                count = String.format("%02d Không đạt", failCount),
                                bgColor = SemanticFailLight,
                                textColor = SemanticFail,
                                modifier = Modifier.weight(1f)
                            )
                            SummaryCounterBox(
                                label = "Không Áp Dụng",
                                count = String.format("%02d K/A", naCount),
                                bgColor = InputSurface,
                                textColor = TextSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // 2. Quick Assessment 4 Levels (SAFE | FIX_TODAY | MONITOR | ESCALATE)
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ĐÁNH GIÁ NHANH HIỆN TRƯỜNG (4 MỨC BIỂU MẪU PL07)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val levels = listOf(
                            Triple("SAFE", "SAFE — Khu vực an toàn / chấp nhận được", SemanticPass),
                            Triple("FIX_TODAY", "FIX_TODAY — Có điểm cần khắc phục ngay trong ngày", SemanticWarning),
                            Triple("MONITOR", "MONITOR — Có rủi ro đáng kể cần theo dõi sát", BrandPrimary),
                            Triple("ESCALATE", "ESCALATE — Có rủi ro nghiêm trọng cần leo thang ngay", SemanticFail)
                        )

                        levels.forEach { (code, label, accent) ->
                            val selected = selectedQuickAssessment == code
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (selected) accent.copy(alpha = 0.1f) else Color.Transparent)
                                    .clickable { selectedQuickAssessment = code }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selected,
                                    onClick = { selectedQuickAssessment = code },
                                    colors = RadioButtonDefaults.colors(selectedColor = accent)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = if (selected) accent else TextPrimary,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Immediate CAPA List (Mục 4 Biểu mẫu - Danh sách điểm không đạt cần khắc phục tức thì)
            item {
                FieldOpsCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = SemanticFail.copy(alpha = 0.45f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = SemanticFail,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CẢNH BÁO VI PHẠM CẦN KHẮC PHỤC TỨC THÌ (CAPA)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = SemanticFail,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (risks.isEmpty()) {
                            Surface(
                                color = SemanticFailLight,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "★ Rủi ro #01: Rò rỉ dầu ở Tổ máy xoắn số 04 (Hạng mục #09)",
                                        style = MaterialTheme.typography.titleMedium.copy(color = SemanticFailDark)
                                    )
                                    Text(
                                        text = "Yêu cầu: Dừng máy LOTO, thay phớt làm kín và vệ sinh dầu tràn trước 10:30 hôm nay.",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                                    )
                                }
                            }
                        } else {
                            risks.forEach { risk ->
                                Surface(
                                    color = SemanticFailLight.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, SemanticFail.copy(alpha = 0.35f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                        .clickable { onOpenRiskDetail(risk) }
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "★ ${risk.id} (${risk.checklistItemCode ?: "ITEM_09"})",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = SemanticFail,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = "Mức: ${risk.severity} • Hạn: ${risk.deadline} (Trước 10:30)",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SemanticFailDark,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = risk.areaLocation,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(
                                            text = risk.description,
                                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Phụ trách: ${risk.ownerName} • Trạng thái: ${risk.status}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = BrandPrimary)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onOpenRiskDetail(risks.firstOrNull()) },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, SemanticFail),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("scr007_open_risk_detail_btn")
                            ) {
                                Text(
                                    text = "Xem chi tiết Điểm Không Đạt",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = SemanticFail,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            OutlinedButton(
                                onClick = onOpenEscalationScreen,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.5.dp, BrandPrimary),
                                modifier = Modifier
                                    .weight(0.85f)
                                    .heightIn(min = 48.dp)
                                    .testTag("scr007_open_escalation_btn")
                            ) {
                                Text(
                                    text = "Mục 5: Escalation →",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom CTA Bar
        HseBottomCtaBar(
            primaryText = "Chuyển sang Bước Ký Duyệt →",
            onPrimaryClick = onProceedToApproval,
            primaryTestTag = "scr007_proceed_approval_btn",
            secondaryText = "Mục 5: Escalation",
            onSecondaryClick = onOpenEscalationScreen
        )
    }
}

@Composable
private fun SummaryCounterBox(
    label: String,
    count: String,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleMedium.copy(
                color = textColor,
                fontWeight = FontWeight.ExtraBold
            )
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = textColor)
        )
    }
}

/**
 * SCR-008: Chi tiết Điểm Không Đạt / Rủi ro phát hiện (Non-Conformance Detail - Mục 4 Biểu mẫu)
 * Vị trí cụ thể: Cụm cấp dầu bôi trơn - Tổ máy xoắn số 04,
 * Risk Matrix Pill: ★ Mức 3: Rủi ro Cao (Dừng máy xử lý),
 * Immediate Action, Owner, Deadline, 2 ảnh thực địa (dầu tràn + thước đo an toàn).
 */
@Composable
fun Scr008RiskDetailScreen(
    existingRisk: InspectionRiskEntity?,
    evidences: List<RiskEvidenceEntity>,
    onCapturePhoto: () -> Unit,
    onCancel: () -> Unit,
    onSaveRisk: (
        existingRiskId: String?,
        responseId: String?,
        checklistItemCode: String?,
        areaLocation: String,
        description: String,
        severity: String,
        correctiveAction: String,
        ownerName: String,
        ownerEmail: String,
        deadline: String,
        status: String
    ) -> Unit
) {
    var areaLocation by remember(existingRisk) {
        mutableStateOf(existingRisk?.areaLocation ?: "Cụm cấp dầu bôi trơn - Tổ máy xoắn số 04")
    }
    var description by remember(existingRisk) {
        mutableStateOf(
            existingRisk?.description
                ?: "Phát hiện rò rỉ dầu thủy lực lan ra sàn thao tác (diện tích ~0.8m²) và thiếu khay hứng dầu phụ trợ."
        )
    }
    var severity by remember(existingRisk) {
        mutableStateOf(existingRisk?.severity ?: "HIGH")
    }
    var correctiveAction by remember(existingRisk) {
        mutableStateOf(
            existingRisk?.correctiveAction
                ?: "1. Dừng máy khẩn cấp & khóa LOTO.\n2. Rải tấm thấm dầu chuyên dụng.\n3. Thay phớt chặn dầu trục chính trước 10:30."
        )
    }
    var ownerName by remember(existingRisk) {
        mutableStateOf(existingRisk?.ownerName ?: "Trần Văn B (Trưởng bộ phận Bảo trì)")
    }
    var ownerEmail by remember(existingRisk) {
        mutableStateOf(existingRisk?.ownerEmail ?: "b.tran@adchealthsafety.com")
    }
    var deadline by remember(existingRisk) {
        mutableStateOf(existingRisk?.deadline ?: "2026-11-06")
    }
    var status by remember(existingRisk) {
        mutableStateOf(existingRisk?.status ?: "OPEN")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Risk Matrix Pill Banner
            item {
                Surface(
                    color = SemanticFailLight,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.5.dp, SemanticFail),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SemanticFail,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "PHÂN CẤP MA TRẬN RỦI RO (RISK MATRIX PILL)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SemanticFailDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = when (severity) {
                                    "HIGH" -> "★ Mức 3: Rủi ro Cao (Dừng máy xử lý ngay)"
                                    "MEDIUM" -> "▲ Mức 2: Rủi ro Trung bình (Xử lý trong 1–3 ngày)"
                                    else -> "● Mức 1: Rủi ro Thấp (Theo kế hoạch)"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = SemanticFail,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            )
                        }
                    }
                }
            }

            // Form Phân tích Nguy cơ & Khắc phục
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MỤC 4. FORM PHÂN TÍCH NGUY CƠ & HÀNH ĐỘNG CAPA",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = areaLocation,
                            onValueChange = { areaLocation = it },
                            label = { Text("Vị trí cụ thể phát hiện rủi ro") },
                            leadingIcon = {
                                Icon(Icons.Default.Place, contentDescription = null, tint = SemanticFail)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("risk_area_location_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Severity Selector Pills (HIGH | MEDIUM | LOW)
                        Text(
                            text = "Chọn mức độ rủi ro (Severity):",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Triple("HIGH", "CAO (Xử lý ngay)", SemanticFail),
                                Triple("MEDIUM", "TRUNG BÌNH", SemanticWarning),
                                Triple("LOW", "THẤP", BrandPrimary)
                            ).forEach { (code, label, color) ->
                                val selected = severity == code
                                FilterChip(
                                    selected = selected,
                                    onClick = { severity = code },
                                    label = { Text(label, fontWeight = FontWeight.Bold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = color,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Mô tả điểm không đạt / nguy cơ an toàn") },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("risk_description_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = correctiveAction,
                            onValueChange = { correctiveAction = it },
                            label = { Text("Hành động khắc phục tức thời (Immediate Action / CAPA)") },
                            minLines = 3,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("risk_corrective_action_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = ownerName,
                            onValueChange = { ownerName = it },
                            label = { Text("Người / Bộ phận chịu trách nhiệm xử lý") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = BrandPrimary)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = deadline,
                                onValueChange = { deadline = it },
                                label = { Text("Deadline (YYYY-MM-DD)") },
                                leadingIcon = {
                                    Icon(Icons.Default.Event, contentDescription = null, tint = BrandPrimary)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = status,
                                onValueChange = { status = it },
                                label = { Text("Trạng thái (OPEN/CLOSED)") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(0.85f)
                            )
                        }
                    }
                }
            }

            // Bằng chứng ảnh thực địa (2 ảnh chụp vị trí dầu tràn kèm góc máy có thước đo an toàn)
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BẰNG CHỨNG ẢNH THỰC ĐỊA (HSE_Evidence)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            OutlinedButton(
                                onClick = onCapturePhoto,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("+ Chụp thêm")
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            item {
                                EvidenceThumbnailCard(
                                    fileName = "HSE_oil_leak_01.jpg",
                                    caption = "1. Vị trí dầu tràn sàn máy",
                                    uploadStatus = evidences.firstOrNull()?.uploadStatus ?: "PENDING"
                                )
                            }
                            item {
                                EvidenceThumbnailCard(
                                    fileName = "HSE_scale_ruler_02.jpg",
                                    caption = "2. Góc máy có thước đo an toàn",
                                    uploadStatus = evidences.getOrNull(1)?.uploadStatus ?: "PENDING"
                                )
                            }
                            items(evidences.drop(2), key = { it.id }) { ev ->
                                EvidenceThumbnailCard(
                                    fileName = ev.fileName,
                                    caption = "Ảnh đính kèm CAPA",
                                    uploadStatus = ev.uploadStatus
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action Bar: Nút phụ Hủy bỏ, Nút chính màu Navy Lưu Điểm Không Đạt (Rủi ro #01)
        HseBottomCtaBar(
            primaryText = "Lưu Điểm Không Đạt (Rủi ro #01)",
            onPrimaryClick = {
                onSaveRisk(
                    existingRisk?.id,
                    existingRisk?.responseId,
                    existingRisk?.checklistItemCode ?: "ITEM_09",
                    areaLocation,
                    description,
                    severity,
                    correctiveAction,
                    ownerName,
                    ownerEmail,
                    deadline,
                    status
                )
            },
            primaryTestTag = "scr008_save_risk_btn",
            secondaryText = "Hủy bỏ",
            onSecondaryClick = onCancel
        )
    }
}

/**
 * SCR-009: Escalation Bắt buộc & Bài học kinh nghiệm (Escalation & Lessons Learned - Mục 5 & Mục 6 Biểu mẫu)
 * Cảnh báo đỏ gửi thông báo khẩn qua Teams/SMS tới Giám đốc Nhà máy và Quản đốc xưởng,
 * 6 điều kiện bắt buộc Escalation theo DDS-APP-001, Checkbox tạm dừng vận hành thiết bị,
 * và Mục 6 Bài học kinh nghiệm cuối ngày (Điểm tốt, Điểm cần nhắc lại, Khu vực kiểm tra ngày mai).
 */
@Composable
fun Scr009EscalationScreen(
    summary: InspectionSummaryEntity?,
    onSaveAndProceedToApproval: (
        quickAssessment: String,
        escalationFlag: Boolean,
        triggers: List<String>,
        escalationDescription: String,
        goodPoints: String,
        reminders: String,
        nextDayAreas: String
    ) -> Unit
) {
    var escalationEnabled by remember(summary?.escalationFlag) {
        mutableStateOf((summary?.escalationFlag ?: 1) == 1)
    }
    var machineHaltedConfirmed by remember { mutableStateOf(true) }

    val selectedTriggers = remember {
        mutableStateListOf("UNSAFE_EQUIPMENT", "FIRE_LEAK_HAZARD")
    }

    var escalationDesc by remember(summary?.escalationDescription) {
        mutableStateOf(
            summary?.escalationDescription
                ?: "Rò rỉ dầu thủy lực áp suất cao tại Tổ máy xoắn số 04. Đã tạm dừng máy khẩn cấp (LOTO) và gửi cảnh báo Teams/SMS tới Giám đốc Nhà máy & Quản đốc xưởng."
        )
    }
    var goodPoints by remember(summary?.goodPoints) {
        mutableStateOf(
            summary?.goodPoints
                ?: "Công nhân khu vực Xưởng Dệt A tuân thủ 100% đeo kính bảo hộ, nút tai chống ồn và giày mũi thép."
        )
    }
    var reminders by remember(summary?.reminders) {
        mutableStateOf(
            summary?.reminders
                ?: "Khuyến cáo họp giao ban đầu giờ ca tiếp theo: Kiểm tra phớt chặn dầu và khay hứng phụ trợ toàn bộ các tổ máy xoắn."
        )
    }
    var nextDayAreas by remember(summary?.nextDayAreas) {
        mutableStateOf(
            summary?.nextDayAreas
                ?: "Tổ máy xoắn số 04 - Line 1 & Khu vực Nhuộm"
        )
    }

    val allTriggers = listOf(
        "SEVERE_ACCIDENT_RISK" to "Nguy cơ tai nạn nghiêm trọng (SEVERE_ACCIDENT_RISK)",
        "UNSAFE_EQUIPMENT" to "Thiết bị / máy móc mất an toàn rõ ràng (UNSAFE_EQUIPMENT)",
        "FIRE_LEAK_HAZARD" to "Rò rỉ / cháy / khói / nguy cơ cháy nổ (FIRE_LEAK_HAZARD)",
        "BLOCKED_EXIT" to "Lối thoát hiểm bị chặn (BLOCKED_EXIT)",
        "MASS_VIOLATION" to "Nhiều người cùng vi phạm an toàn (MASS_VIOLATION)",
        "REPEATED_UNFIXED_ISSUE" to "Lỗi lặp lại nhiều lần chưa khắc phục (REPEATED_UNFIXED_ISSUE)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Khối kích hoạt Escalation khẩn cấp (Mục 5 Biểu mẫu)
            item {
                Surface(
                    color = SemanticFailLight,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.5.dp, SemanticFail),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = SemanticFail,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "MỤC 5. KÍCH HOẠT ESCALATION KHẨN CẤP",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = SemanticFail,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                            Checkbox(
                                checked = escalationEnabled,
                                onCheckedChange = { escalationEnabled = it },
                                colors = CheckboxDefaults.colors(checkedColor = SemanticFail)
                            )
                        }

                        Text(
                            text = "Gửi thông báo khẩn cấp qua Microsoft Teams / SMS trực tiếp tới Giám đốc Nhà máy (Plant Director) và Quản đốc xưởng.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = SemanticFailDark)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Mandatory Confirmation Checkbox per SCR-009 Spec
                        Surface(
                            color = CardSurface,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SemanticFail.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { machineHaltedConfirmed = !machineHaltedConfirmed }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = machineHaltedConfirmed,
                                    onCheckedChange = { machineHaltedConfirmed = it },
                                    colors = CheckboxDefaults.colors(checkedColor = SemanticFail),
                                    modifier = Modifier.testTag("halt_equipment_checkbox")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "\"Đã yêu cầu tạm dừng vận hành thiết bị cho đến khi có xác nhận an toàn\"",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = SemanticFailDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Điều kiện bắt buộc Escalation (escalation_triggers_json):",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = SemanticFailDark,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        allTriggers.forEach { (code, label) ->
                            val checked = selectedTriggers.contains(code)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (checked) selectedTriggers.remove(code) else selectedTriggers.add(code)
                                    }
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        if (it) selectedTriggers.add(code) else selectedTriggers.remove(code)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = SemanticFail)
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = escalationDesc,
                            onValueChange = { escalationDesc = it },
                            label = { Text("Nội dung cảnh báo Escalation gửi Ban Giám đốc") },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardSurface, RoundedCornerShape(10.dp))
                        )
                    }
                }
            }

            // 2. Khối đúc kết bài học ca (Mục 6 Biểu mẫu - Lessons Learned)
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MỤC 6. ĐÚC KẾT BÀI HỌC CA & NHẬN XÉT CUỐI NGÀY",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Nội dung khuyến cáo gửi vào buổi họp giao ban đầu giờ của ca tiếp theo.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = goodPoints,
                            onValueChange = { goodPoints = it },
                            label = { Text("6.1. Điểm tốt ghi nhận trong ca (good_points)") },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = reminders,
                            onValueChange = { reminders = it },
                            label = { Text("6.2. Điểm cần nhắc lại ở buổi họp giao ban ca sau (reminders)") },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = nextDayAreas,
                            onValueChange = { nextDayAreas = it },
                            label = { Text("6.3. Khu vực trọng tâm cần kiểm tra lại ngày mai (next_day_areas)") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Bottom CTA Bar
        HseBottomCtaBar(
            primaryText = "Xác nhận & Chuyển Bước Ký Biên Bản →",
            onPrimaryClick = {
                onSaveAndProceedToApproval(
                    if (escalationEnabled) "ESCALATE" else "FIX_TODAY",
                    escalationEnabled,
                    selectedTriggers.toList(),
                    escalationDesc,
                    goodPoints,
                    reminders,
                    nextDayAreas
                )
            },
            primaryTestTag = "scr009_proceed_approval_btn"
        )
    }
}
