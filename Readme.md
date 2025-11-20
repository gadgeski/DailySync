# DailySync

Android / Kotlin / Jetpack Compose ベースの日報アプリです。  
その日の作業内容や学びを素早くメモして、後から振り返りやすくすることを目的としています。

現時点では、責務ごとにクラスを分割したシンプルな構成（ドメインモデル＋ UseCase ＋ Repository ＋ ViewModel ＋ Compose UI）のサンプル兼ポートフォリオとして位置付けています。

---

## Features (現状の機能)

- 日報の作成・更新
  - 日付ごとに 1 件の日報を保存（同じ日付に対しては上書き）
  - タイトル（必須）と本文を入力
- 日報一覧の表示
  - 保存済みの日報を日付の新しい順にリスト表示
  - 各日報のタイトルと本文の先頭部分をカード形式で表示
- バリデーション
  - タイトルが空の場合は保存時にエラーメッセージを表示

※ 現時点では、データ保存は **インメモリ実装** です（アプリを再起動すると初期化されます）。
→ インメモリ実装から変更予定

---

## アプリ画面

<table>
<tr>
<td align="center">
<img src="docs/img/DailySync-Home.png" alt="ホーム" width="300">
<br>
<sub>ホーム</sub>
</td>
</tr>
</table>

---

## Tech Stack

- **Platform**: Android
- **Language**: Kotlin
- **UI**: Jetpack Compose, Material3
- **Architecture**: MVVM + UseCase + Repository
- **Async**: Kotlin Coroutines, Flow / StateFlow

---

## Architecture

DailySync は、小さなクリーンアーキテクチャ風のレイヤ構成を採用しています。

```text
UI (Compose)
  ↓
ViewModel (DailySyncViewModel)
  ↓
UseCase
  - CreateDailyReportUseCase
  - ObserveDailyReportsUseCase
  ↓
Repository
  - DailyReportRepository (interface)
  - InMemoryDailyReportRepository (implementation)
  ↓
Domain Model
  - DailyReport
```
