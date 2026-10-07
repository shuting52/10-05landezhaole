package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001b\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00032\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0014"}, d2 = {"Lcom/example/model/IpMonitorConfig;", "", "enabled", "", "url", "", "<init>", "(ZLjava/lang/String;)V", "getEnabled", "()Z", "getUrl", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class IpMonitorConfig {
    public static final int $stable = 0;
    private final boolean enabled;
    private final String url;

    public IpMonitorConfig() {
        this(false, null, 3, null);
    }

    public static /* synthetic */ IpMonitorConfig copy$default(IpMonitorConfig ipMonitorConfig, boolean z, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            z = ipMonitorConfig.enabled;
        }
        if ((i & 2) != 0) {
            str = ipMonitorConfig.url;
        }
        return ipMonitorConfig.copy(z, str);
    }

    public final boolean component1() {
        return this.enabled;
    }

    public final String component2() {
        return this.url;
    }

    public final IpMonitorConfig copy(boolean z, String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        return new IpMonitorConfig(z, url);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof IpMonitorConfig) {
            IpMonitorConfig ipMonitorConfig = (IpMonitorConfig) obj;
            return this.enabled == ipMonitorConfig.enabled && Intrinsics.areEqual(this.url, ipMonitorConfig.url);
        }
        return false;
    }

    public int hashCode() {
        return (Boolean.hashCode(this.enabled) * 31) + this.url.hashCode();
    }

    public String toString() {
        boolean z = this.enabled;
        return "IpMonitorConfig(enabled=" + z + ", url=" + this.url + ")";
    }

    public IpMonitorConfig(boolean enabled, String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        this.enabled = enabled;
        this.url = url;
    }

    public /* synthetic */ IpMonitorConfig(boolean z, String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? false : z, (i & 2) != 0 ? "https://myip.ipip.net/" : str);
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final String getUrl() {
        return this.url;
    }
}
