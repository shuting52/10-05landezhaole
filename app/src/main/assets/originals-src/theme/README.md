# 主题代码(取自 shuiyingongju)
深浅两档主题系统 + 液态玻璃 UI 组件。

## 文件
- `lib/ui/palette.dart` — 色板:深浅两档所有色号集中定义,`Palette.of(isDark)` 取用
- `lib/ui/glass.dart` — 玻璃拟态组件:GlassPanel/GlassScaffold/SubPage 等
- `lib/ui/theme_appearance_page.dart` — "主题与外观"设置页

## 依赖
- `package:flutter/cupertino.dart` (CupertinoTheme 判定深浅)
- palette.dart 被 glass.dart 引用
