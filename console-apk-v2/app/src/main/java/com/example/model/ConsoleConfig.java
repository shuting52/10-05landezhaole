package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B%\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003¢\u0006\u0004\b\u0007\u0010\bJ\t\u0010\u000e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J'\u0010\u0011\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0015\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0016\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\n¨\u0006\u0017"}, d2 = {"Lcom/example/model/ConsoleConfig;", "", "version", "", "code", "", "apkUrl", "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", "getVersion", "()Ljava/lang/String;", "getCode", "()I", "getApkUrl", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ConsoleConfig {
    public static final int $stable = 0;
    private final String apkUrl;
    private final int code;
    private final String version;

    public ConsoleConfig() {
        this(null, 0, null, 7, null);
    }

    public static /* synthetic */ ConsoleConfig copy$default(ConsoleConfig consoleConfig, String str, int i, String str2, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = consoleConfig.version;
        }
        if ((i2 & 2) != 0) {
            i = consoleConfig.code;
        }
        if ((i2 & 4) != 0) {
            str2 = consoleConfig.apkUrl;
        }
        return consoleConfig.copy(str, i, str2);
    }

    public final String component1() {
        return this.version;
    }

    public final int component2() {
        return this.code;
    }

    public final String component3() {
        return this.apkUrl;
    }

    public final ConsoleConfig copy(String version, int i, String apkUrl) {
        Intrinsics.checkNotNullParameter(version, "version");
        Intrinsics.checkNotNullParameter(apkUrl, "apkUrl");
        return new ConsoleConfig(version, i, apkUrl);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ConsoleConfig) {
            ConsoleConfig consoleConfig = (ConsoleConfig) obj;
            return Intrinsics.areEqual(this.version, consoleConfig.version) && this.code == consoleConfig.code && Intrinsics.areEqual(this.apkUrl, consoleConfig.apkUrl);
        }
        return false;
    }

    public int hashCode() {
        return (((this.version.hashCode() * 31) + Integer.hashCode(this.code)) * 31) + this.apkUrl.hashCode();
    }

    public String toString() {
        String str = this.version;
        int i = this.code;
        return "ConsoleConfig(version=" + str + ", code=" + i + ", apkUrl=" + this.apkUrl + ")";
    }

    public ConsoleConfig(String version, int code, String apkUrl) {
        Intrinsics.checkNotNullParameter(version, "version");
        Intrinsics.checkNotNullParameter(apkUrl, "apkUrl");
        this.version = version;
        this.code = code;
        this.apkUrl = apkUrl;
    }

    public /* synthetic */ ConsoleConfig(String str, int i, String str2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? "1.3.2" : str, (i2 & 2) != 0 ? 56 : i, (i2 & 4) != 0 ? "" : str2);
    }

    public final String getVersion() {
        return this.version;
    }

    public final int getCode() {
        return this.code;
    }

    public final String getApkUrl() {
        return this.apkUrl;
    }
}
