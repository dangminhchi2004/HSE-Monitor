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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.local.ChecklistCategoryEntity
import com.example.data.local.ChecklistItemEntity
import com.example.data.local.EmployeeInterviewEntity
import com.example.data.local.InspectionEntity
import com.example.data.local.InspectionResponseEntity
import com.example.data.local.ReferenceCatalogEntity
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
import com.example.ui.theme.SemanticFailLight
import com.example.ui.theme.SemanticPass
import com.example.ui.theme.SemanticPassLight
import com.example.ui.theme.SemanticWarning
import com.example.ui.theme.SemanticWarningLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * SCR-004: Thông tin chung (Wizard Bước 1/4)
 * Chọn xưởng, ca làm việc, người thực hiện (auto-fill Entra ID), hình thức Định kỳ/Đột xuất, thời tiết & môi trường.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scr004GeneralInfoScreen(
    activeInspection: InspectionEntity?,
    catalogs: List<ReferenceCatalogEntity>,
    onSaveAndProceed: (
        createNew: Boolean,
        factoryCode: String,
        factoryName: String,
        areaCode: String,
        areaName: String,
        shiftTime: String,
        inspectionMode: String,
        cooperatingPersonnel: String,
        weatherNote: String
    ) -> Unit
) {
    val factories = catalogs.filter { it.catalogType == "FACTORY" }
    val allAreas = catalogs.filter { it.catalogType == "AREA" }
    val shifts = catalogs.filter { it.catalogType == "SHIFT" }

    var selectedFactoryCode by remember(activeInspection) {
        mutableStateOf(activeInspection?.factoryId ?: "FAC_01")
    }
    var selectedFactoryName by remember(activeInspection) {
        mutableStateOf(activeInspection?.factoryName ?: "Xưởng Dệt A")
    }

    val filteredAreas = allAreas.filter { it.parentCode == selectedFactoryCode }.ifEmpty { allAreas }
    var selectedAreaCode by remember(activeInspection, selectedFactoryCode) {
        mutableStateOf(
            activeInspection?.areaId?.takeIf { code -> filteredAreas.any { it.code == code } }
                ?: filteredAreas.firstOrNull()?.code
                ?: "AREA_TWIST_04"
        )
    }
    var selectedAreaName by remember(activeInspection, selectedFactoryCode) {
        mutableStateOf(
            activeInspection?.areaName?.takeIf { _ -> filteredAreas.any { it.code == selectedAreaCode } }
                ?: filteredAreas.firstOrNull()?.name
                ?: "Tổ máy xoắn số 04 - Line 1"
        )
    }

    var selectedShift by remember(activeInspection) {
        mutableStateOf(activeInspection?.shiftTime ?: (shifts.firstOrNull()?.name ?: "Ca 1 (06:00 - 14:00)"))
    }
    var inspectionMode by remember(activeInspection) {
        mutableStateOf(activeInspection?.inspectionMode ?: "PERIODIC")
    }
    var cooperatingPersonnel by remember(activeInspection) {
        mutableStateOf(activeInspection?.cooperatingPersonnel ?: "Trần Văn B (Tổ trưởng Bảo trì)")
    }
    var weatherNote by remember(activeInspection) {
        mutableStateOf(
            activeInspection?.weatherSpecialNote
                ?: "Nhiệt độ 29.5°C • Độ ồn 81 dBA • Chiếu sáng 420 Lux (Thiết bị đo đạt chuẩn)"
        )
    }

    var factoryExpanded by remember { mutableStateOf(false) }
    var areaExpanded by remember { mutableStateOf(false) }
    var shiftExpanded by remember { mutableStateOf(false) }

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
            // Document Header Banner
            item {
                Surface(
                    color = BrandPrimaryLight,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BrandPrimary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "MỤC 1. THÔNG TIN CHUNG PHIẾU KIỂM TRA",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Mã tài liệu: ADC_IFR_EH_QC_01_PL07 • Phiên bản: V1.0",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Surface(
                            color = BrandPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = activeInspection?.id ?: "PHIẾU MỚI",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.White),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Field 1: Người thực hiện (Auto-fill từ Microsoft Entra ID)
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "1. Người thực hiện & Đơn vị (Entra ID Auto-fill)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = "Nguyễn Văn A - Mã NV: ADC-8842 (Bộ phận HSE)",
                            onValueChange = {},
                            readOnly = true,
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = BrandPrimary)
                            },
                            label = { Text("Cán bộ Kiểm định An toàn") },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = InputSurface,
                                focusedContainerColor = InputSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = cooperatingPersonnel,
                            onValueChange = { cooperatingPersonnel = it },
                            label = { Text("Người / Đơn vị phối hợp hiện trường") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("cooperating_personnel_input")
                        )
                    }
                }
            }

            // Field 2 & 3: Khu vực / Nhà máy & Ca làm việc
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "2. Khu vực / Nhà máy & Ca làm việc",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Factory Dropdown
                        ExposedDropdownMenuBox(
                            expanded = factoryExpanded,
                            onExpandedChange = { factoryExpanded = !factoryExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedFactoryName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Chọn Nhà máy / Phân xưởng") },
                                leadingIcon = {
                                    Icon(Icons.Default.Factory, contentDescription = null, tint = BrandPrimary)
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = factoryExpanded)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("factory_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = factoryExpanded,
                                onDismissRequest = { factoryExpanded = false }
                            ) {
                                factories.forEach { fac ->
                                    DropdownMenuItem(
                                        text = { Text("${fac.name} (${fac.code})") },
                                        onClick = {
                                            selectedFactoryCode = fac.code
                                            selectedFactoryName = fac.name
                                            val firstArea = allAreas.firstOrNull { it.parentCode == fac.code }
                                            if (firstArea != null) {
                                                selectedAreaCode = firstArea.code
                                                selectedAreaName = firstArea.name
                                            }
                                            factoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Area Dropdown
                        ExposedDropdownMenuBox(
                            expanded = areaExpanded,
                            onExpandedChange = { areaExpanded = !areaExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedAreaName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Khu vực / Tổ máy kiểm tra cụ thể") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = areaExpanded)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("area_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = areaExpanded,
                                onDismissRequest = { areaExpanded = false }
                            ) {
                                filteredAreas.forEach { area ->
                                    DropdownMenuItem(
                                        text = { Text(area.name) },
                                        onClick = {
                                            selectedAreaCode = area.code
                                            selectedAreaName = area.name
                                            areaExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Shift Dropdown
                        ExposedDropdownMenuBox(
                            expanded = shiftExpanded,
                            onExpandedChange = { shiftExpanded = !shiftExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedShift,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Ca làm việc") },
                                leadingIcon = {
                                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = BrandPrimary)
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = shiftExpanded)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .testTag("shift_dropdown")
                            )
                            ExposedDropdownMenu(
                                expanded = shiftExpanded,
                                onDismissRequest = { shiftExpanded = false }
                            ) {
                                shifts.forEach { sh ->
                                    DropdownMenuItem(
                                        text = { Text(sh.name) },
                                        onClick = {
                                            selectedShift = sh.name
                                            shiftExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Field 4 & 5: Thời gian định tuyến (UTC+7), Hình thức & Môi trường
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3. Hình thức & Điều kiện Môi trường / Thiết bị",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val isPeriodic = inspectionMode == "PERIODIC"
                            FilterChip(
                                selected = isPeriodic,
                                onClick = { inspectionMode = "PERIODIC" },
                                label = { Text("Kiểm tra Định kỳ (PERIODIC)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = BrandPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = !isPeriodic,
                                onClick = { inspectionMode = "ADHOC" },
                                label = { Text("Đột xuất (ADHOC)") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SemanticWarning,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = "06/11/2026 (UTC+7 Giờ VN) → Định tuyến Sheet: 2026-11 (tbl_2026_11)",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ngày & Định tuyến Excel SharePoint tự động") },
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = InputSurface,
                                focusedContainerColor = InputSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = weatherNote,
                            onValueChange = { weatherNote = it },
                            label = { Text("Tình trạng máy móc & môi trường (Nhiệt độ, độ ồn, chiếu sáng)") },
                            leadingIcon = {
                                Icon(Icons.Default.Thermostat, contentDescription = null, tint = BrandPrimary)
                            },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("weather_environment_input")
                        )
                    }
                }
            }
        }

        // Bottom CTA Bar (One-thumb Zone)
        HseBottomCtaBar(
            primaryText = "Tiếp tục: Checklist 27 Hạng mục →",
            onPrimaryClick = {
                onSaveAndProceed(
                    false,
                    selectedFactoryCode,
                    selectedFactoryName,
                    selectedAreaCode,
                    selectedAreaName,
                    selectedShift,
                    inspectionMode,
                    cooperatingPersonnel,
                    weatherNote
                )
            },
            primaryTestTag = "scr004_continue_checklist_btn",
            secondaryText = "+ Tạo Phiếu Mới",
            onSecondaryClick = {
                onSaveAndProceed(
                    true,
                    selectedFactoryCode,
                    selectedFactoryName,
                    selectedAreaCode,
                    selectedAreaName,
                    selectedShift,
                    inspectionMode,
                    cooperatingPersonnel,
                    weatherNote
                )
            }
        )
    }
}

/**
 * SCR-005: Checklist 27 Hạng mục (Wizard Bước 2/4)
 * 7 nhóm A–G, 27 tiêu chí hiện trường, 3 nút [Đạt] | [Không đạt] | [K/A] (>= 48dp touch target),
 * hiển thị trích dẫn vi phạm, nút Chi tiết CAPA -> và 2 thumbnail ảnh thực địa ở Mục #09.
 */
@Composable
fun Scr005ChecklistScreen(
    categories: List<ChecklistCategoryEntity>,
    checklistItems: List<ChecklistItemEntity>,
    responses: List<InspectionResponseEntity>,
    evidences: List<RiskEvidenceEntity>,
    selectedCategoryId: String,
    onSelectCategory: (String) -> Unit,
    onUpdateResponse: (InspectionResponseEntity, String, String?, String?) -> Unit,
    onOpenRiskCapaDetail: (InspectionResponseEntity) -> Unit,
    onProceedToInterview: () -> Unit
) {
    val responseByItemCode = responses.associateBy { it.checklistItemId }
    val passCount = responses.count { it.status == "PASS" }
    val failCount = responses.count { it.status == "FAIL" }
    val naCount = responses.count { it.status == "NA" }
    val answeredCount = passCount + failCount + naCount

    val activeCategory = categories.find { it.id == selectedCategoryId } ?: categories.firstOrNull()
    val filteredItems = if (selectedCategoryId == "ALL") {
        checklistItems
    } else {
        checklistItems.filter { it.categoryId == selectedCategoryId }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface)
    ) {
        // Category Tabs A..G + ALL
        Surface(
            color = CardSurface,
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        val isAll = selectedCategoryId == "ALL"
                        FilterChip(
                            selected = isAll,
                            onClick = { onSelectCategory("ALL") },
                            label = {
                                Text(
                                    "Tất cả (27)",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("cat_chip_all")
                        )
                    }
                    items(categories, key = { it.id }) { cat ->
                        val isSelected = selectedCategoryId == cat.id
                        val countInCat = checklistItems.count { it.categoryId == cat.id }
                        FilterChip(
                            selected = isSelected,
                            onClick = { onSelectCategory(cat.id) },
                            label = {
                                Text(
                                    "Nhóm ${cat.id} ($countInCat)",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPrimary,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("cat_chip_${cat.id.lowercase()}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (selectedCategoryId == "ALL") {
                        "TOÀN BỘ 27 HẠNG MỤC KIỂM TRA HIỆN TRƯỜNG (NHÓM A – G)"
                    } else {
                        "${activeCategory?.id ?: "B"}. ${activeCategory?.title ?: "MÁY MÓC – THIẾT BỊ – KHU VỰC LÀM VIỆC"}"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = BrandPrimary,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }

        // Checklist Item Cards
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredItems, key = { it.id }) { item ->
                val resp = responseByItemCode[item.id]
                val currentStatus = resp?.status ?: "PASS"
                val isPass = currentStatus == "PASS"
                val isFail = currentStatus == "FAIL"
                val isNa = currentStatus == "NA"

                val cardBorderColor = when (currentStatus) {
                    "FAIL" -> SemanticFail
                    "PASS" -> SemanticPass.copy(alpha = 0.45f)
                    else -> CardBorder
                }

                FieldOpsCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("checklist_card_${item.itemNumber}"),
                    borderColor = cardBorderColor
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Top Row: Item # Badge + Status Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = BrandPrimaryLight,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = String.format("Mục #%02d • Nhóm %s", item.itemNumber, item.categoryId),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            when (currentStatus) {
                                "PASS" -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SemanticPassLight)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "✓ Đạt chuẩn",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = SemanticPass,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                                "FAIL" -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SemanticFailLight)
                                            .border(1.dp, SemanticFail, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "★ Rủi ro Cao (Không đạt)",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = SemanticFail,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(InputSurface)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Không áp dụng (K/A)",
                                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Question Content
                        Text(
                            text = item.content,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3 Segmented Control Buttons: [Đạt] | [Không đạt] | [K/A] (48dp height for gloved hands)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // [Đạt]
                            Button(
                                onClick = {
                                    if (resp != null) onUpdateResponse(resp, "PASS", null, resp.note)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPass) SemanticPass else InputSurface,
                                    contentColor = if (isPass) Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("btn_pass_${item.itemNumber}")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Đạt", fontWeight = FontWeight.Bold)
                            }

                            // [Không đạt]
                            Button(
                                onClick = {
                                    if (resp != null) onUpdateResponse(resp, "FAIL", resp.issueDescription, resp.note)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isFail) SemanticFail else InputSurface,
                                    contentColor = if (isFail) Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("btn_fail_${item.itemNumber}")
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Không đạt", fontWeight = FontWeight.Bold)
                            }

                            // [K/A]
                            OutlinedButton(
                                onClick = {
                                    if (resp != null) onUpdateResponse(resp, "NA", null, resp.note)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, if (isNa) BrandPrimary else CardBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isNa) BrandPrimaryLight else Color.Transparent,
                                    contentColor = if (isNa) BrandPrimary else TextSecondary
                                ),
                                modifier = Modifier
                                    .weight(0.75f)
                                    .heightIn(min = 48.dp)
                                    .testTag("btn_na_${item.itemNumber}")
                            ) {
                                Text("K/A", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Expanded FAIL details (Mandatory issue_description + CAPA Detail button + 2 Evidence Thumbnails)
                        if (isFail && resp != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                color = SemanticFailLight.copy(alpha = 0.55f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SemanticFail.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "MÔ TẢ VI PHẠM HIỆN TRƯỜNG (BẮT BUỘC KHI FAIL):",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SemanticFail,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    var issueDraft by remember(resp.id, resp.issueDescription) {
                                        mutableStateOf(
                                            resp.issueDescription
                                                ?: "Phát hiện rò rỉ dầu thủy lực tại cụm cấp dầu bôi trơn Tổ máy xoắn số 04..."
                                        )
                                    }
                                    OutlinedTextField(
                                        value = issueDraft,
                                        onValueChange = {
                                            issueDraft = it
                                            onUpdateResponse(resp, "FAIL", it, resp.note)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = CardSurface,
                                            unfocusedContainerColor = CardSurface
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("fail_issue_input_${item.itemNumber}")
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Thumbnails row (2 evidence photos)
                                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                        val itemEvidences = evidences.take(2)
                                        if (itemEvidences.isNotEmpty()) {
                                            items(itemEvidences, key = { it.id }) { ev ->
                                                EvidenceThumbnailCard(
                                                    fileName = ev.fileName,
                                                    caption = "Ảnh hiện trường #${item.itemNumber}",
                                                    uploadStatus = ev.uploadStatus
                                                )
                                            }
                                        } else {
                                            item {
                                                EvidenceThumbnailCard(
                                                    fileName = "oil_leak_01.jpg",
                                                    caption = "Vị trí dầu tràn sàn máy",
                                                    uploadStatus = "PENDING"
                                                )
                                            }
                                            item {
                                                EvidenceThumbnailCard(
                                                    fileName = "safety_ruler_02.jpg",
                                                    caption = "Góc máy thước đo an toàn",
                                                    uploadStatus = "PENDING"
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Button(
                                        onClick = { onOpenRiskCapaDetail(resp) },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SemanticFail,
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 48.dp)
                                            .testTag("open_capa_detail_btn_${item.itemNumber}")
                                    ) {
                                        Text(
                                            text = "Chi tiết CAPA & Phân công Khắc phục →",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Progress & Step 3 CTA
        Column {
            LinearProgressIndicator(
                progress = { (answeredCount / 27f).coerceIn(0f, 1f) },
                color = SemanticPass,
                trackColor = CardBorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            HseBottomCtaBar(
                primaryText = "Chuyển Bước 3: Trao đổi NLĐ →",
                onPrimaryClick = onProceedToInterview,
                primaryTestTag = "scr005_proceed_interview_btn",
                progressSummary = "Tiến độ: $answeredCount/27 hạng mục đã kiểm tra ($passCount Đạt • $failCount Không đạt • $naCount K/A)"
            )
        }
    }
}

/**
 * SCR-006: Trao đổi với Người lao động (Wizard Bước 3/4)
 * Ghi nhận phỏng vấn công nhân vận hành + 4 câu hỏi chuẩn Q1..Q4 theo Mục 2.B biểu mẫu PL07 + đính kèm ảnh hiện trường.
 */
@Composable
fun Scr006InterviewScreen(
    interviews: List<EmployeeInterviewEntity>,
    evidences: List<RiskEvidenceEntity>,
    onUpdateInterview: (EmployeeInterviewEntity, String, String, String?) -> Unit,
    onCaptureInterviewPhoto: () -> Unit,
    onProceedToSummary: () -> Unit
) {
    var workerName by remember { mutableStateOf("Lê Văn Hùng (Mã NV: ADC-4519)") }
    var machinePosition by remember { mutableStateOf("Tổ máy xoắn số 04 - Line 1") }
    var yearsExperience by remember { mutableStateOf("4.5 năm kinh nghiệm vận hành") }
    var emergencyStopTrained by remember { mutableStateOf(true) }

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
            // Worker Profile & Emergency Stop Training Block
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "MỤC 2.B: THÔNG TIN NGƯỜI LAO ĐỘNG ĐƯỢC TRAO ĐỔI",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = workerName,
                            onValueChange = { workerName = it },
                            label = { Text("Họ tên công nhân vận hành") },
                            leadingIcon = {
                                Icon(Icons.Default.Engineering, contentDescription = null, tint = BrandPrimary)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("worker_name_input")
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = machinePosition,
                                onValueChange = { machinePosition = it },
                                label = { Text("Vị trí tổ máy") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = yearsExperience,
                                onValueChange = { yearsExperience = it },
                                label = { Text("Thâm niên") },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(0.85f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = CardBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Câu hỏi trắc nghiệm nhanh: \"Đã được huấn luyện quy trình dừng khẩn cấp (E-Stop / LOTO) chưa?\"",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { emergencyStopTrained = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (emergencyStopTrained) SemanticPass else InputSurface,
                                    contentColor = if (emergencyStopTrained) Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("estop_trained_yes_btn")
                            ) {
                                Text("✓ [Có] Đã huấn luyện", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { emergencyStopTrained = false },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!emergencyStopTrained) SemanticFail else InputSurface,
                                    contentColor = if (!emergencyStopTrained) Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                                    .testTag("estop_trained_no_btn")
                            ) {
                                Text("✕ [Chưa] Cần đào tạo lại", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 4 Standard Questions Q1..Q4 from employee_interviews table
            items(interviews, key = { it.id }) { interview ->
                var answerDraft by remember(interview.id, interview.answerText) {
                    mutableStateOf(interview.answerText ?: "")
                }
                var noteDraft by remember(interview.id, interview.intervieweeNote) {
                    mutableStateOf(interview.intervieweeNote ?: "")
                }

                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = BrandPrimaryLight,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Câu hỏi ${interview.questionCode} (Bảng employee_interviews)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = interview.questionText,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = answerDraft,
                            onValueChange = {
                                answerDraft = it
                                onUpdateInterview(interview, it, noteDraft, null)
                            },
                            label = { Text("Ghi nhận ý kiến / câu trả lời của người lao động") },
                            minLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("interview_answer_${interview.questionCode.lowercase()}")
                        )
                    }
                }
            }

            // Photo Evidence Block (+ Chụp ảnh hiện trường phỏng vấn)
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "BẰNG CHỨNG ẢNH HIỆN TRƯỜNG PHỎNG VẤN",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onCaptureInterviewPhoto,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.5.dp, BrandPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("capture_interview_photo_btn")
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = BrandPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "+ Chụp ảnh hiện trường phỏng vấn",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        if (evidences.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(evidences, key = { it.id }) { ev ->
                                    EvidenceThumbnailCard(
                                        fileName = ev.fileName,
                                        caption = ev.evidenceType,
                                        uploadStatus = ev.uploadStatus
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom CTA Bar
        HseBottomCtaBar(
            primaryText = "Tiếp tục: Đánh giá & CAPA →",
            onPrimaryClick = onProceedToSummary,
            primaryTestTag = "scr006_proceed_summary_btn"
        )
    }
}
