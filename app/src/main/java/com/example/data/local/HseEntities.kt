package com.example.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "checklist_categories",
    primaryKeys = ["id", "checklist_version"]
)
data class ChecklistCategoryEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "checklist_version", defaultValue = "'V1.0'") val checklistVersion: String = "V1.0",
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "display_order") val displayOrder: Int,
    @ColumnInfo(name = "is_active", defaultValue = "1") val isActive: Int = 1
)

@Entity(
    tableName = "checklist_items",
    primaryKeys = ["id", "checklist_version"],
    foreignKeys = [
        ForeignKey(
            entity = ChecklistCategoryEntity::class,
            parentColumns = ["id", "checklist_version"],
            childColumns = ["category_id", "checklist_version"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["category_id", "checklist_version"])]
)
data class ChecklistItemEntity(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "checklist_version", defaultValue = "'V1.0'") val checklistVersion: String = "V1.0",
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "item_number") val itemNumber: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "is_active", defaultValue = "1") val isActive: Int = 1
)

@Entity(
    tableName = "reference_catalogs",
    primaryKeys = ["catalog_type", "code"],
    indices = [Index(value = ["parent_code"])]
)
data class ReferenceCatalogEntity(
    @ColumnInfo(name = "catalog_type") val catalogType: String, // FACTORY, AREA, SHIFT, UNIT
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "parent_code") val parentCode: String? = null,
    @ColumnInfo(name = "display_order", defaultValue = "0") val displayOrder: Int = 0,
    @ColumnInfo(name = "is_active", defaultValue = "1") val isActive: Int = 1,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)

@Entity(
    tableName = "inspections",
    indices = [
        Index(value = ["local_date", "inspection_date"]),
        Index(value = ["status", "sync_status"]),
        Index(value = ["factory_id", "area_id"]),
        Index(value = ["inspector_email"]),
        Index(value = ["local_year_month"])
    ]
)
data class InspectionEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "document_code", defaultValue = "'ADC_IFR_EH_QC_01_PL07'") val documentCode: String = "ADC_IFR_EH_QC_01_PL07",
    @ColumnInfo(name = "checklist_version", defaultValue = "'V1.0'") val checklistVersion: String = "V1.0",
    @ColumnInfo(name = "inspection_date") val inspectionDate: String, // ISO-8601 UTC
    @ColumnInfo(name = "local_date") val localDate: String, // YYYY-MM-DD (UTC+7)
    @ColumnInfo(name = "local_year_month") val localYearMonth: String, // YYYY-MM (UTC+7)
    @ColumnInfo(name = "shift_time") val shiftTime: String,
    @ColumnInfo(name = "inspector_id") val inspectorId: String,
    @ColumnInfo(name = "inspector_email") val inspectorEmail: String,
    @ColumnInfo(name = "inspector_name") val inspectorName: String,
    @ColumnInfo(name = "executing_unit", defaultValue = "'Bộ phận HSE'") val executingUnit: String = "Bộ phận HSE",
    @ColumnInfo(name = "cooperating_personnel") val cooperatingPersonnel: String? = null,
    @ColumnInfo(name = "factory_id") val factoryId: String,
    @ColumnInfo(name = "factory_name") val factoryName: String,
    @ColumnInfo(name = "area_id") val areaId: String,
    @ColumnInfo(name = "area_name") val areaName: String,
    @ColumnInfo(name = "inspection_mode", defaultValue = "'PERIODIC'") val inspectionMode: String = "PERIODIC", // PERIODIC | ADHOC
    @ColumnInfo(name = "weather_special_note") val weatherSpecialNote: String? = null,
    @ColumnInfo(name = "status", defaultValue = "'DRAFT'") val status: String = "DRAFT", // DRAFT, IN_PROGRESS, COMPLETED_LOCAL, LOCKED
    @ColumnInfo(name = "sync_status", defaultValue = "'LOCAL_ONLY'") val syncStatus: String = "LOCAL_ONLY", // LOCAL_ONLY, PENDING, SYNCING, SYNCED, FAILED, CONFLICT
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1,
    @ColumnInfo(name = "remote_file_id") val remoteFileId: String? = null,
    @ColumnInfo(name = "remote_sheet_name") val remoteSheetName: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
    @ColumnInfo(name = "completed_at") val completedAt: String? = null,
    @ColumnInfo(name = "synced_at") val syncedAt: String? = null,
    @ColumnInfo(name = "is_deleted", defaultValue = "0") val isDeleted: Int = 0
)

