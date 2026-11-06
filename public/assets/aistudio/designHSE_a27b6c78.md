# TÀI LIỆU HƯỚNG DẪN XÂY DỰNG 12 MÀN HÌNH (SPECIFICATION & IMPLEMENTATION GUIDE)
**Dự án:** ADC Daily HSE Inspection (Internal Android Mobile App)  
**Nền tảng:** Android Native (Jetpack Compose / Material 3)  
**Quy chuẩn áp dụng:** Tiêu chuẩn Kiểm định An toàn ADC #HSE-402 • Xác thực Microsoft Entra ID  
**Hệ thống Thiết kế:** Field Ops Industrial (`DESIGN.md` v2.4)  
**Khổ màn hình quy chuẩn:** Mobile Portrait (390 x 844 dp)

---

## I. TỔNG QUAN HỆ THỐNG THIẾT KẾ (DESIGN SYSTEM TOKENS)

### 1. Bảng màu Chuẩn (Color Palette)
- **Brand Primary (Chủ đạo):** `#0F4C81` (Navy Blue) — Dùng cho Top App Bar, CTA chính, Tab đang chọn.
  - Hover / Dark: `#0A355C`
  - Container / Light: `#EEF5FC`
- **Semantic Pass / Synced (Đạt / Đã đồng bộ):** `#15803D` (Green)
  - Container / Light: `#DCFCE7`
  - Dark: `#166534`
- **Semantic Fail / Hazard (Không đạt / Rủi ro cao):** `#B91C1C` (Red)
  - Container / Light: `#FEE2E2`
  - Dark: `#991B1B`
- **Semantic Warning (Cảnh báo / Chờ Sync):** `#B45309` (Amber)
  - Container / Light: `#FEF3C7`
- **Surfaces & Neutrals (Bề mặt & Nền xám):**
  - Nền toàn màn hình: `#F8FAFC`
  - Bề mặt Card nổi: `#FFFFFF` (Viền 1px `#E2E8F0`)
  - Bề mặt Input / Khung nhạt: `#F1F5F9`
  - Chữ chính (Text Primary): `#0F172A`
  - Chữ phụ (Text Secondary / Caption): `#64748B`

### 2. Typography & Quy chuẩn Tương tác
- **Font gia đình:** Inter (hỗ trợ 100% tiếng Việt chuẩn)
- **Kích thước & Phân cấp:**
  - H1 Display: 24px (Bold 800)
  - H2 Header / Top Bar: 18px (Bold 700)
  - H3 Section Header / Card Title: 16px (SemiBold 600)
  - Body Regular: 14px (Medium 500)
  - Caption & Mã quy chuẩn: 11 - 12px (SemiBold 600)
- **Góc bo (Border Radius):** ROUND_FOUR (`rounded-lg: 8px`, `rounded-xl: 12px`, `rounded-2xl: 16px`).
- **Touch Target:** Chiều cao tối thiểu 48 x 48 dp (Đảm bảo công nhân/cán bộ HSE thao tác chính xác khi đeo găng tay bảo hộ).

---

## II. DANH MỤC & CẤU TRÚC 12 MÀN HÌNH ỨNG DỤNG

