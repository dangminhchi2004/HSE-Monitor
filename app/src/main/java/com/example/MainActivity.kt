package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.HseScreen
import com.example.ui.HseViewModel
import com.example.ui.components.HseBottomNavigationBar
import com.example.ui.components.HseScreenNavigatorStrip
import com.example.ui.components.HseStackTopBar
import com.example.ui.components.HseTabTopBar
import com.example.ui.screens.Scr001LoginScreen
import com.example.ui.screens.Scr002DashboardScreen
import com.example.ui.screens.Scr003HistoryScreen
import com.example.ui.screens.Scr004GeneralInfoScreen
import com.example.ui.screens.Scr005ChecklistScreen
import com.example.ui.screens.Scr006InterviewScreen
import com.example.ui.screens.Scr007SummaryScreen
import com.example.ui.screens.Scr008RiskDetailScreen
import com.example.ui.screens.Scr009EscalationScreen
import com.example.ui.screens.Scr010ApprovalScreen
import com.example.ui.screens.Scr011SyncCenterScreen
import com.example.ui.screens.Scr012SettingsScreen
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SemanticPass

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AdcHseInspectionApp()
            }
        }
    }
}

@Composable
fun AdcHseInspectionApp(viewModel: HseViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val rememberSession by viewModel.rememberDeviceSession.collectAsStateWithLifecycle()
    val wifiOnlyPhotoSync by viewModel.wifiOnlyPhotoSync.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val checklistItems by viewModel.checklistItems.collectAsStateWithLifecycle()
    val catalogs by viewModel.catalogs.collectAsStateWithLifecycle()
    val allInspections by viewModel.allInspections.collectAsStateWithLifecycle()
    val allSummaries by viewModel.allSummaries.collectAsStateWithLifecycle()
    val allRisks by viewModel.allRisks.collectAsStateWithLifecycle()
    val syncQueue by viewModel.syncQueue.collectAsStateWithLifecycle()

    val activeInspection by viewModel.activeInspection.collectAsStateWithLifecycle()
    val activeResponses by viewModel.activeResponses.collectAsStateWithLifecycle()
    val activeInterviews by viewModel.activeInterviews.collectAsStateWithLifecycle()
    val activeSummary by viewModel.activeSummary.collectAsStateWithLifecycle()
    val activeRisks by viewModel.activeRisks.collectAsStateWithLifecycle()
    val activeEvidences by viewModel.activeEvidences.collectAsStateWithLifecycle()
    val activeApprovals by viewModel.activeApprovals.collectAsStateWithLifecycle()
    val activeRiskId by viewModel.activeRiskId.collectAsStateWithLifecycle()

    val selectedCategory by viewModel.activeCategoryFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.historySearchQuery.collectAsStateWithLifecycle()
    val historyFilterChip by viewModel.historyFilterChip.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val bannerMessage by viewModel.bannerMessage.collectAsStateWithLifecycle()
    val lastSyncedExcelRows by viewModel.lastSyncedExcelRows.collectAsStateWithLifecycle()

    val pendingSyncCount = allInspections.count { it.syncStatus != "SYNCED" }

    // Mandatory BackHandler for secondary screens
    BackHandler(enabled = currentScreen != HseScreen.SCR_001_LOGIN && currentScreen != HseScreen.SCR_002_DASHBOARD) {
        viewModel.navigateBack()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Column {
                when (currentScreen) {
                    HseScreen.SCR_001_LOGIN -> {
                        // Fullscreen without Top App Bar per SCR-001 spec, but keep compact 12-screen switcher strip at very top for instant testing
                    }
                    HseScreen.SCR_002_DASHBOARD -> {
                        HseTabTopBar(
                            title = "ADC Trang Chủ",
                            subtitle = "Nguyễn Văn A (Mã NV: ADC-8842)",
                            pendingSyncCount = pendingSyncCount,
                            onSyncBadgeClick = { viewModel.navigateTo(HseScreen.SCR_011_SYNC_CENTER) }
                        )
                    }
                    HseScreen.SCR_003_HISTORY -> {
                        HseTabTopBar(
                            title = "Lịch sử Kiểm tra HSE",
                            subtitle = "Tra cứu biên bản & Xuất báo cáo PDF",
                            pendingSyncCount = pendingSyncCount,
                            onSyncBadgeClick = { viewModel.navigateTo(HseScreen.SCR_011_SYNC_CENTER) }
                        )
                    }
                    HseScreen.SCR_012_SETTINGS -> {
                        HseTabTopBar(
                            title = "Cài đặt & Quản trị Hệ thống",
                            subtitle = "Danh mục Offline & Tài khoản Entra ID",
                            pendingSyncCount = pendingSyncCount,
                            onSyncBadgeClick = { viewModel.navigateTo(HseScreen.SCR_011_SYNC_CENTER) }
                        )
                    }
                    HseScreen.SCR_004_GENERAL_INFO -> {
                        HseStackTopBar(
                            title = "Thiết lập Thông tin chung",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "Bước 1/4",
                            screenCode = "SCR-004",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_005_CHECKLIST -> {
                        HseStackTopBar(
                            title = "Checklist Kiểm tra Hiện trường",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "Bước 2/4",
                            screenCode = "SCR-005",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_006_INTERVIEW -> {
                        HseStackTopBar(
                            title = "Trao đổi với Người lao động",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "Bước 3/4",
                            screenCode = "SCR-006",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_007_SUMMARY -> {
                        HseStackTopBar(
                            title = "Mục 3: Đánh giá & Phân tích CAPA",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "Bước 4/4",
                            screenCode = "SCR-007",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_008_RISK_DETAIL -> {
                        HseStackTopBar(
                            title = "Chi tiết Điểm Không Đạt",
                            subtitle = "Phiếu Phân tích Nguy cơ & Khắc phục",
                            stepBadge = "CAPA",
                            screenCode = "SCR-008",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_009_ESCALATION -> {
                        HseStackTopBar(
                            title = "Mục 5: Leo thang Sự cố & Đúc kết",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "Mục 5 & 6",
                            screenCode = "SCR-009",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_010_APPROVAL -> {
                        HseStackTopBar(
                            title = "Mục 7: Xác nhận Phiếu Kiểm tra",
                            subtitle = "Quy chuẩn An toàn ADC #HSE-402",
                            stepBadge = "3 Vai trò",
                            screenCode = "SCR-010",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                    HseScreen.SCR_011_SYNC_CENTER -> {
                        HseStackTopBar(
                            title = "Trung tâm Đồng bộ SharePoint",
                            subtitle = "Hệ thống Quản lý Dữ liệu HSE",
                            stepBadge = "tbl_2026_11",
                            screenCode = "SCR-011",
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }
                }

                // Quick 12-Screen Bar (hidden only on Login if desired, or visible on all authenticated screens)
                if (currentScreen != HseScreen.SCR_001_LOGIN) {
                    HseScreenNavigatorStrip(
                        currentScreen = currentScreen,
                        onSelectScreen = { viewModel.navigateTo(it) }
                    )
                }
            }
        },
        bottomBar = {
            if (currentScreen == HseScreen.SCR_002_DASHBOARD ||
                currentScreen == HseScreen.SCR_003_HISTORY ||
                currentScreen == HseScreen.SCR_012_SETTINGS
            ) {
                HseBottomNavigationBar(
                    currentScreen = currentScreen,
                    pendingSyncCount = pendingSyncCount,
                    onSelectScreen = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                HseScreen.SCR_001_LOGIN -> {
                    Scr001LoginScreen(
                        rememberSession = rememberSession,
                        onToggleRemember = { viewModel.toggleRememberSession(it) },
                        onSignInClick = { viewModel.signInWithMicrosoft365() }
                    )
                }
                HseScreen.SCR_002_DASHBOARD -> {
                    Scr002DashboardScreen(
                        inspections = allInspections,
                        summaries = allSummaries,
                        risks = allRisks,
                        onStartNewInspection = { viewModel.startNewInspectionWizard() },
                        onOpenInspection = { id, screen -> viewModel.selectInspection(id, screen) },
                        onOpenSyncCenter = { viewModel.navigateTo(HseScreen.SCR_011_SYNC_CENTER) },
                        onOpenHistory = { viewModel.navigateTo(HseScreen.SCR_003_HISTORY) }
                    )
                }
                HseScreen.SCR_003_HISTORY -> {
                    Scr003HistoryScreen(
                        inspections = allInspections,
                        summaries = allSummaries,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setHistorySearchQuery(it) },
                        selectedFilter = historyFilterChip,
                        onSelectFilter = { viewModel.setHistoryFilterChip(it) },
                        onOpenInspectionDetail = { id -> viewModel.selectInspection(id, HseScreen.SCR_007_SUMMARY) },
                        onExportPdf = { insp ->
                            viewModel.showBanner("Đã xuất biên bản PDF chuẩn ISO (${insp.documentCode}) cho phiếu #${insp.id}")
                        }
                    )
                }
                HseScreen.SCR_004_GENERAL_INFO -> {
                    Scr004GeneralInfoScreen(
                        activeInspection = activeInspection,
                        catalogs = catalogs,
                        onSaveAndProceed = { createNew, facCode, facName, areaCode, areaName, shift, mode, coop, weather ->
                            viewModel.saveStep1AndProceedToChecklist(
                                createNew = createNew,
                                factoryCode = facCode,
                                factoryName = facName,
                                areaCode = areaCode,
                                areaName = areaName,
                                shiftTime = shift,
                                inspectionMode = mode,
                                cooperatingPersonnel = coop,
                                weatherNote = weather
                            )
                        }
                    )
                }
                HseScreen.SCR_005_CHECKLIST -> {
                    Scr005ChecklistScreen(
                        categories = categories,
                        checklistItems = checklistItems,
                        responses = activeResponses,
                        evidences = activeEvidences,
                        selectedCategoryId = selectedCategory,
                        onSelectCategory = { viewModel.setCategoryFilter(it) },
                        onUpdateResponse = { resp, status, issue, note ->
                            viewModel.updateChecklistResponse(resp, status, issue, note)
                        },
                        onOpenRiskCapaDetail = { resp ->
                            viewModel.openRiskDetailForResponse(resp)
                        },
                        onProceedToInterview = {
                            viewModel.navigateTo(HseScreen.SCR_006_INTERVIEW)
                        }
                    )
                }
                HseScreen.SCR_006_INTERVIEW -> {
                    Scr006InterviewScreen(
                        interviews = activeInterviews,
                        evidences = activeEvidences,
                        onUpdateInterview = { interview, ans, note, qText ->
                            viewModel.saveInterviewItem(interview, ans, note, qText)
                        },
                        onCaptureInterviewPhoto = {
                            viewModel.captureInterviewEvidence()
                        },
                        onProceedToSummary = {
                            viewModel.navigateTo(HseScreen.SCR_007_SUMMARY)
                        }
                    )
                }
                HseScreen.SCR_007_SUMMARY -> {
                    Scr007SummaryScreen(
                        inspection = activeInspection,
                        summary = activeSummary,
                        risks = activeRisks,
                        onOpenRiskDetail = { risk ->
                            viewModel.openRiskDetailForResponse(null)
                        },
                        onOpenEscalationScreen = {
                            viewModel.navigateTo(HseScreen.SCR_009_ESCALATION)
                        },
                        onProceedToApproval = {
                            viewModel.navigateTo(HseScreen.SCR_010_APPROVAL)
                        }
                    )
                }
                HseScreen.SCR_008_RISK_DETAIL -> {
                    val currentRisk = activeRisks.find { it.id == activeRiskId } ?: activeRisks.firstOrNull()
                    Scr008RiskDetailScreen(
                        existingRisk = currentRisk,
                        evidences = activeEvidences,
                        onCapturePhoto = { viewModel.captureRiskPhoto() },
                        onCancel = { viewModel.navigateBack() },
                        onSaveRisk = { riskId, respId, itemCode, loc, desc, sev, action, owner, email, deadline, status ->
                            viewModel.saveRiskDetail(
                                existingRiskId = riskId,
                                responseId = respId,
                                checklistItemCode = itemCode,
                                areaLocation = loc,
                                description = desc,
                                severity = sev,
                                correctiveAction = action,
                                ownerName = owner,
                                ownerEmail = email,
                                deadline = deadline,
                                status = status
                            )
                        }
                    )
                }
                HseScreen.SCR_009_ESCALATION -> {
                    Scr009EscalationScreen(
                        summary = activeSummary,
                        onSaveAndProceedToApproval = { quickAssess, escFlag, triggers, escDesc, good, remind, nextDay ->
                            viewModel.saveEscalationAndLessons(
                                quickAssessment = quickAssess,
                                escalationFlag = escFlag,
                                triggers = triggers,
                                escalationDescription = escDesc,
                                goodPoints = good,
                                reminders = remind,
                                nextDayAreas = nextDay
                            )
                        }
                    )
                }
                HseScreen.SCR_010_APPROVAL -> {
                    Scr010ApprovalScreen(
                        approvals = activeApprovals,
                        onRecordSignature = { role, name, email, method, comment, attachPhoto ->
                            viewModel.recordRoleSignature(role, name, email, method, comment, attachPhoto)
                        },
                        onCompleteAndSaveInspection = {
                            viewModel.completeInspectionAndOpenSyncCenter()
                        }
                    )
                }
                HseScreen.SCR_011_SYNC_CENTER -> {
                    Scr011SyncCenterScreen(
                        inspections = allInspections,
                        syncQueue = syncQueue,
                        isSyncing = isSyncing,
                        wifiOnlyPhotoSync = wifiOnlyPhotoSync,
                        lastSyncedRows = lastSyncedExcelRows,
                        onToggleWifiOnly = { viewModel.toggleWifiOnlyPhotoSync(it) },
                        onSyncSingleInspection = { id -> viewModel.syncSingleInspection(id) },
                        onSyncAll = { viewModel.syncAllPending() }
                    )
                }
                HseScreen.SCR_012_SETTINGS -> {
                    Scr012SettingsScreen(
                        checklistItems = checklistItems,
                        catalogs = catalogs,
                        onClearCache = { viewModel.clearOfflineCacheMessage() },
                        onRefreshCatalogs = { viewModel.refreshMasterCatalogs() },
                        onSignOut = { viewModel.signOutMicrosoft365() }
                    )
                }
            }

            // Floating Toast / Notification Banner
            AnimatedVisibility(
                visible = bannerMessage != null,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    color = BrandPrimaryDark,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.dismissBanner() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4ADE80),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = bannerMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Đóng thông báo",
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
