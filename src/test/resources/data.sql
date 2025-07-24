
INSERT INTO students (
  public_id, full_name, kana_name, nickname, email, region, age, sex, remark, is_deleted
) VALUES
-- 全データ含むレコード
('3ab6f73c-3bc1-11f0-b608-6845f1a11345', '田中 太郎', 'タナカ タロウ', 'たろちゃん', 'taro@example.com', '東京都 葛飾区', 30, '男性', 'Javaコース受講検討中', FALSE),
-- 最低限データ + 空データ(ageはIntegerなのでNULL)
('d8a0a2f1-7e5e-4f0f-b123-a1b3d1f82dd2', '佐藤 花子', 'サトウ ハナコ', '', 'hanako@example.com', '', NULL, '', '', FALSE),
-- 最低限データ + NULLデータ
('e5f734ac-7c44-43e4-9aef-2dc1cb246aa1', '井上 翔', 'イノウエ ショウ', NULL, 'sho@example.com', NULL, NULL, NULL, NULL, FALSE),
('c7f92318-bb9e-4a9b-908b-c5a8aaf3f378', '中村 悠', 'ナカムラ ユウ', 'ゆう', 'yuu@example.com', '東京都 港区', 22, '女性', 'Pythonとデータ分析に関心があります。統計や機械学習にも取り組みたいです。', FALSE),
('b77c3c35-d43a-4f9e-bb7a-55871a6c2b7e', '鈴木 太郎', 'スズキ タロウ', 'たかさん', 'suzuki@example.com', '長野県 松本市', 100, 'その他', 'リスキリングとしてJavaを学習中', FALSE),
-- 論理削除=TRUE
('9a30b7b0-5e1b-4ac5-a7de-d2c3c1bfa382', '田中 太郎', 'タナカ タロウ', NULL, 'tanaka2@example.com', NULL, 40, '男性', 'すでに退会済の受講生', TRUE);
--補足：studentId=2は更新処理のテスト対象として使用

INSERT INTO students_courses (
  student_id, course, start_date, end_date
) VALUES
(1, 'Javaコース', '2025-01-10', '2025-07-10'),
(1, 'JavaScriptコース', '2025-04-15', '2025-10-15'),
(2, 'Pythonコース', '2025-08-01', '2026-03-01'),
(5, 'Javaコース', '2025-01-20', '2025-08-20'),
(4, 'Pythonコース', NULL, NULL),
(5, 'AWSコース', '2025-04-01', '2026-02-01'),
(6, 'AWSコース', '2025-01-01', '2026-08-01'),
-- 下記データは対のステータスデータ無し/ステータス登録処理の検証で利用
(4, 'AWSコース', NULL, NULL);
-- 補足:studentId=2は更新処理のテスト対象として使用
-- 補足:studentId=3のみ紐づく登録コース無し

INSERT INTO course_status (
  course_id, status, provisional_application_date, application_date, cancel_date
) VALUES
(1, '受講終了', '2024-12-15', '2024-12-20', NULL),
(2, '受講中', '2025-03-01', '2025-03-15', NULL),
(3, '本申込', '2025-07-10', '2025-07-20', NULL),
(4, 'キャンセル', '2024-12-25', '2025-01-05', '2025-06-30'),
(5, '仮申込', '2025-01-15', NULL, NULL),
(6, '受講中', '2025-03-10', '2025-03-20', NULL),
(7, 'キャンセル', '2024-12-01', NULL, '2024-12-10');
-- 補足:courseId=3は更新処理のテスト対象として使用