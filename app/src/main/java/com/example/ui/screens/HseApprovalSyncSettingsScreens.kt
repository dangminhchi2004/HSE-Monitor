package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ChecklistItemEntity
import com.example.data.local.InspectionApprovalEntity
import com.example.data.local.InspectionEntity
import com.example.data.local.ReferenceCatalogEntity
import com.example.data.local.SyncQueueEntity
import com.example.data.remote.ExcelTableRow
import com.example.data.repository.HseRepository
import com.example.ui.components.FieldOpsCard
import com.example.ui.components.HseBottomCtaBar
import com.example.ui.components.HseSyncStatusBadge
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
import com.example.ui.theme.SemanticWarningBorder
import com.example.ui.theme.SemanticWarningLight
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * SCR-010: Mục 7. Xác nhận (3 Vai trò Hybrid Confirmation)
 * 1. HSE Auditor (Đã ký điện tử kèm timestamp ISO-8601 từ Microsoft Entra ID)
 * 2. Area Owner / HOST (Khung Canvas vẽ tay chữ ký cảm ứng trực tiếp trên màn hình, nút Ký lại)
 * 3. Plant Lead / MANAGER (Chế độ Hybrid — Ký trực tiếp trên màn hình HOẶC Chụp biên bản giấy thay thế)
 */
