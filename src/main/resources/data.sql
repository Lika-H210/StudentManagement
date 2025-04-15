-- students テーブル用データ
INSERT INTO students (full_name, full_name_kana, nickname, mail_address, residence_area, age, sex, remark, is_deleted) VALUES
('田中_太郎', 'タナカ_タロウ', 'タロー', 'tanaka_taro@gmail.com', '東京_葛飾区', 25, 'Male', '', false),
('山田_花子', 'ヤマダ_ハナコ', 'ハナ', 'yamada_hanako@gmail.com', '千葉県_船橋市', 20, 'Female', '', false),
('斎藤_肇', 'サイトウ_ハジメ', 'ハジメ', 'saito_hajime@example.com', '神奈川_横浜市', 35, 'Male', '', false),
('鈴木_一郎', 'スズキ_イチロウ', 'イチロー', 'suzuki_ichi@example.com', '愛知_名古屋市', 28, 'Male', '', true),
('小林_かおり', 'コバヤシ_カオリ', 'カオリ', 'kobayashi_kaori@example.com', '京都_京都市', 32, 'Female', NULL, false);

-- students_courses テーブル用データ
INSERT INTO students_courses (student_id, course, start_date, end_date) VALUES
(1, 'English', '2024-10-01', '2025-05-31'),
(2, 'Japanese', '2024-11-15', '2025-06-14'),
(2, 'French',  '2024-11-05', '2025-07-04'),
(4, 'Java',    '2024-10-20', '2025-03-19'),
(4, 'Python',  '2024-03-04', '2025-04-20'),
(5, 'English', '2024-12-05', '2025-07-04');