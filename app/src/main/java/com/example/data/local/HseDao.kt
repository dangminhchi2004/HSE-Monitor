package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HseDao {

    // --- 1. Master & Catalog Queries ---
    @Query("SELECT * FROM checklist_categories WHERE is_active = 1 ORDER BY display_order ASC")
    fun observeCategories(): Flow<List<ChecklistCategoryEntity>>

    @Query("SELECT * FROM checklist_items WHERE is_active = 1 ORDER BY item_number ASC")
    fun observeChecklistItems(): Flow<List<ChecklistItemEntity>>

    @Query("SELECT * FROM checklist_items WHERE is_active = 1 ORDER BY item_number ASC")
    suspend fun getChecklistItemsOnce(): List<ChecklistItemEntity>

    @Query("SELECT * FROM reference_catalogs WHERE is_active = 1 ORDER BY catalog_type ASC, display_order ASC")
    fun observeReferenceCatalogs(): Flow<List<ReferenceCatalogEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategories(categories: List<ChecklistCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferenceCatalogs(catalogs: List<ReferenceCatalogEntity>)

    // --- 2. Inspections ---
    @Query("SELECT * FROM inspections WHERE is_deleted = 0 ORDER BY local_date DESC, inspection_date DESC")
    fun observeAllInspections(): Flow<List<InspectionEntity>>

    @Query("SELECT * FROM inspections WHERE id = :inspectionId LIMIT 1")
    fun observeInspectionById(inspectionId: String): Flow<InspectionEntity?>

    @Query("SELECT * FROM inspections WHERE id = :inspectionId LIMIT 1")
    suspend fun getInspectionById(inspectionId: String): InspectionEntity?

    @Query("SELECT COUNT(*) FROM inspections WHERE is_deleted = 0")
    suspend fun getInspectionCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionEntity)

    @Update
    suspend fun updateInspection(inspection: InspectionEntity)

    // --- 3. Responses (27 items) ---
    @Query("SELECT * FROM inspection_responses WHERE inspection_id = :inspectionId ORDER BY item_number ASC")
    fun observeResponsesByInspection(inspectionId: String): Flow<List<InspectionResponseEntity>>

    @Query("SELECT * FROM inspection_responses WHERE inspection_id = :inspectionId ORDER BY item_number ASC")
    suspend fun getResponsesByInspection(inspectionId: String): List<InspectionResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponses(responses: List<InspectionResponseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertResponse(response: InspectionResponseEntity)

    // --- 4. Employee Interviews (4 questions) ---
    @Query("SELECT * FROM employee_interviews WHERE inspection_id = :inspectionId ORDER BY question_number ASC")
    fun observeInterviewsByInspection(inspectionId: String): Flow<List<EmployeeInterviewEntity>>

    @Query("SELECT * FROM employee_interviews WHERE inspection_id = :inspectionId ORDER BY question_number ASC")
    suspend fun getInterviewsByInspection(inspectionId: String): List<EmployeeInterviewEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterviews(interviews: List<EmployeeInterviewEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInterview(interview: EmployeeInterviewEntity)

    // --- 5. Summaries (1-1 with inspection) ---
    @Query("SELECT * FROM inspection_summaries WHERE inspection_id = :inspectionId LIMIT 1")
    fun observeSummaryByInspection(inspectionId: String): Flow<InspectionSummaryEntity?>

    @Query("SELECT * FROM inspection_summaries WHERE inspection_id = :inspectionId LIMIT 1")
    suspend fun getSummaryByInspection(inspectionId: String): InspectionSummaryEntity?

    @Query("SELECT * FROM inspection_summaries")
    fun observeAllSummaries(): Flow<List<InspectionSummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSummary(summary: InspectionSummaryEntity)

    // --- 6. Risks (CAPA) ---
    @Query("SELECT * FROM inspection_risks WHERE inspection_id = :inspectionId ORDER BY risk_order ASC")
    fun observeRisksByInspection(inspectionId: String): Flow<List<InspectionRiskEntity>>

    @Query("SELECT * FROM inspection_risks WHERE inspection_id = :inspectionId ORDER BY risk_order ASC")
    suspend fun getRisksByInspection(inspectionId: String): List<InspectionRiskEntity>

    @Query("SELECT * FROM inspection_risks ORDER BY created_at DESC")
    fun observeAllRisks(): Flow<List<InspectionRiskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRisk(risk: InspectionRiskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRisks(risks: List<InspectionRiskEntity>)

    // --- 7. Risk Evidences ---
    @Query("SELECT * FROM risk_evidences WHERE inspection_id = :inspectionId ORDER BY created_at DESC")
    fun observeEvidencesByInspection(inspectionId: String): Flow<List<RiskEvidenceEntity>>

    @Query("SELECT * FROM risk_evidences WHERE inspection_id = :inspectionId")
    suspend fun getEvidencesByInspection(inspectionId: String): List<RiskEvidenceEntity>

    @Query("SELECT * FROM risk_evidences")
    fun observeAllEvidences(): Flow<List<RiskEvidenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvidence(evidence: RiskEvidenceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidences(evidences: List<RiskEvidenceEntity>)

    // --- 8. Approvals (3 Roles: HOST, HSE, MANAGER) ---
    @Query("SELECT * FROM inspection_approvals WHERE inspection_id = :inspectionId")
    fun observeApprovalsByInspection(inspectionId: String): Flow<List<InspectionApprovalEntity>>

    @Query("SELECT * FROM inspection_approvals WHERE inspection_id = :inspectionId")
    suspend fun getApprovalsByInspection(inspectionId: String): List<InspectionApprovalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertApproval(approval: InspectionApprovalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApprovals(approvals: List<InspectionApprovalEntity>)

    // --- 9. Sync Queue ---
    @Query("SELECT * FROM sync_queue ORDER BY priority ASC, created_at DESC")
    fun observeSyncQueue(): Flow<List<SyncQueueEntity>>

    @Query("SELECT * FROM sync_queue WHERE inspection_id = :inspectionId ORDER BY priority ASC")
    suspend fun getSyncQueueForInspection(inspectionId: String): List<SyncQueueEntity>

    @Query("SELECT * FROM sync_queue WHERE sync_state IN ('PENDING', 'FAILED', 'CONFLICT') ORDER BY priority ASC, next_attempt_at ASC")
    suspend fun getPendingSyncQueue(): List<SyncQueueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSyncQueueItems(items: List<SyncQueueEntity>)

    @Update
    suspend fun updateSyncQueueItems(items: List<SyncQueueEntity>)

    @Query("UPDATE sync_queue SET sync_state = :newState, updated_at = :updatedAt, last_error_code = NULL, last_error_message = NULL WHERE inspection_id = :inspectionId")
    suspend fun updateSyncQueueStateForInspection(inspectionId: String, newState: String, updatedAt: String)

    // --- 10. Auto-initialization Transaction (Section 6.1 Rule 1) ---
    @Transaction
    suspend fun initializeNewInspectionTransaction(
        inspection: InspectionEntity,
        responses: List<InspectionResponseEntity>,
        interviews: List<EmployeeInterviewEntity>,
        summary: InspectionSummaryEntity,
        approvals: List<InspectionApprovalEntity>
    ) {
        insertInspection(inspection)
        insertResponses(responses)
        insertInterviews(interviews)
        upsertSummary(summary)
        insertApprovals(approvals)
    }
}