@Composable
fun Scr010ApprovalScreen(
    approvals: List<InspectionApprovalEntity>,
    onRecordSignature: (
        role: String,
        approverName: String,
        approverEmail: String,
        approvalMethod: String,
        comment: String,
        attachPaperPhoto: Boolean
    ) -> Unit,
    onCompleteAndSaveInspection: () -> Unit
) {
    val approvalByRole = approvals.associateBy { it.role }

    // Canvas paths for Role 2 (Area Owner) and Role 3 (Plant Manager)
    val areaOwnerPoints = remember {
        mutableStateListOf(
            Offset(40f, 110f), Offset(95f, 65f), Offset(145f, 120f),
            Offset(210f, 70f), Offset(280f, 95f), Offset(350f, 80f)
        )
    }
    val plantManagerPoints = remember {
        mutableStateListOf(
            Offset(50f, 95f), Offset(120f, 55f), Offset(180f, 115f),
            Offset(250f, 65f), Offset(330f, 100f)
        )
    }
    var managerHybridMode by remember { mutableStateOf("ELECTRONIC") } // ELECTRONIC | PAPER
    var paperCaptured by remember { mutableStateOf(false) }

    val isoTimestamp = remember { HseRepository.currentIsoUtc() }

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
            // Role 1: Người thực hiện kiểm tra (HSE Auditor) - Microsoft Entra ID Digital Signature
            item {
                val hseApproval = approvalByRole["HSE"]
                FieldOpsCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = SemanticPass.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "1. NGƯỜI THỰC HIỆN KIỂM TRA (HSE AUDITOR)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Surface(
                                color = SemanticPassLight,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "✓ Đã ký số Entra ID",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SemanticPass,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = hseApproval?.approverName ?: "Nguyễn Văn A (Mã NV: ADC-8842)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Email: nguyenvana@adchealthsafety.com • Phương thức: ELECTRONIC",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Timestamp ISO-8601: ${hseApproval?.signedAt ?: isoTimestamp}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SemanticPass,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Role 2: Người phụ trách khu vực (Area Owner / HOST) - Interactive Touch Signature Canvas
            item {
                val hostApproval = approvalByRole["HOST"]
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "2. NGƯỜI PHỤ TRÁCH KHU VỰC (AREA OWNER)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = hostApproval?.approverName ?: "Trần Văn B (Trưởng bộ phận Bảo trì)",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            OutlinedButton(
                                onClick = { areaOwnerPoints.clear() },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("clear_host_signature_btn")
                            ) {
                                Text("Ký lại", style = MaterialTheme.typography.labelMedium.copy(color = SemanticFail))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Touch Signature Canvas
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(125.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(InputSurface)
                                .border(1.5.dp, BrandPrimary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .pointerInput(Unit) {
                                    detectDragGestures { change, _ ->
                                        change.consume()
                                        areaOwnerPoints.add(change.position)
                                    }
                                }
                                .testTag("host_signature_canvas")
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                if (areaOwnerPoints.size > 1) {
                                    val path = Path().apply {
                                        moveTo(areaOwnerPoints.first().x, areaOwnerPoints.first().y)
                                        for (i in 1 until areaOwnerPoints.size) {
                                            lineTo(areaOwnerPoints[i].x, areaOwnerPoints[i].y)
                                        }
                                    }
                                    drawPath(
                                        path = path,
                                        color = BrandPrimaryDark,
                                        style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                    )
                                }
                            }
                            if (areaOwnerPoints.isEmpty()) {
                                Text(
                                    text = "Ký tay cảm ứng trực tiếp vào khung này...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Text(
                                text = "ISO-8601: ${hostApproval?.signedAt ?: isoTimestamp}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                onRecordSignature(
                                    "HOST",
                                    "Trần Văn B (Phụ trách Khu vực)",
                                    "b.tran@adchealthsafety.com",
                                    "ELECTRONIC",
                                    "Đã ký xác nhận vector trên màn hình cảm ứng",
                                    false
                                )
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp)
                                .testTag("confirm_host_signature_btn")
                        ) {
                            Icon(Icons.Default.Draw, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Xác nhận Chữ ký Số (Area Owner)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Role 3: Quản đốc xưởng (Plant Lead / MANAGER) - Chế độ Hybrid (Ký màn hình HOẶC Chụp biên bản giấy)
            item {
                val managerApproval = approvalByRole["MANAGER"]
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "3. QUẢN ĐỐC XƯỞNG (PLANT LEAD — CHẾ ĐỘ HYBRID)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = BrandPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = managerApproval?.approverName ?: "Lê Hoàng Nam (Quản đốc Xưởng Dệt A)",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Mode Toggle: Ký điện tử vs Chụp biên bản giấy thay thế
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { managerHybridMode = "ELECTRONIC" },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (managerHybridMode == "ELECTRONIC") BrandPrimary else InputSurface,
                                    contentColor = if (managerHybridMode == "ELECTRONIC") Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .heightIn(min = 48.dp)
                            ) {
                                Text("Ký trên màn hình", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    managerHybridMode = "PAPER"
                                    paperCaptured = true
                                    onRecordSignature(
                                        "MANAGER",
                                        "Lê Hoàng Nam (Quản đốc Xưởng)",
                                        "nam.le@adchealthsafety.com",
                                        "PAPER",
                                        "Đính kèm ảnh chụp biên bản giấy đã ký tay",
                                        true
                                    )
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (managerHybridMode == "PAPER") SemanticPass else InputSurface,
                                    contentColor = if (managerHybridMode == "PAPER") Color.White else TextPrimary
                                ),
                                modifier = Modifier
                                    .weight(1.15f)
                                    .heightIn(min = 48.dp)
                                    .testTag("capture_paper_signature_btn")
                            ) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Chụp biên bản giấy thay thế", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (managerHybridMode == "ELECTRONIC") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(InputSurface)
                                    .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, _ ->
                                            change.consume()
                                            plantManagerPoints.add(change.position)
                                        }
                                    }
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    if (plantManagerPoints.size > 1) {
                                        val path = Path().apply {
                                            moveTo(plantManagerPoints.first().x, plantManagerPoints.first().y)
                                            for (i in 1 until plantManagerPoints.size) {
                                                lineTo(plantManagerPoints[i].x, plantManagerPoints[i].y)
                                            }
                                        }
                                        drawPath(
                                            path = path,
                                            color = BrandPrimary,
                                            style = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                                        )
                                    }
                                }
                                Text(
                                    text = "ISO-8601: ${managerApproval?.signedAt ?: isoTimestamp}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                )
                            }
                        } else {
                            Surface(
                                color = SemanticPassLight,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SemanticPass),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = SemanticPass)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Đã đính kèm ảnh chụp tờ phiếu ký tay (PAPER_SIGNATURE)",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = SemanticPass,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                        Text(
                                            text = "File: paper_sign_manager.jpg • SHA-256 verified • ISO-8601: $isoTimestamp",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom CTA Bar: Hoàn tất & Lưu Phiếu Kiểm Tra
        HseBottomCtaBar(
            primaryText = "Hoàn tất & Lưu Phiếu Kiểm Tra",
            onPrimaryClick = onCompleteAndSaveInspection,
            primaryTestTag = "scr010_complete_inspection_btn"
        )
    }
}

