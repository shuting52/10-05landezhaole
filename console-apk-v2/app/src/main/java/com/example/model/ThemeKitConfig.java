package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b(\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u007f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003¢\u0006\u0004\b\u000f\u0010\u0010J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003J\t\u0010%\u001a\u00020\u0003HÆ\u0003J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003J\u0081\u0001\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u0003HÆ\u0001J\u0013\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010.\u001a\u00020/HÖ\u0001J\t\u00100\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0012R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0012¨\u00061"}, d2 = {"Lcom/example/model/ThemeKitConfig;", "", "appBarCss", "", "bottomBarCss", "splashCss", "statusBarCss", "cardCss", "buttonCss", "dialogCss", "searchCss", "globalCss", "settingsPageCss", "customThemeCss", "customThemeHtml", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAppBarCss", "()Ljava/lang/String;", "getBottomBarCss", "getSplashCss", "getStatusBarCss", "getCardCss", "getButtonCss", "getDialogCss", "getSearchCss", "getGlobalCss", "getSettingsPageCss", "getCustomThemeCss", "getCustomThemeHtml", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ThemeKitConfig {
    public static final int $stable = 0;
    private final String appBarCss;
    private final String bottomBarCss;
    private final String buttonCss;
    private final String cardCss;
    private final String customThemeCss;
    private final String customThemeHtml;
    private final String dialogCss;
    private final String globalCss;
    private final String searchCss;
    private final String settingsPageCss;
    private final String splashCss;
    private final String statusBarCss;

    public ThemeKitConfig() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, 4095, null);
    }

    public static /* synthetic */ ThemeKitConfig copy$default(ThemeKitConfig themeKitConfig, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, int i, Object obj) {
        if ((i & 1) != 0) {
            str = themeKitConfig.appBarCss;
        }
        if ((i & 2) != 0) {
            str2 = themeKitConfig.bottomBarCss;
        }
        if ((i & 4) != 0) {
            str3 = themeKitConfig.splashCss;
        }
        if ((i & 8) != 0) {
            str4 = themeKitConfig.statusBarCss;
        }
        if ((i & 16) != 0) {
            str5 = themeKitConfig.cardCss;
        }
        if ((i & 32) != 0) {
            str6 = themeKitConfig.buttonCss;
        }
        if ((i & 64) != 0) {
            str7 = themeKitConfig.dialogCss;
        }
        if ((i & 128) != 0) {
            str8 = themeKitConfig.searchCss;
        }
        if ((i & 256) != 0) {
            str9 = themeKitConfig.globalCss;
        }
        if ((i & 512) != 0) {
            str10 = themeKitConfig.settingsPageCss;
        }
        if ((i & 1024) != 0) {
            str11 = themeKitConfig.customThemeCss;
        }
        if ((i & 2048) != 0) {
            str12 = themeKitConfig.customThemeHtml;
        }
        String str13 = str11;
        String str14 = str12;
        String str15 = str9;
        String str16 = str10;
        String str17 = str7;
        String str18 = str8;
        String str19 = str5;
        String str20 = str6;
        return themeKitConfig.copy(str, str2, str3, str4, str19, str20, str17, str18, str15, str16, str13, str14);
    }

    public final String component1() {
        return this.appBarCss;
    }

    public final String component10() {
        return this.settingsPageCss;
    }

    public final String component11() {
        return this.customThemeCss;
    }

    public final String component12() {
        return this.customThemeHtml;
    }

    public final String component2() {
        return this.bottomBarCss;
    }

    public final String component3() {
        return this.splashCss;
    }

    public final String component4() {
        return this.statusBarCss;
    }

    public final String component5() {
        return this.cardCss;
    }

    public final String component6() {
        return this.buttonCss;
    }

    public final String component7() {
        return this.dialogCss;
    }

    public final String component8() {
        return this.searchCss;
    }

    public final String component9() {
        return this.globalCss;
    }

    public final ThemeKitConfig copy(String appBarCss, String bottomBarCss, String splashCss, String statusBarCss, String cardCss, String buttonCss, String dialogCss, String searchCss, String globalCss, String settingsPageCss, String customThemeCss, String customThemeHtml) {
        Intrinsics.checkNotNullParameter(appBarCss, "appBarCss");
        Intrinsics.checkNotNullParameter(bottomBarCss, "bottomBarCss");
        Intrinsics.checkNotNullParameter(splashCss, "splashCss");
        Intrinsics.checkNotNullParameter(statusBarCss, "statusBarCss");
        Intrinsics.checkNotNullParameter(cardCss, "cardCss");
        Intrinsics.checkNotNullParameter(buttonCss, "buttonCss");
        Intrinsics.checkNotNullParameter(dialogCss, "dialogCss");
        Intrinsics.checkNotNullParameter(searchCss, "searchCss");
        Intrinsics.checkNotNullParameter(globalCss, "globalCss");
        Intrinsics.checkNotNullParameter(settingsPageCss, "settingsPageCss");
        Intrinsics.checkNotNullParameter(customThemeCss, "customThemeCss");
        Intrinsics.checkNotNullParameter(customThemeHtml, "customThemeHtml");
        return new ThemeKitConfig(appBarCss, bottomBarCss, splashCss, statusBarCss, cardCss, buttonCss, dialogCss, searchCss, globalCss, settingsPageCss, customThemeCss, customThemeHtml);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ThemeKitConfig) {
            ThemeKitConfig themeKitConfig = (ThemeKitConfig) obj;
            return Intrinsics.areEqual(this.appBarCss, themeKitConfig.appBarCss) && Intrinsics.areEqual(this.bottomBarCss, themeKitConfig.bottomBarCss) && Intrinsics.areEqual(this.splashCss, themeKitConfig.splashCss) && Intrinsics.areEqual(this.statusBarCss, themeKitConfig.statusBarCss) && Intrinsics.areEqual(this.cardCss, themeKitConfig.cardCss) && Intrinsics.areEqual(this.buttonCss, themeKitConfig.buttonCss) && Intrinsics.areEqual(this.dialogCss, themeKitConfig.dialogCss) && Intrinsics.areEqual(this.searchCss, themeKitConfig.searchCss) && Intrinsics.areEqual(this.globalCss, themeKitConfig.globalCss) && Intrinsics.areEqual(this.settingsPageCss, themeKitConfig.settingsPageCss) && Intrinsics.areEqual(this.customThemeCss, themeKitConfig.customThemeCss) && Intrinsics.areEqual(this.customThemeHtml, themeKitConfig.customThemeHtml);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((this.appBarCss.hashCode() * 31) + this.bottomBarCss.hashCode()) * 31) + this.splashCss.hashCode()) * 31) + this.statusBarCss.hashCode()) * 31) + this.cardCss.hashCode()) * 31) + this.buttonCss.hashCode()) * 31) + this.dialogCss.hashCode()) * 31) + this.searchCss.hashCode()) * 31) + this.globalCss.hashCode()) * 31) + this.settingsPageCss.hashCode()) * 31) + this.customThemeCss.hashCode()) * 31) + this.customThemeHtml.hashCode();
    }

    public String toString() {
        String str = this.appBarCss;
        String str2 = this.bottomBarCss;
        String str3 = this.splashCss;
        String str4 = this.statusBarCss;
        String str5 = this.cardCss;
        String str6 = this.buttonCss;
        String str7 = this.dialogCss;
        String str8 = this.searchCss;
        String str9 = this.globalCss;
        String str10 = this.settingsPageCss;
        String str11 = this.customThemeCss;
        return "ThemeKitConfig(appBarCss=" + str + ", bottomBarCss=" + str2 + ", splashCss=" + str3 + ", statusBarCss=" + str4 + ", cardCss=" + str5 + ", buttonCss=" + str6 + ", dialogCss=" + str7 + ", searchCss=" + str8 + ", globalCss=" + str9 + ", settingsPageCss=" + str10 + ", customThemeCss=" + str11 + ", customThemeHtml=" + this.customThemeHtml + ")";
    }

    public ThemeKitConfig(String appBarCss, String bottomBarCss, String splashCss, String statusBarCss, String cardCss, String buttonCss, String dialogCss, String searchCss, String globalCss, String settingsPageCss, String customThemeCss, String customThemeHtml) {
        Intrinsics.checkNotNullParameter(appBarCss, "appBarCss");
        Intrinsics.checkNotNullParameter(bottomBarCss, "bottomBarCss");
        Intrinsics.checkNotNullParameter(splashCss, "splashCss");
        Intrinsics.checkNotNullParameter(statusBarCss, "statusBarCss");
        Intrinsics.checkNotNullParameter(cardCss, "cardCss");
        Intrinsics.checkNotNullParameter(buttonCss, "buttonCss");
        Intrinsics.checkNotNullParameter(dialogCss, "dialogCss");
        Intrinsics.checkNotNullParameter(searchCss, "searchCss");
        Intrinsics.checkNotNullParameter(globalCss, "globalCss");
        Intrinsics.checkNotNullParameter(settingsPageCss, "settingsPageCss");
        Intrinsics.checkNotNullParameter(customThemeCss, "customThemeCss");
        Intrinsics.checkNotNullParameter(customThemeHtml, "customThemeHtml");
        this.appBarCss = appBarCss;
        this.bottomBarCss = bottomBarCss;
        this.splashCss = splashCss;
        this.statusBarCss = statusBarCss;
        this.cardCss = cardCss;
        this.buttonCss = buttonCss;
        this.dialogCss = dialogCss;
        this.searchCss = searchCss;
        this.globalCss = globalCss;
        this.settingsPageCss = settingsPageCss;
        this.customThemeCss = customThemeCss;
        this.customThemeHtml = customThemeHtml;
    }

    public /* synthetic */ ThemeKitConfig(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? "" : str2, (i & 4) != 0 ? "" : str3, (i & 8) != 0 ? "" : str4, (i & 16) != 0 ? "" : str5, (i & 32) != 0 ? "" : str6, (i & 64) != 0 ? "" : str7, (i & 128) != 0 ? "" : str8, (i & 256) != 0 ? "" : str9, (i & 512) != 0 ? "" : str10, (i & 1024) != 0 ? "" : str11, (i & 2048) != 0 ? "" : str12);
    }

    public final String getAppBarCss() {
        return this.appBarCss;
    }

    public final String getBottomBarCss() {
        return this.bottomBarCss;
    }

    public final String getSplashCss() {
        return this.splashCss;
    }

    public final String getStatusBarCss() {
        return this.statusBarCss;
    }

    public final String getCardCss() {
        return this.cardCss;
    }

    public final String getButtonCss() {
        return this.buttonCss;
    }

    public final String getDialogCss() {
        return this.dialogCss;
    }

    public final String getSearchCss() {
        return this.searchCss;
    }

    public final String getGlobalCss() {
        return this.globalCss;
    }

    public final String getSettingsPageCss() {
        return this.settingsPageCss;
    }

    public final String getCustomThemeCss() {
        return this.customThemeCss;
    }

    public final String getCustomThemeHtml() {
        return this.customThemeHtml;
    }
}
