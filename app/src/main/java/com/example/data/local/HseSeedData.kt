package com.example.data.local

import java.util.UUID

object HseSeedData {

    fun getStandardCategories(): List<ChecklistCategoryEntity> = listOf(
        ChecklistCategoryEntity("A", "V1.0", "TÁC PHONG – PPE – HÀNH VI AN TOÀN", 1, 1),
        ChecklistCategoryEntity("B", "V1.0", "MÁY MÓC – THIẾT BỊ – KHU VỰC LÀM VIỆC", 2, 1),
        ChecklistCategoryEntity("C", "V1.0", "ĐIỆN – CƠ – BẢO TRÌ LIÊN QUAN AN TOÀN", 3, 1),
        ChecklistCategoryEntity("D", "V1.0", "BIỂN BÁO – CẢNH BÁO – NHẬN DIỆN KHU VỰC", 4, 1),
        ChecklistCategoryEntity("E", "V1.0", "PHÒNG CHÁY / ỨNG PHÓ KHẨN CẤP CƠ BẢN", 5, 1),
        ChecklistCategoryEntity("F", "V1.0", "VỆ SINH AN TOÀN – HOUSEKEEPING LIÊN QUAN HSE", 6, 1),
        ChecklistCategoryEntity("G", "V1.0", "MÔI TRƯỜNG / BẤT THƯỜNG KHÁC", 7, 1)
    )

    fun getStandardChecklistItems(): List<ChecklistItemEntity> = listOf(
        // Nhóm A (1 - 5)
        ChecklistItemEntity("ITEM_01", "V1.0", "A", 1, "Người lao động sử dụng PPE đúng quy định", 1),
        ChecklistItemEntity("ITEM_02", "V1.0", "A", 2, "Không có hành vi làm việc nguy hiểm rõ ràng", 1),
        ChecklistItemEntity("ITEM_03", "V1.0", "A", 3, "Không có người làm việc sai tư thế / thao tác nguy hiểm", 1),
        ChecklistItemEntity("ITEM_04", "V1.0", "A", 4, "Không có việc đi lại / đứng / ngồi sai khu vực nguy hiểm", 1),
        ChecklistItemEntity("ITEM_05", "V1.0", "A", 5, "Người vận hành máy tuân thủ thao tác an toàn", 1),
        // Nhóm B (6 - 11)
        ChecklistItemEntity("ITEM_06", "V1.0", "B", 6, "Máy móc hoạt động trong tình trạng an toàn nhìn thấy được", 1),
        ChecklistItemEntity("ITEM_07", "V1.0", "B", 7, "Che chắn / bảo vệ máy còn đầy đủ", 1),
        ChecklistItemEntity("ITEM_08", "V1.0", "B", 8, "Không có dấu hiệu hư hỏng nguy hiểm rõ ràng", 1),
        ChecklistItemEntity("ITEM_09", "V1.0", "B", 9, "Khu vực làm việc không có vật cản nguy hiểm", 1),
        ChecklistItemEntity("ITEM_10", "V1.0", "B", 10, "Lối đi / lối thoát thông thoáng", 1),
        ChecklistItemEntity("ITEM_11", "V1.0", "B", 11, "Sàn, khu vực đứng làm việc không trơn trượt / đọng nước / đọng dầu", 1),
        // Nhóm C (12 - 15)
        ChecklistItemEntity("ITEM_12", "V1.0", "C", 12, "Không có dây điện / ổ cắm / thiết bị điện lộ rõ nguy hiểm", 1),
        ChecklistItemEntity("ITEM_13", "V1.0", "C", 13, "Không có dây điện đi tạm bợ qua lối đi", 1),
        ChecklistItemEntity("ITEM_14", "V1.0", "C", 14, "Khu vực sửa chữa / bảo trì được kiểm soát an toàn", 1),
        ChecklistItemEntity("ITEM_15", "V1.0", "C", 15, "Không có thiết bị / vật tư để sai vị trí gây nguy cơ", 1),
        // Nhóm D (16 - 18)
        ChecklistItemEntity("ITEM_16", "V1.0", "D", 16, "Biển báo an toàn còn đủ và dễ nhìn", 1),
        ChecklistItemEntity("ITEM_17", "V1.0", "D", 17, "Khu vực nguy hiểm được nhận diện rõ", 1),
        ChecklistItemEntity("ITEM_18", "V1.0", "D", 18, "Lối đi, khu vực cấm, khu vực nâng hạ được phân định rõ", 1),
        // Nhóm E (19 - 21)
        ChecklistItemEntity("ITEM_19", "V1.0", "E", 19, "Thiết bị PCCC dễ tiếp cận, không bị che khuất", 1),
        ChecklistItemEntity("ITEM_20", "V1.0", "E", 20, "Lối thoát hiểm / tập kết không bị cản trở", 1),
        ChecklistItemEntity("ITEM_21", "V1.0", "E", 21, "Không có dấu hiệu nguy cơ cháy rõ ràng chưa được xử lý", 1),
        // Nhóm F (22 - 24)
        ChecklistItemEntity("ITEM_22", "V1.0", "F", 22, "Khu vực làm việc sạch ở mức an toàn chấp nhận được", 1),
        ChecklistItemEntity("ITEM_23", "V1.0", "F", 23, "Không có vật sắc nhọn / vật nguy hiểm để bừa bãi", 1),
        ChecklistItemEntity("ITEM_24", "V1.0", "F", 24, "Không có rác / nguyên liệu / bán thành phẩm gây cản trở hoặc nguy cơ", 1),
        // Nhóm G (25 - 27)
        ChecklistItemEntity("ITEM_25", "V1.0", "G", 25, "Không có rò rỉ / tràn đổ bất thường", 1),
        ChecklistItemEntity("ITEM_26", "V1.0", "G", 26, "Không có mùi / khói / bụi / tiếng ồn bất thường vượt mức thông thường cần chú ý", 1),
        ChecklistItemEntity("ITEM_27", "V1.0", "G", 27, "Không có tình trạng môi trường bất thường chưa được báo", 1)
    )