| Mã Màn Hình | Tên Màn Hình Chi Tiết | Dạng Shell (Khung điều hướng) | Mục Tiêu / Tính Năng Chính |
|---|---|---|---|
| **SCR-001** | Đăng nhập Microsoft 365 | Fullscreen (Không Top/Bottom Bar) | SSO Entra ID, nhận diện thương hiệu ADC HSE Shield |
| **SCR-002** | Bảng điều khiển Tổng quan (Dashboard) | Mobile Tab (Header + Bottom 5-Tabs) | Thống kê ca, KPI an toàn, phiếu kiểm tra gần đây, lối tắt |
| **SCR-003** | Lịch sử & Tra cứu Phiếu Kiểm tra | Mobile Tab (Header + Bottom 5-Tabs) | Tìm kiếm, bộ lọc thời gian/nhà máy, danh sách phiếu |
| **SCR-004** | Thông tin chung (Bước 1/4) | Mobile Stack (Back Button + Header) | Chọn xưởng, ca, người thực hiện, thời tiết, thiết bị |
| **SCR-005** | Checklist 27 Hạng mục (Bước 2/4) | Mobile Stack (Back Button + Header) | 27 tiêu chí kiểm tra hiện trường, 3 nút [Đạt/Không đạt/K/A] |
| **SCR-006** | Trao đổi với Người lao động (Bước 3/4) | Mobile Stack (Back Button + Header) | Ghi nhận phản ánh công nhân, đính kèm bằng chứng ảnh |
| **SCR-007** | Kết quả kiểm tra & Đánh giá nhanh | Mobile Stack (Back Button + Header) | Tóm tắt tỷ lệ tuân thủ, phân loại rủi ro nhanh, chỉ số CAPA |
| **SCR-008** | Chi tiết Điểm Không Đạt & CAPA | Mobile Stack (Back Button + Header) | Form ghi nhận nguy cơ, gán mức rủi ro, phân công xử lý |
| **SCR-009** | Escalation Bắt buộc & Bài học KN | Mobile Stack (Back Button + Header) | Quy trình leo thang sự cố khẩn cấp, đúc kết bài học ca |
| **SCR-010** | Mục 7. Xác nhận (3 Vai trò Hybrid) | Mobile Stack (Back Button + Header) | Chữ ký số canvas + Chụp ảnh biên bản giấy (3 vai trò) |
| **SCR-011** | Trung tâm Đồng bộ SharePoint & Lịch sử | Mobile Stack (Back Button + Header) | Quản lý hàng đợi offline, trigger đồng bộ cloud tức thời |
| **SCR-012** | Cài đặt & Quản trị Danh mục Hệ thống | Mobile Tab (Header + Bottom 5-Tabs) | Cấu hình cache, đồng bộ offline, thông tin phiên bản app |

---

## III. HƯỚNG DẪN THI CÔNG CHI TIẾT TỪNG MÀN HÌNH

### 1. SCR-001: Đăng nhập Microsoft 365 (Login Screen)
- **Mục đích:** Xác thực người dùng qua SSO doanh nghiệp Microsoft 365 / Entra ID.
- **Cấu trúc giao diện:**
  - **Khung ngoài:** Màn hình độc lập, không có Top Navigation Bar và không có Bottom Navigation Bar.
  - **Logo & Thương hiệu:** Biểu tượng khiên bảo hộ công nghiệp **ADC HSE Industrial Shield** (màu xanh Navy `#0F4C81`, dấu thập bảo hộ xanh lục `#15803D`).
  - **Tiêu đề:** `ADC Daily HSE Inspection` (H1 24px Bold), phụ đề: `Hệ thống Kiểm định An toàn Hiện trường`.
  - **Thẻ đăng nhập SSO:**
    - Khung Card trắng nổi bo góc 16px.
    - Nút CTA đăng nhập chuẩn Microsoft: `Đăng nhập bằng Microsoft 365` (Icon Windows 4 màu, chiều cao 48dp).
    - Lựa chọn ghi nhớ phiên làm việc trên thiết bị chuyên dụng của nhà máy.
  - **Chân trang:** Nhãn bản quyền `© 2026 ADC Industrial Operations • Bảo mật Entra ID`.

---

### 2. SCR-002: Bảng điều khiển Tổng quan (Home Dashboard)
- **Mục đích:** Trung tâm chỉ huy đầu ca cho cán bộ HSE, theo dõi trạng thái đồng bộ và tạo phiếu mới.
- **Cấu trúc giao diện:**
  - **Top App Bar (Tab Header):**
    - Logo biểu trưng ADC Shield + Chữ `ADC Trang Chủ`.
    - Thẻ chào hỏi HSE Auditor: `Nguyễn Văn A (Mã NV: ADC-8842)`.
    - Badge trạng thái đồng bộ Cloud: `⟳ 2 Chờ Sync` (màu hổ phách `#FEF3C7` / `#B45309`).
  - **Khối Hành Động Chính (Primary Action Card):**
    - Nút lớn nổi bật (Navy CTA): `+ Bắt đầu Kiểm tra Mới` (kích hoạt Wizard SCR-004).
  - **Thống kê nhanh trong ngày (Metrics Grid):**
    - 3 thẻ đo lường: `Tổng phiếu đã lập (12)`, `Tỷ lệ Đạt (96.5%)`, `Điểm Không Đạt cần xử lý (03)`.
  - **Danh sách phiếu kiểm tra gần đây (Recent Cards):**
    - Thẻ phiếu kiểm tra Xưởng Dệt (Xanh lá - Đã đồng bộ).
    - Thẻ phiếu Kho Nguyên Liệu A3 (Màu hổ phách - Chờ Sync).
  - **Bottom Navigation Bar:** Tab `Trang chủ` ở trạng thái Active.

