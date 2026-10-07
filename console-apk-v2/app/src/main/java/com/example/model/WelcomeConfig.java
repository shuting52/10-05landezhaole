package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001b\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0005HÆ\u0003JO\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u001e\u001a\u00020\u00032\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010 \u001a\u00020!HÖ\u0001J\t\u0010\"\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0010¨\u0006#"}, d2 = {"Lcom/example/model/WelcomeConfig;", "", "enabled", "", "title", "", "welcomeText", "content", "ratio", "imageUrl", "buttonText", "<init>", "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getEnabled", "()Z", "getTitle", "()Ljava/lang/String;", "getWelcomeText", "getContent", "getRatio", "getImageUrl", "getButtonText", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class WelcomeConfig {
    public static final int $stable = 0;
    private final String buttonText;
    private final String content;
    private final boolean enabled;
    private final String imageUrl;
    private final String ratio;
    private final String title;
    private final String welcomeText;

    public WelcomeConfig() {
        this(false, null, null, null, null, null, null, 127, null);
    }

    public static /* synthetic */ WelcomeConfig copy$default(WelcomeConfig welcomeConfig, boolean z, String str, String str2, String str3, String str4, String str5, String str6, int i, Object obj) {
        if ((i & 1) != 0) {
            z = welcomeConfig.enabled;
        }
        if ((i & 2) != 0) {
            str = welcomeConfig.title;
        }
        if ((i & 4) != 0) {
            str2 = welcomeConfig.welcomeText;
        }
        if ((i & 8) != 0) {
            str3 = welcomeConfig.content;
        }
        if ((i & 16) != 0) {
            str4 = welcomeConfig.ratio;
        }
        if ((i & 32) != 0) {
            str5 = welcomeConfig.imageUrl;
        }
        if ((i & 64) != 0) {
            str6 = welcomeConfig.buttonText;
        }
        String str7 = str5;
        String str8 = str6;
        String str9 = str4;
        String str10 = str2;
        return welcomeConfig.copy(z, str, str10, str3, str9, str7, str8);
    }

    public final boolean component1() {
        return this.enabled;
    }

    public final String component2() {
        return this.title;
    }

    public final String component3() {
        return this.welcomeText;
    }

    public final String component4() {
        return this.content;
    }

    public final String component5() {
        return this.ratio;
    }

    public final String component6() {
        return this.imageUrl;
    }

    public final String component7() {
        return this.buttonText;
    }

    public final WelcomeConfig copy(boolean z, String title, String welcomeText, String content, String ratio, String imageUrl, String buttonText) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(welcomeText, "welcomeText");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(ratio, "ratio");
        Intrinsics.checkNotNullParameter(imageUrl, "imageUrl");
        Intrinsics.checkNotNullParameter(buttonText, "buttonText");
        return new WelcomeConfig(z, title, welcomeText, content, ratio, imageUrl, buttonText);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof WelcomeConfig) {
            WelcomeConfig welcomeConfig = (WelcomeConfig) obj;
            return this.enabled == welcomeConfig.enabled && Intrinsics.areEqual(this.title, welcomeConfig.title) && Intrinsics.areEqual(this.welcomeText, welcomeConfig.welcomeText) && Intrinsics.areEqual(this.content, welcomeConfig.content) && Intrinsics.areEqual(this.ratio, welcomeConfig.ratio) && Intrinsics.areEqual(this.imageUrl, welcomeConfig.imageUrl) && Intrinsics.areEqual(this.buttonText, welcomeConfig.buttonText);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((Boolean.hashCode(this.enabled) * 31) + this.title.hashCode()) * 31) + this.welcomeText.hashCode()) * 31) + this.content.hashCode()) * 31) + this.ratio.hashCode()) * 31) + this.imageUrl.hashCode()) * 31) + this.buttonText.hashCode();
    }

    public String toString() {
        boolean z = this.enabled;
        String str = this.title;
        String str2 = this.welcomeText;
        String str3 = this.content;
        String str4 = this.ratio;
        String str5 = this.imageUrl;
        return "WelcomeConfig(enabled=" + z + ", title=" + str + ", welcomeText=" + str2 + ", content=" + str3 + ", ratio=" + str4 + ", imageUrl=" + str5 + ", buttonText=" + this.buttonText + ")";
    }

    public WelcomeConfig(boolean enabled, String title, String welcomeText, String content, String ratio, String imageUrl, String buttonText) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(welcomeText, "welcomeText");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(ratio, "ratio");
        Intrinsics.checkNotNullParameter(imageUrl, "imageUrl");
        Intrinsics.checkNotNullParameter(buttonText, "buttonText");
        this.enabled = enabled;
        this.title = title;
        this.welcomeText = welcomeText;
        this.content = content;
        this.ratio = ratio;
        this.imageUrl = imageUrl;
        this.buttonText = buttonText;
    }

    public /* synthetic */ WelcomeConfig(boolean z, String str, String str2, String str3, String str4, String str5, String str6, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? false : z, (i & 2) != 0 ? "" : str, (i & 4) != 0 ? "" : str2, (i & 8) != 0 ? "" : str3, (i & 16) != 0 ? "compact" : str4, (i & 32) != 0 ? "" : str5, (i & 64) != 0 ? "开始体验" : str6);
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getWelcomeText() {
        return this.welcomeText;
    }

    public final String getContent() {
        return this.content;
    }

    public final String getRatio() {
        return this.ratio;
    }

    public final String getImageUrl() {
        return this.imageUrl;
    }

    public final String getButtonText() {
        return this.buttonText;
    }
}
