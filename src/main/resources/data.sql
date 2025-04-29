-- students テーブル用データ
INSERT INTO students (full_name, full_name_kana, nickname, mail_address, residence_area, age, sex, remark, is_deleted) VALUES
('田中_太郎', 'タナカ_タロウ', 'タロー', 'tanaka_taro@gmail.com', '東京_葛飾区', 25, 'Male', '', false),
('山田_花子', 'ヤマダ_ハナコ', 'ハナ', 'yamada_hanako@gmail.com', '千葉県_船橋市', 20, 'Female', '', false),
('斎藤_太朗', 'サイトウ_タロウ', 'タロウ', 'saito_tarou@example.com', '神奈川_横浜市', 35, 'Male', '', false),
('鈴木_一郎', 'スズキ_イチロウ', 'イチロー', 'suzuki_ichi@example.com', '京都_京都市', 28, 'Male', '', true),
('小林_かおり', 'コバヤシ_カオリ', 'カオリ', 'kobayashi_kaori@example.com', '東京_立川市', 32, 'Female', NULL, false);

-- students_courses テーブル用データ
INSERT INTO students_courses (student_id, course, start_date, end_date) VALUES
(1, 'English', '2024-10-01', '2025-05-31'),
(2, 'Japanese', '2024-11-15', '2025-06-14'),
(2, 'French',  '2024-11-05', '2025-07-04'),
(4, 'Java',    '2024-10-20', '2025-03-19'),
(4, 'Python',  '2024-03-04', '2025-04-20'),
(5, 'English', '2024-12-05', '2025-04-04'),
 -- 下記は対応するcourse_statusが未登録のdata
(5, 'French',  '2024-12-10', '2025-07-09');

-- courses_status テーブル用データ
INSERT INTO course_status (course_id, status, provisional_application_date, application_date, cancel_date) VALUES
(1, '仮申込', '2024-09-15', NULL, NULL),
(2, '本申込', '2024-10-01', '2024-10-15', NULL),
(3, 'キャンセル', '2024-10-20', NULL, '2024-11-01'),
(4, '受講中', '2024-09-30', '2024-10-10', NULL),
(5, '仮申込', '2024-03-01', NULL, NULL),
(6, '受講完了', '2024-11-20', '2024-12-01', NULL);