---

### 3. SCR-003: Lịch sử & Tra cứu Phiếu Kiểm tra (History & Search)
- **Mục đích:** Tìm kiếm, tra cứu và kiểm tra lại toàn bộ các biên bản kiểm tra trong quá khứ.
- **Cấu trúc giao diện:**
  - **Top Bar:** Tiêu đề `Lịch sử Kiểm tra HSE`.
  - **Bộ lọc & Tìm kiếm:**
    - Thanh Input tìm kiếm theo mã phiếu `#HSE-2026-xxx` hoặc tên xưởng.
    - Thanh lọc nhanh (Filter Chips): `Tất cả`, `Đã đồng bộ`, `Chờ Sync`, `Có điểm vi phạm`.
  - **Danh sách biên bản (Card List):**
    - Mỗi thẻ chứa: Mã phiếu, ngày giờ kiểm tra, khu vực xưởng, tên cán bộ kiểm định.
    - Huy hiệu kết quả: Badge xanh `26 Đạt`, badge đỏ `01 K.Đạt`.
    - Nút xem chi tiết và xuất biên bản PDF.
  - **Bottom Navigation Bar:** Tab `Lịch sử` ở trạng thái Active.

---

### 4. SCR-004: Thông tin chung (Wizard Bước 1/4)
- **Mục đích:** Khởi tạo thông tin cơ bản cho ca kiểm định hiện trường trước khi vào checklist.
- **Cấu trúc giao diện:**
  - **Top App Bar (Stack Header):** Nút Back `←`, tiêu đề `Thiết lập Thông tin chung`, phụ đề `Quy chuẩn An toàn ADC #HSE-402`, huy hiệu tiến trình `Bước 1/4`.
  - **Form nhập liệu:**
    - Trường 1: `Người thực hiện` (Auto-fill `Nguyễn Văn A - ADC-8842` từ Entra ID).
    - Trường 2: Dropdown chọn `Khu vực / Nhà máy` (Xưởng Dệt A, Xưởng May B, Kho Nguyên Liệu).
    - Trường 3: Dropdown chọn `Ca làm việc` (Ca 1: 06:00 - 14:00, Ca 2: 14:00 - 22:00, Ca 3).
    - Trường 4: Ngày & giờ tự động theo thời gian thực của thiết bị.
    - Trường 5: Tình trạng máy móc & điều kiện môi trường (Nhiệt độ, độ ồn, chiếu sáng).
  - **Bottom CTA:** Nút cố định đáy `Tiếp tục: Checklist 27 Hạng mục →` (48dp).

---

### 5. SCR-005: Checklist 27 Hạng mục (Wizard Bước 2/4)
- **Mục đích:** Màn hình cốt lõi nhất để kiểm định an toàn tại các tổ máy.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Nút Back `←`, tiêu đề `Checklist Kiểm tra Hiện trường`, phụ đề `Quy chuẩn An toàn ADC #HSE-402`, huy hiệu `Bước 2/4`.
  - **Tiêu đề nhóm hạng mục:** `B. MÁY MÓC – THIẾT BỊ – KHU VỰC LÀM VIỆC`.
  - **Checklist Item Cards (Thẻ hạng mục):**
    - *Mục #08 (Đạt chuẩn):* Thẻ có viền xanh nhạt, badge `Đạt chuẩn`, nút `[Đạt]` được chọn (nền xanh lá).
    - *Mục #09 (Không đạt - Rủi ro cao):* Thẻ viền đỏ, badge đỏ `★ Rủi ro Cao`, nút `[Không đạt]` được chọn (nền đỏ), hiển thị trích dẫn vi phạm: *"Phát hiện rò rỉ dầu thủy lực..."*, nút `Chi tiết CAPA →` và đính kèm 2 thumbnail ảnh.
    - *Mục #10 (Chưa chọn):* 3 nút segmented control dạng Outline: `[Đạt]` | `[Không đạt]` | `[K/A]`.
  - **Thanh tiến độ đáy:** Hiển thị `18/27 hạng mục đã kiểm tra`, nút chuyển bước `Chuyển Bước 3: Trao đổi NLĐ →`.

---