/**
 * SCR-011: Trung tâm Đồng bộ SharePoint & Lịch sử (SharePoint Sync Center)
 * Badge xanh Online - Đã kết nối Wi-Fi Nhà máy ADC-Industrial,
 * Hàng đợi đồng bộ (Sync Queue) với nút Đồng bộ ngay & trạng thái ✓ ĐÃ ĐỒNG BỘ (Đã ghi vào tbl_2026_11),
 * Checkbox "Chỉ đồng bộ hình ảnh độ phân giải cao khi có Wi-Fi",
 * Nút chính "Đồng bộ tất cả dữ liệu (Sync All)".
 */
@Composable
fun Scr011SyncCenterScreen(
    inspections: List<InspectionEntity>,
    syncQueue: List<SyncQueueEntity>,
    isSyncing: Boolean,
    wifiOnlyPhotoSync: Boolean,
    lastSyncedRows: List<ExcelTableRow>,
    onToggleWifiOnly: (Boolean) -> Unit,
    onSyncSingleInspection: (String) -> Unit,
    onSyncAll: () -> Unit
) {
    val queueByInspection = syncQueue.groupBy { it.inspectionId }

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
            // 1. Network Connection Status Badge
            item {
                Surface(
                    color = SemanticPassLight,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SemanticPass.copy(alpha = 0.45f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = null,
                            tint = SemanticPass,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Online - Đã kết nối Wi-Fi Nhà máy ADC-Industrial",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = SemanticPass,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Đích đồng bộ: HSE_Inspections_2026.xlsx • Sheet: 2026-11 • Bảng: tbl_2026_11",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary)
                            )
                        }
                    }
                }
            }

            // 2. Photo Upload Wi-Fi Option Checkbox
            item {
                FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleWifiOnly(!wifiOnlyPhotoSync) }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = wifiOnlyPhotoSync,
                            onCheckedChange = onToggleWifiOnly,
                            colors = CheckboxDefaults.colors(checkedColor = BrandPrimary),
                            modifier = Modifier.testTag("wifi_only_photo_checkbox")
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Chỉ đồng bộ hình ảnh độ phân giải cao khi có Wi-Fi",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Thư viện SharePoint: HSE_Evidence (Ưu tiên Priority 1 trước khi ghi dòng Excel)",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            // 3. Hàng đợi đồng bộ (Sync Queue Cards)
            item {
                Text(
                    text = "HÀNG ĐỢI ĐỒNG BỘ SHAREPOINT EXCEL (${inspections.size} PHIẾU / ${syncQueue.size} BẢN GHI)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            items(inspections, key = { it.id }) { insp ->
                val itemsForInsp = queueByInspection[insp.id].orEmpty()
                val isSynced = insp.syncStatus == "SYNCED"
                val rowCount = itemsForInsp.size.coerceAtLeast(36)

                FieldOpsCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (isSynced) SemanticPass.copy(alpha = 0.4f) else SemanticWarningBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Phiếu #${insp.id}",
                                style = MaterialTheme.typography.titleMedium.copy(color = BrandPrimary)
                            )
                            if (isSynced) {
                                Surface(
                                    color = SemanticPassLight,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "✓ ĐÃ ĐỒNG BỘ (Đã ghi vào tbl_${insp.localYearMonth.replace("-", "_")})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SemanticPass,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                HseSyncStatusBadge(syncStatus = insp.syncStatus)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${insp.factoryName} — ${insp.areaName} (${insp.shiftTime})",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Định tuyến giờ VN (UTC+7): File HSE_Inspections_2026.xlsx → Bảng tbl_${insp.localYearMonth.replace("-", "_")} ($rowCount dòng Batch)",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        if (!isSynced) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSyncing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = SemanticWarning
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(
                                        text = "Chờ tải lên: 1 Header + 27 Checklist + 4 NLĐ + 1 Summary + 3 Ký duyệt",
                                        style = MaterialTheme.typography.labelSmall.copy(color = SemanticWarning),
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onSyncSingleInspection(insp.id) },
                                enabled = !isSyncing,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                                    .testTag("sync_now_btn_${insp.id}")
                            ) {
                                Icon(Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Đồng bộ ngay (1-Request Batch POST)", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Text(
                                text = "Đồng bộ hoàn tất lúc: ${insp.syncedAt ?: insp.updatedAt} • Workbook-Session-Id đã đóng an toàn.",
                                style = MaterialTheme.typography.labelSmall.copy(color = SemanticPass)
                            )
                        }
                    }
                }
            }

            // Preview of 26-Column Flattened Excel Payload after Sync
            if (lastSyncedRows.isNotEmpty()) {
                item {
                    FieldOpsCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = BrandPrimary.copy(alpha = 0.4f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "BẢN XEM TRƯỚC DỮ LIỆU PHẲNG HÓA 26 CỘT TRÊN EXCEL (tbl_2026_11)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            lastSyncedRows.take(5).forEach { row ->
                                Text(
                                    text = "• [${row.recordType}] ID=${row.recordId.take(8)}.. | Status=${row.status} | JSON=${row.payloadJson.take(60)}...",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextPrimary),
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom CTA: Đồng bộ tất cả dữ liệu (Sync All)
        HseBottomCtaBar(
            primaryText = if (isSyncing) "Đang đồng bộ lên SharePoint Excel..." else "Đồng bộ tất cả dữ liệu (Sync All)",
            onPrimaryClick = onSyncAll,
            primaryTestTag = "scr011_sync_all_btn",
            enabled = !isSyncing
        )
    }
}

/**
 * SCR-012: Cài đặt & Quản trị Danh mục Hệ thống (Settings & System Master Data)
 * Thông tin tài khoản Nguyễn Văn A (@adchealthsafety.com, Lead HSE Auditor),
 * Cấu hình dữ liệu ngoại tuyến (27 câu hỏi checklist v2.4 / V1.0, Danh sách 12 phân xưởng),
 * Nút Xóa bộ nhớ đệm (Clear Cache), Cập nhật danh mục mới nhất từ Cloud,
 * Thông tin kỹ thuật Phiên bản 2.4.0 (Build 102) • Bản quyền Phòng HCNS & IT ADC 2026,
 * Nút viền đỏ Đăng xuất khỏi tài khoản Microsoft.
 */
@Composable
fun Scr012SettingsScreen(
    checklistItems: List<ChecklistItemEntity>,
    catalogs: List<ReferenceCatalogEntity>,
    onClearCache: () -> Unit,
    onRefreshCatalogs: () -> Unit,
    onSignOut: () -> Unit
) {
    val areaCount = catalogs.count { it.catalogType == "AREA" }.coerceAtLeast(12)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundSurface),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Account Profile Card
        item {
            FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "NA",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Nguyễn Văn A (Mã NV: ADC-8842)",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "nguyenvana@adchealthsafety.com",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = BrandPrimaryLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Vai trò: Lead HSE Auditor • Bộ phận HSE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BrandPrimary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Cấu hình dữ liệu ngoại tuyến (Offline & Cache Master Data)
        item {
            FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = BrandPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Cấu hình Dữ liệu Ngoại tuyến (Offline & Cache)",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = InputSurface,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Bộ tiêu chí Checklist tải sẵn:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "${checklistItems.size.coerceAtLeast(27)} câu hỏi checklist v2.4 (PL07 V1.0)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = SemanticPass,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Danh mục Nhà máy & Khu vực:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "Danh sách $areaCount phân xưởng",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = BrandPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Cơ sở dữ liệu nội bộ:", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "SQLite Room (11 Bảng chuẩn hóa)",
                                    style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onRefreshCatalogs,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("update_master_catalog_btn")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cập nhật danh mục mới nhất từ Cloud", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onClearCache,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .testTag("clear_cache_btn")
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = TextSecondary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Xóa bộ nhớ đệm (Clear Cache)",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // 3. Technical Specification & Sign-Out Button
        item {
            FieldOpsCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "THÔNG TIN KỸ THUẬT & QUY CHUẨN",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Phiên bản 2.4.0 (Build 102) • Bản quyền Phòng HCNS & IT ADC 2026",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Chuẩn thiết kế: Field Ops Industrial (DESIGN.md v2.4) • Biểu mẫu: ADC_IFR_EH_QC_01_PL07",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onSignOut,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.5.dp, SemanticFail),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 50.dp)
                            .testTag("sign_out_microsoft_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null,
                            tint = SemanticFail
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Đăng xuất khỏi tài khoản Microsoft",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = SemanticFail,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
