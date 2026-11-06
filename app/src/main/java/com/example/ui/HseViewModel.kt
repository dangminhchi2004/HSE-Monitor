package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ChecklistCategoryEntity
import com.example.data.local.ChecklistItemEntity
import com.example.data.local.EmployeeInterviewEntity
import com.example.data.local.HseDatabase
import com.example.data.local.InspectionApprovalEntity
import com.example.data.local.InspectionEntity
import com.example.data.local.InspectionResponseEntity
import com.example.data.local.InspectionRiskEntity
import com.example.data.local.InspectionSummaryEntity
import com.example.data.local.ReferenceCatalogEntity
import com.example.data.local.RiskEvidenceEntity
import com.example.data.local.SyncQueueEntity
import com.example.data.remote.ExcelTableRow
import com.example.data.repository.HseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class HseScreen(val code: String, val title: String, val isMainTab: Boolean = false) {
    SCR_001_LOGIN("SCR-001", "Đăng nhập Microsoft 365", false),
    SCR_002_DASHBOARD("SCR-002", "Bảng điều khiển Tổng quan", true),
    SCR_003_HISTORY("SCR-003", "Lịch sử & Tra cứu Phiếu Kiểm tra", true),
    SCR_004_GENERAL_INFO("SCR-004", "Thiết lập Thông tin chung", false),
    SCR_005_CHECKLIST("SCR-005", "Checklist Kiểm tra Hiện trường", false),
    SCR_006_INTERVIEW("SCR-006", "Trao đổi với Người lao động", false),
    SCR_007_SUMMARY("SCR-007", "Mục 3: Đánh giá & Phân tích CAPA", false),
    SCR_008_RISK_DETAIL("SCR-008", "Chi tiết Điểm Không Đạt", false),
    SCR_009_ESCALATION("SCR-009", "Mục 5: Leo thang Sự cố & Đúc kết", false),
    SCR_010_APPROVAL("SCR-010", "Mục 7: Xác nhận Phiếu Kiểm tra", false),
    SCR_011_SYNC_CENTER("SCR-011", "Trung tâm Đồng bộ SharePoint", true),
    SCR_012_SETTINGS("SCR-012", "Cài đặt & Quản trị Hệ thống", true)
}

@OptIn(ExperimentalCoroutinesApi::class)
class HseViewModel(application: Application) : AndroidViewModel(application) {

    private val database = HseDatabase.getInstance(application)
    private val repository = HseRepository(database.hseDao())

    // Navigation & Session State
    private val _currentScreen = MutableStateFlow(HseScreen.SCR_001_LOGIN)
    val currentScreen: StateFlow<HseScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<HseScreen>()

    private val _rememberDeviceSession = MutableStateFlow(true)
    val rememberDeviceSession: StateFlow<Boolean> = _rememberDeviceSession.asStateFlow()

    private val _wifiOnlyPhotoSync = MutableStateFlow(true)
    val wifiOnlyPhotoSync: StateFlow<Boolean> = _wifiOnlyPhotoSync.asStateFlow()

    private val _activeInspectionId = MutableStateFlow("HSE-2026-1106-01")
    val activeInspectionId: StateFlow<String> = _activeInspectionId.asStateFlow()

    private val _activeRiskId = MutableStateFlow<String?>("RISK-2026-1106-01")
    val activeRiskId: StateFlow<String?> = _activeRiskId.asStateFlow()

    private val _activeCategoryFilter = MutableStateFlow("B")
    val activeCategoryFilter: StateFlow<String> = _activeCategoryFilter.asStateFlow()

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _historyFilterChip = MutableStateFlow("ALL") // ALL, SYNCED, PENDING, HAS_FAIL
    val historyFilterChip: StateFlow<String> = _historyFilterChip.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _bannerMessage = MutableStateFlow<String?>(null)
    val bannerMessage: StateFlow<String?> = _bannerMessage.asStateFlow()

    private val _lastSyncedExcelRows = MutableStateFlow<List<ExcelTableRow>>(emptyList())
    val lastSyncedExcelRows: StateFlow<List<ExcelTableRow>> = _lastSyncedExcelRows.asStateFlow()