### 6. SCR-006: Trao đổi với Người lao động (Wizard Bước 3/4)
- **Mục đích:** Ghi nhận ý kiến trực tiếp từ công nhân vận hành máy về các mối nguy tiềm ẩn.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Nút Back `←`, tiêu đề `Trao đổi với Người lao động`, `Bước 3/4`.
  - **Khối nhập liệu phỏng vấn:**
    - Họ tên công nhân vận hành, vị trí máy, số năm kinh nghiệm.
    - Câu hỏi trắc nghiệm an toàn: *"Đã được huấn luyện quy trình dừng khẩn cấp chưa?"* (`[Có]` / `[Chưa]`).
    - Ô ghi chú ý kiến đóng góp: Nhập các đề xuất cải thiện thông gió, cấp phát bổ sung găng tay cách nhiệt.
  - **Khối bằng chứng ảnh:** Nút `+ Chụp ảnh hiện trường phỏng vấn`.
  - **Bottom CTA:** Nút `Tiếp tục: Đánh giá & CAPA →`.

---

### 7. SCR-007: Kết quả kiểm tra & Đánh giá nhanh (Summary & Fast Assessment)
- **Mục đích:** Tổng hợp điểm số, tỷ lệ tuân thủ toàn bộ 27 hạng mục và đưa ra kết luận ca.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Mục 3: Đánh giá & Phân tích CAPA`, `Quy chuẩn An toàn ADC #HSE-402`.
  - **Thẻ điểm số trực quan (Score Card):**
    - Tỷ lệ tuân thủ: `96.3% - Đạt tiêu chuẩn ca`.
    - Bộ đếm: `26 Đạt` (Xanh), `01 Không đạt` (Đỏ), `00 K/A` (Xám).
  - **Danh sách cảnh báo vi phạm cần khắc phục tức thì (Immediate CAPA):**
    - Liệt kê lỗi rò rỉ dầu ở Tổ máy xoắn số 04 kèm thời hạn khắc phục trước 10:30.
  - **Bottom CTA:** Nút chính `Xem chi tiết Điểm Không Đạt` hoặc `Chuyển sang Bước Ký Duyệt →`.

---

