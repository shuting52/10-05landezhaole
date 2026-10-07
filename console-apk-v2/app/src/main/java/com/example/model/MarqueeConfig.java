package com.example.model;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B5\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b¢\u0006\u0004\b\t\u0010\nJ\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÆ\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\bHÆ\u0003J7\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\bHÆ\u0001J\u0013\u0010\u0017\u001a\u00020\u00032\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\t\u0010\u001b\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011¨\u0006\u001c"}, d2 = {"Lcom/example/model/MarqueeConfig;", "", "enabled", "", "icon", "", "defaultText", "segments", "", "<init>", "(ZLjava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getEnabled", "()Z", "getIcon", "()Ljava/lang/String;", "getDefaultText", "getSegments", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class MarqueeConfig {
    public static final int $stable = 8;
    private final String defaultText;
    private final boolean enabled;
    private final String icon;
    private final List<String> segments;

    public MarqueeConfig() {
        this(false, null, null, null, 15, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ MarqueeConfig copy$default(MarqueeConfig marqueeConfig, boolean z, String str, String str2, List list, int i, Object obj) {
        if ((i & 1) != 0) {
            z = marqueeConfig.enabled;
        }
        if ((i & 2) != 0) {
            str = marqueeConfig.icon;
        }
        if ((i & 4) != 0) {
            str2 = marqueeConfig.defaultText;
        }
        if ((i & 8) != 0) {
            list = marqueeConfig.segments;
        }
        return marqueeConfig.copy(z, str, str2, list);
    }

    public final boolean component1() {
        return this.enabled;
    }

    public final String component2() {
        return this.icon;
    }

    public final String component3() {
        return this.defaultText;
    }

    public final List<String> component4() {
        return this.segments;
    }

    public final MarqueeConfig copy(boolean z, String icon, String defaultText, List<String> segments) {
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(defaultText, "defaultText");
        Intrinsics.checkNotNullParameter(segments, "segments");
        return new MarqueeConfig(z, icon, defaultText, segments);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MarqueeConfig) {
            MarqueeConfig marqueeConfig = (MarqueeConfig) obj;
            return this.enabled == marqueeConfig.enabled && Intrinsics.areEqual(this.icon, marqueeConfig.icon) && Intrinsics.areEqual(this.defaultText, marqueeConfig.defaultText) && Intrinsics.areEqual(this.segments, marqueeConfig.segments);
        }
        return false;
    }

    public int hashCode() {
        return (((((Boolean.hashCode(this.enabled) * 31) + this.icon.hashCode()) * 31) + this.defaultText.hashCode()) * 31) + this.segments.hashCode();
    }

    public String toString() {
        boolean z = this.enabled;
        String str = this.icon;
        String str2 = this.defaultText;
        return "MarqueeConfig(enabled=" + z + ", icon=" + str + ", defaultText=" + str2 + ", segments=" + this.segments + ")";
    }

    public MarqueeConfig(boolean enabled, String icon, String defaultText, List<String> segments) {
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(defaultText, "defaultText");
        Intrinsics.checkNotNullParameter(segments, "segments");
        this.enabled = enabled;
        this.icon = icon;
        this.defaultText = defaultText;
        this.segments = segments;
    }

    public /* synthetic */ MarqueeConfig(boolean z, String str, String str2, List list, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? true : z, (i & 2) != 0 ? "📪" : str, (i & 4) != 0 ? "" : str2, (i & 8) != 0 ? CollectionsKt.emptyList() : list);
    }

    public final boolean getEnabled() {
        return this.enabled;
    }

    public final String getIcon() {
        return this.icon;
    }

    public final String getDefaultText() {
        return this.defaultText;
    }

    public final List<String> getSegments() {
        return this.segments;
    }
}
