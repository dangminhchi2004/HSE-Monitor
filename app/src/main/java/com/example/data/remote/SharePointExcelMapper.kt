package com.example.data.remote

import com.example.data.local.EmployeeInterviewEntity
import com.example.data.local.InspectionApprovalEntity
import com.example.data.local.InspectionEntity
import com.example.data.local.InspectionResponseEntity
import com.example.data.local.InspectionRiskEntity
import com.example.data.local.InspectionSummaryEntity
import com.example.data.local.RiskEvidenceEntity
import org.json.JSONArray
import org.json.JSONObject

/**
 * Maps local Room SQLite records into the 26-column flattened SharePoint Excel Table row format
 * (`tbl_YYYY_MM` inside `HSE_Inspections_<YYYY>.xlsx`) as defined in Section 4.2 & 4.3 of DDS-APP-001.
 */
data class ExcelTableRow(
    val recordId: String,          // Col 0: RecordId (UUID - Idempotency Key)
    val recordType: String,        // Col 1: RecordType (INSPECTION, RESPONSE, INTERVIEW, RISK, SUMMARY, APPROVAL)
    val inspectionId: String,      // Col 2: InspectionId
    val parentRecordId: String,    // Col 3: ParentRecordId
    val inspectionDateVn: String,  // Col 4: InspectionDate (YYYY-MM-DD HH:mm UTC+7)
    val factory: String,           // Col 5: Factory
    val area: String,              // Col 6: Area
    val shift: String,             // Col 7: Shift
    val inspectorEmail: String,    // Col 8: InspectorEmail
    val inspectionMode: String,    // Col 9: InspectionMode (PERIODIC / ADHOC)
    val checklistItemCode: String, // Col 10: ChecklistItemCode (ITEM_01..ITEM_27)
    val categoryCode: String,      // Col 11: CategoryCode (A..G or Q1..Q4)
    val status: String,            // Col 12: Status
    val severity: String,          // Col 13: Severity (HIGH / MEDIUM / LOW / ESCALATED)
    val description: String,       // Col 14: Description
    val correctiveAction: String,  // Col 15: CorrectiveAction
    val ownerEmail: String,        // Col 16: OwnerEmail
    val deadline: String,          // Col 17: Deadline
    val riskStatus: String,        // Col 18: RiskStatus (OPEN / IN_PROGRESS / CLOSED)
    val evidenceUrl: String,       // Col 19: EvidenceUrl (SharePoint HSE_Evidence URLs separated by ';')
    val schemaVersion: String,     // Col 20: SchemaVersion ("1.1")
    val syncVersion: Int,          // Col 21: SyncVersion
    val createdAt: String,         // Col 22: CreatedAt
    val updatedAt: String,         // Col 23: UpdatedAt
    val updatedBy: String,         // Col 24: UpdatedBy
    val payloadJson: String        // Col 25: PayloadJson (< 32,767 chars)
) {
    fun toValueList(): List<Any> = listOf(
        recordId,
        recordType,
        inspectionId,
        parentRecordId,
        inspectionDateVn,
        factory,
        area,
        shift,
        inspectorEmail,
        inspectionMode,
        checklistItemCode,
        categoryCode,
        status,
        severity,
        description,
        correctiveAction,
        ownerEmail,
        deadline,
        riskStatus,
        evidenceUrl,
        schemaVersion,
        syncVersion,
        createdAt,
        updatedAt,
        updatedBy,
        payloadJson
    )
}

object SharePointExcelMapper {

