# 下载代码(取自 shuiyingongju)
Flutter + 原生双通道下载器:断点续传/后台保活/进度通知。

## 文件
- `lib/downloader.dart` — Dart 侧下载器(任务管理/进度回调)
- `lib/download_logic.dart` — 下载业务逻辑
- `lib/download_config.dart` — 下载配置(路径/并发等)
- `lib/ui/download_progress_card.dart` — 下载进度卡片 UI
- `android/kotlin/NativeDownloader.kt` — 原生下载器(OkHttp/断点续传)
- `android/kotlin/DownloadKeepAlive.kt` — 后台保活
- `android/kotlin/DownloadNames.kt` — 文件名生成规则

## 说明
- Dart 侧通过 MethodChannel 调原生,包名 `com.videofix.jicun`
- 移植到新项目时注意改包名和方法通道名