    // Master & Global Data Flows
    val categories: StateFlow<List<ChecklistCategoryEntity>> = repository.categoriesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val checklistItems: StateFlow<List<ChecklistItemEntity>> = repository.checklistItemsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val catalogs: StateFlow<List<ReferenceCatalogEntity>> = repository.catalogsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInspections: StateFlow<List<InspectionEntity>> = repository.inspectionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSummaries: StateFlow<List<InspectionSummaryEntity>> = repository.allSummariesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRisks: StateFlow<List<InspectionRiskEntity>> = repository.allRisksFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEvidences: StateFlow<List<RiskEvidenceEntity>> = repository.allEvidencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val syncQueue: StateFlow<List<SyncQueueEntity>> = repository.syncQueueFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Inspection Scoped Flows
    val activeInspection: StateFlow<InspectionEntity?> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(null) else repository.observeInspectionById(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeResponses: StateFlow<List<InspectionResponseEntity>> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(emptyList()) else repository.observeResponses(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeInterviews: StateFlow<List<EmployeeInterviewEntity>> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(emptyList()) else repository.observeInterviews(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeSummary: StateFlow<InspectionSummaryEntity?> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(null) else repository.observeSummary(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeRisks: StateFlow<List<InspectionRiskEntity>> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(emptyList()) else repository.observeRisks(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeEvidences: StateFlow<List<RiskEvidenceEntity>> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(emptyList()) else repository.observeEvidences(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeApprovals: StateFlow<List<InspectionApprovalEntity>> = _activeInspectionId
        .flatMapLatest { id -> if (id.isBlank()) flowOf(emptyList()) else repository.observeApprovals(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    fun navigateTo(screen: HseScreen, clearBackStack: Boolean = false) {
        if (clearBackStack) {
            _screenHistory.clear()
        } else if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
        }
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        return if (_screenHistory.isNotEmpty()) {
            val previous = _screenHistory.removeAt(_screenHistory.lastIndex)
            _currentScreen.value = previous
            true
        } else if (_currentScreen.value != HseScreen.SCR_002_DASHBOARD && _currentScreen.value != HseScreen.SCR_001_LOGIN) {
            _currentScreen.value = HseScreen.SCR_002_DASHBOARD
            true
        } else {
            false
        }
    }

    fun signInWithMicrosoft365() {
        showBanner("Đã xác thực SSO Microsoft Entra ID: Nguyễn Văn A (ADC-8842)")
        navigateTo(HseScreen.SCR_002_DASHBOARD, clearBackStack = true)
    }

    fun signOutMicrosoft365() {
        showBanner("Đã đăng xuất khỏi phiên Microsoft 365")
        navigateTo(HseScreen.SCR_001_LOGIN, clearBackStack = true)
    }

    fun toggleRememberSession(value: Boolean) {
        _rememberDeviceSession.value = value
    }

    fun toggleWifiOnlyPhotoSync(value: Boolean) {
        _wifiOnlyPhotoSync.value = value
    }

    fun setCategoryFilter(categoryId: String) {
        _activeCategoryFilter.value = categoryId
    }

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun setHistoryFilterChip(chip: String) {
        _historyFilterChip.value = chip
    }

    fun selectInspection(inspectionId: String, targetScreen: HseScreen = HseScreen.SCR_007_SUMMARY) {
        _activeInspectionId.value = inspectionId
        navigateTo(targetScreen)
    }

    fun startNewInspectionWizard() {
        // Ensure we have an active inspection or open SCR-004 directly
        navigateTo(HseScreen.SCR_004_GENERAL_INFO)
    }

    fun saveStep1AndProceedToChecklist(
        createNew: Boolean,
        factoryCode: String,
        factoryName: String,
        areaCode: String,
        areaName: String,
        shiftTime: String,
        inspectionMode: String,
        cooperatingPersonnel: String,
        weatherNote: String
    ) {
        viewModelScope.launch {
            if (createNew || _activeInspectionId.value.isBlank()) {
                val newId = repository.createNewInspectionDraft(
                    inspectorName = "Nguyễn Văn A (ADC-8842)",
                    inspectorEmail = "nguyenvana@adchealthsafety.com",
                    executingUnit = "Bộ phận HSE",
                    cooperatingPersonnel = cooperatingPersonnel,
                    factoryCode = factoryCode,
                    factoryName = factoryName,
                    areaCode = areaCode,
                    areaName = areaName,
                    shiftTime = shiftTime,
                    inspectionMode = inspectionMode,
                    weatherNote = weatherNote
                )
                _activeInspectionId.value = newId
                showBanner("Đã khởi tạo Phiếu $newId (27 hạng mục & 4 câu hỏi NLĐ)")
            } else {
                repository.updateGeneralInfo(
                    inspectionId = _activeInspectionId.value,
                    factoryCode = factoryCode,
                    factoryName = factoryName,
                    areaCode = areaCode,
                    areaName = areaName,
                    shiftTime = shiftTime,
                    inspectionMode = inspectionMode,
                    cooperatingPersonnel = cooperatingPersonnel,
                    weatherNote = weatherNote
                )
                showBanner("Đã lưu thông tin chung Phiếu ${_activeInspectionId.value}")
            }
            navigateTo(HseScreen.SCR_005_CHECKLIST)
        }
    }

    fun updateChecklistResponse(
        response: InspectionResponseEntity,
        status: String,
        issueDescription: String? = response.issueDescription,
        note: String? = response.note
    ) {
        viewModelScope.launch {
            repository.updateResponseStatus(response, status, issueDescription, note)
        }
    }

    fun openRiskDetailForResponse(response: InspectionResponseEntity?) {
        val currentRisks = activeRisks.value
        val matched = if (response != null) {
            currentRisks.find { it.responseId == response.id || it.checklistItemCode == response.checklistItemId }
        } else {
            currentRisks.firstOrNull()
        }
        _activeRiskId.value = matched?.id
        navigateTo(HseScreen.SCR_008_RISK_DETAIL)
    }

    fun saveInterviewItem(
        interview: EmployeeInterviewEntity,
        answerText: String,
        intervieweeNote: String,
        questionTextOverride: String? = null
    ) {
        viewModelScope.launch {
            repository.updateInterviewAnswer(interview, answerText, intervieweeNote, questionTextOverride)
        }
    }

    fun captureInterviewEvidence() {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            val ev = repository.addInterviewPhotoEvidence(inspId)
            showBanner("Đã đính kèm ảnh hiện trường: ${ev.fileName} (SHA-256 verified)")
        }
    }

    fun saveRiskDetail(
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
    ) {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            val saved = repository.saveRiskCapa(
                inspectionId = inspId,
                existingRiskId = existingRiskId,
                responseId = responseId,
                checklistItemCode = checklistItemCode,
                areaLocation = areaLocation,
                description = description,
                severity = severity,
                correctiveAction = correctiveAction,
                ownerName = ownerName,
                ownerEmail = ownerEmail,
                deadline = deadline,
                status = status
            )
            _activeRiskId.value = saved.id
            showBanner("Đã lưu Điểm Không Đạt (${saved.id}) vào SQLite Room")
            navigateTo(HseScreen.SCR_007_SUMMARY)
        }
    }

    fun captureRiskPhoto() {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            val ev = repository.addRiskPhotoEvidence(inspId, _activeRiskId.value)
            showBanner("Đã chụp ảnh bằng chứng CAPA: ${ev.fileName}")
        }
    }

    fun saveEscalationAndLessons(
        quickAssessment: String,
        escalationFlag: Boolean,
        triggers: List<String>,
        escalationDescription: String,
        goodPoints: String,
        reminders: String,
        nextDayAreas: String
    ) {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            repository.updateEscalationAndLessons(
                inspectionId = inspId,
                quickAssessment = quickAssessment,
                escalationFlag = escalationFlag,
                triggers = triggers,
                escalationDescription = escalationDescription,
                goodPoints = goodPoints,
                reminders = reminders,
                nextDayAreas = nextDayAreas
            )
            if (escalationFlag) {
                showBanner("Đã kích hoạt Escalation khẩn cấp & lưu bài học kinh nghiệm")
            } else {
                showBanner("Đã lưu Mục 5 & Mục 6 (Bài học kinh nghiệm cuối ca)")
            }
            navigateTo(HseScreen.SCR_010_APPROVAL)
        }
    }

    fun recordRoleSignature(
        role: String,
        approverName: String,
        approverEmail: String,
        approvalMethod: String,
        comment: String,
        attachPaperPhoto: Boolean = false
    ) {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            repository.updateApprovalSignature(
                inspectionId = inspId,
                role = role,
                approverName = approverName,
                approverEmail = approverEmail,
                approvalMethod = approvalMethod,
                comment = comment,
                attachPaperPhoto = attachPaperPhoto
            )
            val methodLabel = if (approvalMethod == "PAPER") "Biên bản giấy (Ảnh chụp)" else "Chữ ký điện tử (ISO-8601)"
            showBanner("Đã ghi nhận xác nhận [$role]: $methodLabel")
        }
    }

    fun completeInspectionAndOpenSyncCenter() {
        val inspId = _activeInspectionId.value
        if (inspId.isBlank()) return
        viewModelScope.launch {
            val res = repository.completeAndEnqueueInspection(inspId)
            res.onSuccess { count ->
                showBanner("Đã hoàn tất Phiếu $inspId & xếp $count bản ghi vào Sync Queue!")
                navigateTo(HseScreen.SCR_011_SYNC_CENTER)
            }.onFailure { err ->
                showBanner(err.message ?: "Lỗi hoàn tất phiếu")
            }
        }
    }

    fun syncSingleInspection(inspectionId: String) {
        if (_isSyncing.value) return
        viewModelScope.launch {
            _isSyncing.value = true
            val rows = repository.syncInspectionToSharePoint(inspectionId)
            _lastSyncedExcelRows.value = rows
            _isSyncing.value = false
            showBanner("Đồng bộ thành công ${rows.size} dòng vào bảng Excel tbl_2026_11!")
        }
    }

    fun syncAllPending() {
        if (_isSyncing.value) return
        viewModelScope.launch {
            _isSyncing.value = true
            val count = repository.syncAllPendingInspections()
            _isSyncing.value = false
            if (count > 0) {
                showBanner("Đã đồng bộ Batch toàn bộ ($count dòng) lên HSE_Inspections_2026.xlsx!")
            } else {
                showBanner("Tất cả phiếu kiểm tra đều đã đồng bộ (SYNCED).")
            }
        }
    }

    fun refreshMasterCatalogs() {
        viewModelScope.launch {
            repository.refreshMasterCatalogsFromCloud()
            showBanner("Đã đồng bộ 27 câu hỏi Checklist V1.0 & 12 Phân xưởng từ Cloud")
        }
    }

    fun clearOfflineCacheMessage() {
        showBanner("Đã dọn dẹp bộ nhớ đệm ảnh tạm (Giữ nguyên dữ liệu SQLite chưa đồng bộ)")
    }

    fun showBanner(message: String) {
        _bannerMessage.value = message
    }

    fun dismissBanner() {
        _bannerMessage.value = null
    }
}