    fun getStandardCatalogs(): List<ReferenceCatalogEntity> {
        val now = "2026-11-06T00:00:00Z"
        return listOf(
            // FACTORIES
            ReferenceCatalogEntity("FACTORY", "FAC_01", "Xưởng Dệt A", null, 1, 1, now),
            ReferenceCatalogEntity("FACTORY", "FAC_02", "Xưởng May B", null, 2, 1, now),
            ReferenceCatalogEntity("FACTORY", "FAC_03", "Kho Nguyên Liệu A3", null, 3, 1, now),
            ReferenceCatalogEntity("FACTORY", "FAC_04", "Xưởng Sản Xuất 1", null, 4, 1, now),
            // AREAS (12 phân xưởng / khu vực theo đặc tả)
            ReferenceCatalogEntity("AREA", "AREA_TWIST_04", "Tổ máy xoắn số 04 - Line 1", "FAC_01", 1, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_WEAVE_02", "Dây chuyền Dệt thoi Line 2", "FAC_01", 2, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_DYE_01", "Khu vực Nhuộm & Hoàn tất", "FAC_01", 3, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_SEW_01", "Chuyền May Công nghiệp 01-05", "FAC_02", 4, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_CUT_02", "Tổ Cắt tự động & Trải vải", "FAC_02", 5, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_FINISH_03", "Khu vực Ủi hơi & Đóng thùng", "FAC_02", 6, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_CHEM_A3", "Kho Hóa chất & Phụ gia A3", "FAC_03", 7, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_YARN_A3", "Kho Sợi nguyên liệu & Pallet", "FAC_03", 8, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_LOADING", "Bến xuất nhập hàng & Xe nâng", "FAC_03", 9, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_PACKING_02", "Xưởng Sản Xuất 1 - Máy đóng gói Line 2", "FAC_04", 10, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_MIXING_01", "Khu vực Pha chế trung tâm", "FAC_04", 11, 1, now),
            ReferenceCatalogEntity("AREA", "AREA_BOILER", "Trạm Điện - Khí nén - Lò hơi", "FAC_04", 12, 1, now),
            // SHIFTS
            ReferenceCatalogEntity("SHIFT", "SHIFT_1", "Ca 1 (06:00 - 14:00)", null, 1, 1, now),
            ReferenceCatalogEntity("SHIFT", "SHIFT_2", "Ca 2 (14:00 - 22:00)", null, 2, 1, now),
            ReferenceCatalogEntity("SHIFT", "SHIFT_3", "Ca 3 (22:00 - 06:00)", null, 3, 1, now),
            // UNITS
            ReferenceCatalogEntity("UNIT", "UNIT_HSE", "Bộ phận HSE", null, 1, 1, now),
            ReferenceCatalogEntity("UNIT", "UNIT_MAINT", "Bộ phận Cơ điện & Bảo trì", null, 2, 1, now),
            ReferenceCatalogEntity("UNIT", "UNIT_PROD", "Ban Quản đốc Sản xuất", null, 3, 1, now),
            ReferenceCatalogEntity("UNIT", "UNIT_QC", "Bộ phận QA/QC", null, 4, 1, now)
        )
    }

    fun getDefaultInterviewQuestions(inspectionId: String, now: String): List<EmployeeInterviewEntity> = listOf(
        EmployeeInterviewEntity(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            questionCode = "Q1",
            questionNumber = 1,
            questionText = "1. Bạn biết làm gì khi thấy điều kiện không an toàn?",
            answerText = "Dừng việc ngay lập tức, cảnh báo đồng nghiệp xung quanh và báo cho Tổ trưởng hoặc cán bộ HSE. [Đã được huấn luyện dừng khẩn cấp: CÓ]",
            intervieweeNote = "Công nhân vận hành máy - 4 năm kinh nghiệm",
            syncVersion = 1,
            updatedAt = now
        ),
        EmployeeInterviewEntity(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            questionCode = "Q2",
            questionNumber = 2,
            questionText = "2. Có vấn đề an toàn nào bạn muốn phản ánh?",
            answerText = "Đề xuất cải thiện quạt thông gió tại khu vực máy xoắn số 04 và bổ sung găng tay chống cắt/cách nhiệt ca chiều.",
            intervieweeNote = "Tổ máy xoắn số 04",
            syncVersion = 1,
            updatedAt = now
        ),
        EmployeeInterviewEntity(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            questionCode = "Q3",
            questionNumber = 3,
            questionText = "3. Bạn có đủ PPE và biết cách sử dụng đúng?",
            answerText = "Đã được cấp đủ giày mũi thép, kính bảo hộ, nút tai chống ồn và sử dụng đúng quy chuẩn.",
            intervieweeNote = "Đạt 100% kiểm tra thực tế",
            syncVersion = 1,
            updatedAt = now
        ),
        EmployeeInterviewEntity(
            id = UUID.randomUUID().toString(),
            inspectionId = inspectionId,
            questionCode = "Q4",
            questionNumber = 4,
            questionText = "4. Nội dung khác (Đã được huấn luyện quy trình dừng khẩn cấp LOTO chưa?)",
            answerText = "Đã nắm rõ vị trí nút E-Stop và quy trình khóa LOTO khi bảo trì máy.",
            intervieweeNote = "Xác nhận tại hiện trường",
            syncVersion = 1,
            updatedAt = now
        )
    )
}
