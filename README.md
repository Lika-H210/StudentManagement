# 受講生管理システム（Student Management System）

## 概要

本システムは、教育機関やスクール向けに、受講生情報および受講コース、申込状況を一元管理するためのWebアプリケーションです。REST API形式で提供され、登録・更新・検索といった操作を通じて、受講生のライフサイクル全体を把握・管理可能です。

## 使用技術・ライブラリ

- Java 17
- Spring Boot
- MyBatis
- H2 / MySQL（開発・本番想定）
- Spring Validation
- OpenAPI / Swagger UI
- Lombok
- Apache Commons Lang
- JUnit + Spring Boot Test

## 主な機能

- 受講生の一覧取得（キャンセル除外）
- 受講生の詳細検索（ID指定）
- 受講生の新規登録（任意でコース情報も同時登録）
- 受講生情報の更新（論理削除含む）
- コース申込状況の登録・更新（バリデーション含む）
- OpenAPIによる自動ドキュメント生成

## テスト

- 単体テスト（H2によるインメモリDBを使用）
- バリデーション異常系・正常系テスト
- JSON構造での戻り値一致確認

## 工夫した点（アピールポイント）

- **ドメインごとにバリデーションをグルーピング**：登録・更新時で異なる制約を柔軟に適用
- **JSONビューを活用**：登録と表示で異なる構造を定義
- **OpenAPI (springdoc-openapi) 導入**：自動ドキュメントでAPI仕様を可視化しやすく
- **論理削除フラグ設計**：isDeletedにより、実データの物理削除を避け運用効率化
- **レイヤード構成**：Controller → Service → Repository で明確に責務を分離

## 起動方法

```bash
# クローン
git clone https://github.com/your-account/student-management-system.git
cd student-management-system

# ビルド＆起動
./gradlew bootRun