@Entity(
    tableName = "inspection_responses",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["inspection_id", "checklist_item_id"], unique = true),
        Index(value = ["inspection_id", "item_number"]),
        Index(value = ["status"])
    ]
)
data class InspectionResponseEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "checklist_item_id") val checklistItemId: String,
    @ColumnInfo(name = "category_id") val categoryId: String,
    @ColumnInfo(name = "item_number") val itemNumber: Int,
    @ColumnInfo(name = "status") val status: String, // PASS | FAIL | NA | UNANSWERED
    @ColumnInfo(name = "issue_description") val issueDescription: String? = null,
    @ColumnInfo(name = "note") val note: String? = null,
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1,
    @ColumnInfo(name = "updated_at") val updatedAt: String,
    @ColumnInfo(name = "updated_by") val updatedBy: String
)

@Entity(
    tableName = "employee_interviews",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["inspection_id", "question_code"], unique = true)
    ]
)
data class EmployeeInterviewEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "question_code") val questionCode: String, // Q1, Q2, Q3, Q4
    @ColumnInfo(name = "question_number") val questionNumber: Int, // 1..4
    @ColumnInfo(name = "question_text") val questionText: String,
    @ColumnInfo(name = "answer_text") val answerText: String? = null,
    @ColumnInfo(name = "interviewee_note") val intervieweeNote: String? = null,
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)

@Entity(
    tableName = "inspection_summaries",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["inspection_id"], unique = true)
    ]
)
data class InspectionSummaryEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "pass_count", defaultValue = "0") val passCount: Int = 0,
    @ColumnInfo(name = "fail_count", defaultValue = "0") val failCount: Int = 0,
    @ColumnInfo(name = "na_count", defaultValue = "0") val naCount: Int = 0,
    @ColumnInfo(name = "unanswered_count", defaultValue = "27") val unansweredCount: Int = 27,
    @ColumnInfo(name = "quick_assessment") val quickAssessment: String? = null, // SAFE | FIX_TODAY | MONITOR | ESCALATE
    @ColumnInfo(name = "escalation_flag", defaultValue = "0") val escalationFlag: Int = 0, // 0 | 1
    @ColumnInfo(name = "escalation_triggers_json", defaultValue = "'[]'") val escalationTriggersJson: String? = "[]",
    @ColumnInfo(name = "escalation_description") val escalationDescription: String? = null,
    @ColumnInfo(name = "good_points") val goodPoints: String? = null,
    @ColumnInfo(name = "reminders") val reminders: String? = null,
    @ColumnInfo(name = "next_day_areas") val nextDayAreas: String? = null,
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)

@Entity(
    tableName = "inspection_risks",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = InspectionResponseEntity::class,
            parentColumns = ["id"],
            childColumns = ["response_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["inspection_id"]),
        Index(value = ["response_id"]),
        Index(value = ["severity", "status"]),
        Index(value = ["owner_email", "deadline"])
    ]
)
data class InspectionRiskEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "response_id") val responseId: String? = null,
    @ColumnInfo(name = "checklist_item_code") val checklistItemCode: String? = null,
    @ColumnInfo(name = "risk_order", defaultValue = "1") val riskOrder: Int = 1,
    @ColumnInfo(name = "area_location") val areaLocation: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "severity") val severity: String, // HIGH | MEDIUM | LOW
    @ColumnInfo(name = "corrective_action") val correctiveAction: String,
    @ColumnInfo(name = "owner_id") val ownerId: String? = null,
    @ColumnInfo(name = "owner_name") val ownerName: String,
    @ColumnInfo(name = "owner_email") val ownerEmail: String? = null,
    @ColumnInfo(name = "deadline") val deadline: String, // YYYY-MM-DD
    @ColumnInfo(name = "status", defaultValue = "'OPEN'") val status: String = "OPEN", // OPEN | IN_PROGRESS | CLOSED
    @ColumnInfo(name = "closed_at") val closedAt: String? = null,
    @ColumnInfo(name = "closed_by") val closedBy: String? = null,
    @ColumnInfo(name = "closure_note") val closureNote: String? = null,
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)

