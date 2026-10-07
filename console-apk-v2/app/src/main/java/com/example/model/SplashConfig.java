package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0015\u001a\u00020\u0007HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J;\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001b\u001a\u00020\u0007HÖ\u0001J\t\u0010\u001c\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\f¨\u0006\u001d"}, d2 = {"Lcom/example/model/SplashConfig;", "", "type", "", "customHtml", "mediaUrl", "durationSeconds", "", "bgColor", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;)V", "getType", "()Ljava/lang/String;", "getCustomHtml", "getMediaUrl", "getDurationSeconds", "()I", "getBgColor", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class SplashConfig {
    public static final int $stable = 0;
    private final String bgColor;
    private final String customHtml;
    private final int durationSeconds;
    private final String mediaUrl;
    private final String type;

    public SplashConfig() {
        this(null, null, null, 0, null, 31, null);
    }

    public static /* synthetic */ SplashConfig copy$default(SplashConfig splashConfig, String str, String str2, String str3, int i, String str4, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = splashConfig.type;
        }
        if ((i2 & 2) != 0) {
            str2 = splashConfig.customHtml;
        }
        if ((i2 & 4) != 0) {
            str3 = splashConfig.mediaUrl;
        }
        if ((i2 & 8) != 0) {
            i = splashConfig.durationSeconds;
        }
        if ((i2 & 16) != 0) {
            str4 = splashConfig.bgColor;
        }
        String str5 = str4;
        String str6 = str3;
        return splashConfig.copy(str, str2, str6, i, str5);
    }

    public final String component1() {
        return this.type;
    }

    public final String component2() {
        return this.customHtml;
    }

    public final String component3() {
        return this.mediaUrl;
    }

    public final int component4() {
        return this.durationSeconds;
    }

    public final String component5() {
        return this.bgColor;
    }

    public final SplashConfig copy(String type, String customHtml, String mediaUrl, int i, String bgColor) {
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(customHtml, "customHtml");
        Intrinsics.checkNotNullParameter(mediaUrl, "mediaUrl");
        Intrinsics.checkNotNullParameter(bgColor, "bgColor");
        return new SplashConfig(type, customHtml, mediaUrl, i, bgColor);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SplashConfig) {
            SplashConfig splashConfig = (SplashConfig) obj;
            return Intrinsics.areEqual(this.type, splashConfig.type) && Intrinsics.areEqual(this.customHtml, splashConfig.customHtml) && Intrinsics.areEqual(this.mediaUrl, splashConfig.mediaUrl) && this.durationSeconds == splashConfig.durationSeconds && Intrinsics.areEqual(this.bgColor, splashConfig.bgColor);
        }
        return false;
    }

    public int hashCode() {
        return (((((((this.type.hashCode() * 31) + this.customHtml.hashCode()) * 31) + this.mediaUrl.hashCode()) * 31) + Integer.hashCode(this.durationSeconds)) * 31) + this.bgColor.hashCode();
    }

    public String toString() {
        String str = this.type;
        String str2 = this.customHtml;
        String str3 = this.mediaUrl;
        int i = this.durationSeconds;
        return "SplashConfig(type=" + str + ", customHtml=" + str2 + ", mediaUrl=" + str3 + ", durationSeconds=" + i + ", bgColor=" + this.bgColor + ")";
    }

    public SplashConfig(String type, String customHtml, String mediaUrl, int durationSeconds, String bgColor) {
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(customHtml, "customHtml");
        Intrinsics.checkNotNullParameter(mediaUrl, "mediaUrl");
        Intrinsics.checkNotNullParameter(bgColor, "bgColor");
        this.type = type;
        this.customHtml = customHtml;
        this.mediaUrl = mediaUrl;
        this.durationSeconds = durationSeconds;
        this.bgColor = bgColor;
    }

    public /* synthetic */ SplashConfig(String str, String str2, String str3, int i, String str4, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? "default" : str, (i2 & 2) != 0 ? "" : str2, (i2 & 4) != 0 ? "" : str3, (i2 & 8) != 0 ? 3 : i, (i2 & 16) != 0 ? "#7F1D1D" : str4);
    }

    public final String getType() {
        return this.type;
    }

    public final String getCustomHtml() {
        return this.customHtml;
    }

    public final String getMediaUrl() {
        return this.mediaUrl;
    }

    public final int getDurationSeconds() {
        return this.durationSeconds;
    }

    public final String getBgColor() {
        return this.bgColor;
    }
}
