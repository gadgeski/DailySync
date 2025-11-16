```
dailysync/
├── Readme.md
├── STRUCTURE.md
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest
│       │   └── java
│       │       └── com
│       │           └── example
│       │               └── dailysync
│       │                   └── ExampleInstrumentedTest.kt
│       ├── main
│       │   ├── AndroidManifest.xml
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── dailysync
│       │   │               ├── DailySyncApplication.kt
│       │   │               ├── MainActivity.kt
│       │   │               ├── data
│       │   │               │   ├── DailyReportRepository.kt
│       │   │               │   ├── InMemoryDailyReportRepository.kt
│       │   │               │   ├── RoomDailyReportRepository.kt
│       │   │               │   ├── export
│       │   │               │   │   └── MarkdownDailyReportExporter.kt
│       │   │               │   ├── local
│       │   │               │   │   ├── Converters.kt
│       │   │               │   │   ├── DailyReportDao.kt
│       │   │               │   │   ├── DailyReportEntity.kt
│       │   │               │   │   └── DailySyncDatabase.kt
│       │   │               │   ├── mapper
│       │   │               │   │   └── DailyReportMapper.kt
│       │   │               │   └── reminder
│       │   │               │       ├── AndroidReminderScheduler.kt
│       │   │               │       └── DailySyncReminderReceiver.kt
│       │   │               ├── di
│       │   │               │   └── AppModule.kt
│       │   │               ├── domain
│       │   │               │   ├── DailyReport.kt
│       │   │               │   ├── export
│       │   │               │   │   └── DailyReportExporter.kt
│       │   │               │   ├── reminder
│       │   │               │   │   └── ReminderScheduler.kt
│       │   │               │   └── usecase
│       │   │               │       ├── CreateDailyReportUseCase.kt
│       │   │               │       ├── ExportDailyReportsUseCase.kt
│       │   │               │       ├── FilterDailyReportsUseCase.kt
│       │   │               │       ├── ObserveDailyReportsUseCase.kt
│       │   │               │       └── ScheduleDailyReminderUseCase.kt
│       │   │               ├── presentation
│       │   │               │   ├── DailySyncScreen.kt
│       │   │               │   └── DailySyncViewModel.kt
│       │   │               └── ui
│       │   │                   └── theme
│       │   │                       ├── Color.kt
│       │   │                       ├── Theme.kt
│       │   │                       └── Type.kt
│       │   └── res
│       │       ├── drawable
│       │       │   ├── ic_launcher_background.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       ├── mipmap-anydpi
│       │       │   ├── ic_launcher.xml
│       │       │   └── ic_launcher_round.xml
│       │       ├── mipmap-hdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-mdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxxhdpi
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── values
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── dailysync
│                           └── ExampleUnitTest.kt
├── build.gradle.kts
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts

43 directories, 62 files
```