### 8. SCR-008: Chi tiết Điểm Không Đạt / Rủi ro phát hiện (Non-Conformance Detail)
- **Mục đích:** Thiết lập phiếu ghi nhận sự cố chi tiết và đề xuất phương án CAPA.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Chi tiết Điểm Không Đạt`, phụ đề `Phiếu Phân tích Nguy cơ & Khắc phục`.
  - **Form phân tích nguy cơ:**
    - Vị trí cụ thể: `Cụm cấp dầu bôi trơn - Tổ máy xoắn số 04`.
    - Phân cấp mức độ rủi ro (Risk Matrix Pill): `★ Mức 3: Rủi ro Cao (Dừng máy xử lý)`.
    - Mô tả hành động khắc phục tức thời (Immediate Action).
    - Phân công người chịu trách nhiệm và deadline hoàn thành.
  - **Bằng chứng ảnh thực địa:** Hiển thị 2 ảnh chụp vị trí dầu tràn kèm góc máy có thước đo an toàn.
  - **Bottom Action Bar:** Nút phụ `Hủy bỏ`, Nút chính màu Navy `Lưu Điểm Không Đạt (Rủi ro #01)`.

---

### 9. SCR-009: Escalation Bắt buộc & Bài học kinh nghiệm (Escalation & Lessons Learned)
- **Mục đích:** Kích hoạt thông báo khẩn cấp lên cấp quản lý nếu có nguy cơ an toàn nghiêm trọng.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Mục 5: Leo thang Sự cố & Đúc kết`, `Quy chuẩn An toàn ADC #HSE-402`.
  - **Khối kích hoạt Escalation:**
    - Cảnh báo màu đỏ nổi bật: Gửi thông báo khẩn qua Teams/SMS tới Giám đốc Nhà máy và Quản đốc xưởng.
    - Checkbox xác nhận: *"Đã yêu cầu tạm dừng vận hành thiết bị cho đến khi có xác nhận an toàn"*.
  - **Khối đúc kết bài học ca (Lessons Learned):**
    - Nội dung khuyến cáo gửi vào buổi họp giao ban đầu giờ của ca tiếp theo.
  - **Bottom CTA:** Nút xác nhận chuyển sang bước ký biên bản.

---

### 10. SCR-010: Mục 7. Xác nhận (3 Vai trò Hybrid Confirmation)
- **Mục đích:** Thu thập chữ ký của 3 bên liên quan bằng phương thức kết hợp (Ký trên màn hình hoặc Biên bản giấy).
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Mục 7: Xác nhận Phiếu Kiểm tra`, `Quy chuẩn An toàn ADC #HSE-402`.
  - **3 Khối Ký Duyệt Phân Cấp:**
    1. **Người thực hiện kiểm tra (HSE Auditor):** Đã ký điện tử kèm timestamp chính xác từ Microsoft Entra ID.
    2. **Người phụ trách khu vực (Area Owner):** Khung Canvas vẽ tay chữ ký cảm ứng trực tiếp trên màn hình, nút `Ký lại`.
    3. **Quản đốc xưởng (Plant Lead):** Chế độ Hybrid — Cho phép ký trực tiếp trên màn hình HOẶC bấm `Chụp biên bản giấy thay thế` (đính kèm ảnh biên bản đã ký tay).
  - **Bottom CTA:** Nút cố định màu Navy `Hoàn tất & Lưu Phiếu Kiểm Tra`.

---

### 11. SCR-011: Trung tâm Đồng bộ SharePoint & Lịch sử (SharePoint Sync Center)
- **Mục đích:** Theo dõi tiến trình tải dữ liệu từ bộ nhớ đệm nội bộ lên danh sách SharePoint `tbl_2026_11`.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Trung tâm Đồng bộ SharePoint`, phụ đề `Hệ thống Quản lý Dữ liệu HSE`.
  - **Trạng thái kết nối mạng:** Badge xanh `Online - Đã kết nối Wi-Fi Nhà máy ADC-Industrial`.
  - **Hàng đợi đồng bộ (Sync Queue):**
    - Thẻ phiếu kiểm tra đang chờ: Có nút tròn quay tiến độ và nút bấm `Đồng bộ ngay`.
    - Thẻ phiếu kiểm tra đã tải lên thành công: Badge `✓ ĐÃ ĐỒNG BỘ (Đã ghi vào tbl_2026_11)`.
  - **Tùy chọn tải ảnh:** Checkbox *"Chỉ đồng bộ hình ảnh độ phân giải cao khi có Wi-Fi"*.
  - **Bottom CTA:** Nút chính `Đồng bộ tất cả dữ liệu (Sync All)`.

---

### 12. SCR-012: Cài đặt & Quản trị Danh mục Hệ thống (Settings & System Master Data)
- **Mục đích:** Cấu hình tài khoản, bộ nhớ đệm ngoại tuyến và thông tin phiên bản phần mềm.
- **Cấu trúc giao diện:**
  - **Top App Bar:** Tiêu đề `Cài đặt & Quản trị Hệ thống`.
  - **Thông tin tài khoản đăng nhập:** Card hiển thị avatar, tên `Nguyễn Văn A`, email công vụ `@adchealthsafety.com`, mã vai trò `Lead HSE Auditor`.
  - **Cấu hình dữ liệu ngoại tuyến (Offline & Cache):**
    - Trạng thái danh mục tải sẵn: `27 câu hỏi checklist v2.4`, `Danh sách 12 phân xưởng`.
    - Nút bấm: `Xóa bộ nhớ đệm (Clear Cache)`, `Cập nhật danh mục mới nhất từ Cloud`.
  - **Thông tin kỹ thuật:** `Phiên bản 2.4.0 (Build 102) • Bản quyền Phòng HCNS & IT ADC 2026`.
  - **Nút hành động cuối:** Nút viền đỏ `Đăng xuất khỏi tài khoản Microsoft`.
  - **Bottom Navigation Bar:** Tab `Cài đặt` ở trạng thái Active.

---

## IV. QUY TRÌNH KIỂM THỬ GIAO DIỆN (UI/UX AUDIT CHECKLIST)

1. **Chuẩn hóa chữ ký số:** Mọi thao tác ký duyệt tại `SCR-010` phải lưu cả chữ ký vector/bitmap và dấu thời gian ISO 8601.
2. **Khả năng thao tác một tay (One-thumb Zone):** Mọi nút bấm chuyển bước, xác nhận kiểm tra ở các màn hình từ `SCR-004` đến `SCR-010` đều phải đặt ở chân trang với chiều cao tối thiểu 48dp.
3. **Màu sắc ngữ nghĩa:** Tuyệt đối không dùng màu đỏ cho các trạng thái chờ đồng bộ dữ liệu (Chờ Sync bắt buộc dùng màu vàng hổ phách `#FEF3C7` / `#B45309`).
4. **Không phát sinh lỗi placeholder:** Đảm bảo toàn bộ logo, biểu tượng khiên bảo hộ đều sử dụng SVG vector đồng bộ theo Design System.