    fun buildBatchRowsForInspection(
        inspection: InspectionEntity,
        responses: List<InspectionResponseEntity>,
        interviews: List<EmployeeInterviewEntity>,
        summary: InspectionSummaryEntity?,
        risks: List<InspectionRiskEntity>,
        approvals: List<InspectionApprovalEntity>,
        evidences: List<RiskEvidenceEntity>
    ): List<ExcelTableRow> {
        val rows = mutableListOf<ExcelTableRow>()
        val inspectionDateDisplay = "${inspection.localDate} 08:30"
        val evidenceByRisk = evidences.groupBy { it.riskId }
        val evidenceById = evidences.associateBy { it.id }

        // 1. INSPECTION Row (1 row)
        val inspectionPayload = JSONObject().apply {
            put("documentCode", inspection.documentCode)
            put("checklistVersion", inspection.checklistVersion)
            put("inspectorName", inspection.inspectorName)
            put("executingUnit", inspection.executingUnit)
            put("cooperatingPersonnel", inspection.cooperatingPersonnel ?: "")
            put("weatherSpecialNote", inspection.weatherSpecialNote ?: "")
            put("completedAt", inspection.completedAt ?: inspection.updatedAt)
        }.toString()

        rows.add(
            ExcelTableRow(
                recordId = inspection.id,
                recordType = "INSPECTION",
                inspectionId = inspection.id,
                parentRecordId = inspection.id,
                inspectionDateVn = inspectionDateDisplay,
                factory = inspection.factoryName,
                area = inspection.areaName,
                shift = inspection.shiftTime,
                inspectorEmail = inspection.inspectorEmail,
                inspectionMode = inspection.inspectionMode,
                checklistItemCode = "",
                categoryCode = "",
                status = "COMPLETED",
                severity = "",
                description = inspection.weatherSpecialNote ?: "",
                correctiveAction = "",
                ownerEmail = inspection.inspectorEmail,
                deadline = "",
                riskStatus = "",
                evidenceUrl = "",
                schemaVersion = "1.1",
                syncVersion = inspection.syncVersion,
                createdAt = inspection.createdAt,
                updatedAt = inspection.updatedAt,
                updatedBy = inspection.inspectorEmail,
                payloadJson = inspectionPayload
            )
        )

        // 2. RESPONSE Rows (27 rows)
        responses.forEach { resp ->
            val respPayload = JSONObject().apply {
                put("itemNumber", resp.itemNumber)
                put("categoryId", resp.categoryId)
                put("status", resp.status)
                put("issueDescription", resp.issueDescription ?: "")
                put("note", resp.note ?: "")
            }.toString()

            rows.add(
                ExcelTableRow(
                    recordId = resp.id,
                    recordType = "RESPONSE",
                    inspectionId = inspection.id,
                    parentRecordId = inspection.id,
                    inspectionDateVn = inspectionDateDisplay,
                    factory = inspection.factoryName,
                    area = inspection.areaName,
                    shift = inspection.shiftTime,
                    inspectorEmail = inspection.inspectorEmail,
                    inspectionMode = inspection.inspectionMode,
                    checklistItemCode = resp.checklistItemId,
                    categoryCode = resp.categoryId,
                    status = resp.status,
                    severity = if (resp.status == "FAIL") "HIGH" else "",
                    description = resp.issueDescription ?: "",
                    correctiveAction = resp.note ?: "",
                    ownerEmail = "",
                    deadline = "",
                    riskStatus = "",
                    evidenceUrl = "",
                    schemaVersion = "1.1",
                    syncVersion = resp.syncVersion,
                    createdAt = inspection.createdAt,
                    updatedAt = resp.updatedAt,
                    updatedBy = resp.updatedBy,
                    payloadJson = respPayload
                )
            )
        }

        // 3. INTERVIEW Rows (4 rows)
        interviews.forEach { interview ->
            val interviewPayload = JSONObject().apply {
                put("questionCode", interview.questionCode)
                put("questionNumber", interview.questionNumber)
                put("questionText", interview.questionText)
                put("answerText", interview.answerText ?: "")
            }.toString()

            rows.add(
                ExcelTableRow(
                    recordId = interview.id,
                    recordType = "INTERVIEW",
                    inspectionId = inspection.id,
                    parentRecordId = inspection.id,
                    inspectionDateVn = inspectionDateDisplay,
                    factory = inspection.factoryName,
                    area = inspection.areaName,
                    shift = inspection.shiftTime,
                    inspectorEmail = inspection.inspectorEmail,
                    inspectionMode = inspection.inspectionMode,
                    checklistItemCode = "",
                    categoryCode = interview.questionCode,
                    status = "RECORDED",
                    severity = "",
                    description = interview.answerText ?: "",
                    correctiveAction = interview.intervieweeNote ?: "",
                    ownerEmail = "",
                    deadline = "",
                    riskStatus = "",
                    evidenceUrl = "",
                    schemaVersion = "1.1",
                    syncVersion = interview.syncVersion,
                    createdAt = inspection.createdAt,
                    updatedAt = interview.updatedAt,
                    updatedBy = inspection.inspectorEmail,
                    payloadJson = interviewPayload
                )
            )
        }

        // 4. SUMMARY Row (1 row)
        if (summary != null) {
            val summaryPayload = JSONObject().apply {
                put("passCount", summary.passCount)
                put("failCount", summary.failCount)
                put("naCount", summary.naCount)
                put("quickAssessment", summary.quickAssessment ?: "SAFE")
                put("escalationFlag", summary.escalationFlag == 1)
                put("escalationTriggers", JSONArray(summary.escalationTriggersJson ?: "[]"))
                put("escalationDescription", summary.escalationDescription ?: JSONObject.NULL)
                put("goodPoints", summary.goodPoints ?: "")
                put("reminders", summary.reminders ?: "")
                put("nextDayAreas", summary.nextDayAreas ?: "")
            }.toString()

            rows.add(
                ExcelTableRow(
                    recordId = summary.id,
                    recordType = "SUMMARY",
                    inspectionId = inspection.id,
                    parentRecordId = inspection.id,
                    inspectionDateVn = inspectionDateDisplay,
                    factory = inspection.factoryName,
                    area = inspection.areaName,
                    shift = inspection.shiftTime,
                    inspectorEmail = inspection.inspectorEmail,
                    inspectionMode = inspection.inspectionMode,
                    checklistItemCode = "",
                    categoryCode = "",
                    status = summary.quickAssessment ?: "SAFE",
                    severity = if (summary.escalationFlag == 1) "ESCALATED" else "",
                    description = summary.escalationDescription ?: summary.goodPoints ?: "",
                    correctiveAction = summary.reminders ?: "",
                    ownerEmail = "",
                    deadline = "",
                    riskStatus = "",
                    evidenceUrl = "",
                    schemaVersion = "1.1",
                    syncVersion = summary.syncVersion,
                    createdAt = inspection.createdAt,
                    updatedAt = summary.updatedAt,
                    updatedBy = inspection.inspectorEmail,
                    payloadJson = summaryPayload
                )
            )
        }

        // 5. RISK Rows (N rows)
        risks.forEach { risk ->
            val riskEvidences = evidenceByRisk[risk.id].orEmpty()
            val evidenceUrls = riskEvidences.mapNotNull { it.remoteUrl ?: "https://adc.sharepoint.com/sites/HSE/HSE_Evidence/${it.fileName}" }
                .joinToString(";")
            val evidenceIdsArray = JSONArray()
            riskEvidences.forEach { evidenceIdsArray.put(it.id) }

            val riskPayload = JSONObject().apply {
                put("riskOrder", risk.riskOrder)
                put("responseId", risk.responseId ?: "")
                put("checklistItemCode", risk.checklistItemCode ?: "")
                put("areaLocation", risk.areaLocation)
                put("description", risk.description)
                put("severity", risk.severity)
                put("correctiveAction", risk.correctiveAction)
                put("ownerName", risk.ownerName)
                put("ownerEmail", risk.ownerEmail ?: "")
                put("deadline", risk.deadline)
                put("status", risk.status)
                put("evidenceIds", evidenceIdsArray)
            }.toString()

            rows.add(
                ExcelTableRow(
                    recordId = risk.id,
                    recordType = "RISK",
                    inspectionId = inspection.id,
                    parentRecordId = risk.responseId ?: inspection.id,
                    inspectionDateVn = inspectionDateDisplay,
                    factory = inspection.factoryName,
                    area = risk.areaLocation,
                    shift = inspection.shiftTime,
                    inspectorEmail = inspection.inspectorEmail,
                    inspectionMode = inspection.inspectionMode,
                    checklistItemCode = risk.checklistItemCode ?: "",
                    categoryCode = "",
                    status = risk.status,
                    severity = risk.severity,
                    description = risk.description,
                    correctiveAction = risk.correctiveAction,
                    ownerEmail = risk.ownerEmail ?: risk.ownerName,
                    deadline = risk.deadline,
                    riskStatus = risk.status,
                    evidenceUrl = evidenceUrls,
                    schemaVersion = "1.1",
                    syncVersion = risk.syncVersion,
                    createdAt = risk.createdAt,
                    updatedAt = risk.updatedAt,
                    updatedBy = inspection.inspectorEmail,
                    payloadJson = riskPayload
                )
            )
        }

        // 6. APPROVAL Rows (3 rows)
        approvals.forEach { approval ->
            val approvalEvidenceUrl = approval.evidenceId?.let { evId ->
                evidenceById[evId]?.let { it.remoteUrl ?: "https://adc.sharepoint.com/sites/HSE/HSE_Evidence/${it.fileName}" }
            } ?: ""

            val approvalPayload = JSONObject().apply {
                put("role", approval.role)
                put("approverName", approval.approverName)
                put("approverEmail", approval.approverEmail ?: "")
                put("approvalMethod", approval.approvalMethod)
                put("signedAt", approval.signedAt)
                put("comment", approval.comment ?: "")
            }.toString()

            rows.add(
                ExcelTableRow(
                    recordId = approval.id,
                    recordType = "APPROVAL",
                    inspectionId = inspection.id,
                    parentRecordId = inspection.id,
                    inspectionDateVn = inspectionDateDisplay,
                    factory = inspection.factoryName,
                    area = inspection.areaName,
                    shift = inspection.shiftTime,
                    inspectorEmail = inspection.inspectorEmail,
                    inspectionMode = inspection.inspectionMode,
                    checklistItemCode = "",
                    categoryCode = approval.role,
                    status = approval.approvalMethod,
                    severity = "",
                    description = approval.approverName,
                    correctiveAction = approval.comment ?: "",
                    ownerEmail = approval.approverEmail ?: approval.approverName,
                    deadline = "",
                    riskStatus = "",
                    evidenceUrl = approvalEvidenceUrl,
                    schemaVersion = "1.1",
                    syncVersion = approval.syncVersion,
                    createdAt = inspection.createdAt,
                    updatedAt = approval.signedAt,
                    updatedBy = approval.approverEmail ?: inspection.inspectorEmail,
                    payloadJson = approvalPayload
                )
            )
        }

        return rows
    }
}