@Entity(
    tableName = "risk_evidences",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = InspectionRiskEntity::class,
            parentColumns = ["id"],
            childColumns = ["risk_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["inspection_id", "risk_id"]),
        Index(value = ["risk_id"]),
        Index(value = ["upload_status"]),
        Index(value = ["sha256_checksum"])
    ]
)
data class RiskEvidenceEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "risk_id") val riskId: String? = null,
    @ColumnInfo(name = "response_id") val responseId: String? = null,
    @ColumnInfo(name = "evidence_type", defaultValue = "'RISK_PHOTO'") val evidenceType: String = "RISK_PHOTO", // RISK_PHOTO, CHECKLIST_PHOTO, PAPER_SIGNATURE, REMEDIATION_PHOTO
    @ColumnInfo(name = "file_name") val fileName: String,
    @ColumnInfo(name = "local_path") val localPath: String,
    @ColumnInfo(name = "mime_type", defaultValue = "'image/jpeg'") val mimeType: String = "image/jpeg",
    @ColumnInfo(name = "size_bytes", defaultValue = "0") val sizeBytes: Long = 0,
    @ColumnInfo(name = "sha256_checksum") val sha256Checksum: String,
    @ColumnInfo(name = "remote_drive_item_id") val remoteDriveItemId: String? = null,
    @ColumnInfo(name = "remote_url") val remoteUrl: String? = null,
    @ColumnInfo(name = "upload_status", defaultValue = "'CAPTURED_LOCAL'") val uploadStatus: String = "CAPTURED_LOCAL", // CAPTURED_LOCAL, PENDING, UPLOADING, UPLOADED, FAILED
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "uploaded_at") val uploadedAt: String? = null
)

@Entity(
    tableName = "inspection_approvals",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = RiskEvidenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["evidence_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["inspection_id", "role"], unique = true),
        Index(value = ["evidence_id"])
    ]
)
data class InspectionApprovalEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "role") val role: String, // HOST | HSE | MANAGER
    @ColumnInfo(name = "approver_id") val approverId: String? = null,
    @ColumnInfo(name = "approver_email") val approverEmail: String? = null,
    @ColumnInfo(name = "approver_name") val approverName: String,
    @ColumnInfo(name = "approval_method", defaultValue = "'ELECTRONIC'") val approvalMethod: String = "ELECTRONIC", // ELECTRONIC | PAPER
    @ColumnInfo(name = "signed_at") val signedAt: String,
    @ColumnInfo(name = "evidence_id") val evidenceId: String? = null,
    @ColumnInfo(name = "comment") val comment: String? = null,
    @ColumnInfo(name = "sync_version", defaultValue = "1") val syncVersion: Int = 1
)

@Entity(
    tableName = "sync_queue",
    foreignKeys = [
        ForeignKey(
            entity = InspectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["inspection_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["record_id"], unique = true),
        Index(value = ["sync_state", "next_attempt_at", "priority"]),
        Index(value = ["inspection_id"])
    ]
)
data class SyncQueueEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "inspection_id") val inspectionId: String,
    @ColumnInfo(name = "record_id") val recordId: String,
    @ColumnInfo(name = "record_type") val recordType: String, // EVIDENCE_FILE, INSPECTION, RESPONSE, INTERVIEW, RISK, SUMMARY, APPROVAL
    @ColumnInfo(name = "operation", defaultValue = "'UPSERT'") val operation: String = "UPSERT", // UPSERT, UPLOAD_FILE
    @ColumnInfo(name = "priority", defaultValue = "10") val priority: Int = 10,
    @ColumnInfo(name = "target_year_file") val targetYearFile: String, // HSE_Inspections_YYYY.xlsx
    @ColumnInfo(name = "target_month_sheet") val targetMonthSheet: String, // YYYY-MM
    @ColumnInfo(name = "sync_state", defaultValue = "'PENDING'") val syncState: String = "PENDING", // PENDING, IN_FLIGHT, SYNCED, FAILED, CONFLICT, BLOCKED
    @ColumnInfo(name = "retry_count", defaultValue = "0") val retryCount: Int = 0,
    @ColumnInfo(name = "next_attempt_at", defaultValue = "0") val nextAttemptAt: Long = 0,
    @ColumnInfo(name = "last_error_code") val lastErrorCode: String? = null,
    @ColumnInfo(name = "last_error_message") val lastErrorMessage: String? = null,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "updated_at") val updatedAt: String
)
