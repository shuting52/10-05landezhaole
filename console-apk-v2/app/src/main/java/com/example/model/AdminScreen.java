package com.example.model;

import kotlin.Metadata;
import kotlin.enums.EnumEntries;
import kotlin.enums.EnumEntriesKt;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0011\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0019\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\bj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\rj\u0002\b\u000ej\u0002\b\u000fj\u0002\b\u0010j\u0002\b\u0011j\u0002\b\u0012j\u0002\b\u0013¨\u0006\u0014"}, d2 = {"Lcom/example/model/AdminScreen;", "", "route", "", "title", "<init>", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getRoute", "()Ljava/lang/String;", "getTitle", "DASHBOARD", "CARDS", "CATEGORIES", "BUTTONS", "SKILLS", "APP_MODULES", "THEME_KIT", "TEXTS", "SETTINGS", "LOGS", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public enum AdminScreen {
    DASHBOARD("dashboard", "总览"),
    CARDS("cards", "卡片管理"),
    CATEGORIES("categories", "分类管理"),
    BUTTONS("buttons", "软件库管理"),
    SKILLS("skills", "Skill 技能库"),
    APP_MODULES("app_modules", "本体弹窗与模块"),
    THEME_KIT("theme_kit", "主题与样式"),
    TEXTS("texts", "文字管理"),
    SETTINGS("settings", "系统设置"),
    LOGS("logs", "操作日志");
    
    private final String route;
    private final String title;
    private static final /* synthetic */ EnumEntries $ENTRIES = EnumEntriesKt.enumEntries($VALUES);

    public static EnumEntries<AdminScreen> getEntries() {
        return $ENTRIES;
    }

    AdminScreen(String route, String title) {
        this.route = route;
        this.title = title;
    }

    public final String getRoute() {
        return this.route;
    }

    public final String getTitle() {
        return this.title;
    }
}
