package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BW\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e¢\u0006\u0004\b\u000f\u0010\u0010J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\tHÆ\u0003J\t\u0010$\u001a\u00020\u000bHÆ\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u000eHÆ\u0003Jg\u0010'\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000eHÆ\u0001J\u0013\u0010(\u001a\u00020)2\b\u0010*\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010+\u001a\u00020,HÖ\u0001J\t\u0010-\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\f\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012R\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001d¨\u0006."}, d2 = {"Lcom/example/model/StatItem;", "", "id", "", "label", "value", "delta", "deltaLabel", "icon", "Lcom/example/model/StatIcon;", "tone", "Lcom/example/model/StatTone;", "actionLabel", "actionScreen", "Lcom/example/model/AdminScreen;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/example/model/StatIcon;Lcom/example/model/StatTone;Ljava/lang/String;Lcom/example/model/AdminScreen;)V", "getId", "()Ljava/lang/String;", "getLabel", "getValue", "getDelta", "getDeltaLabel", "getIcon", "()Lcom/example/model/StatIcon;", "getTone", "()Lcom/example/model/StatTone;", "getActionLabel", "getActionScreen", "()Lcom/example/model/AdminScreen;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class StatItem {
    public static final int $stable = 0;
    private final String actionLabel;
    private final AdminScreen actionScreen;
    private final String delta;
    private final String deltaLabel;
    private final StatIcon icon;
    private final String id;
    private final String label;
    private final StatTone tone;
    private final String value;

    public static /* synthetic */ StatItem copy$default(StatItem statItem, String str, String str2, String str3, String str4, String str5, StatIcon statIcon, StatTone statTone, String str6, AdminScreen adminScreen, int i, Object obj) {
        if ((i & 1) != 0) {
            str = statItem.id;
        }
        if ((i & 2) != 0) {
            str2 = statItem.label;
        }
        if ((i & 4) != 0) {
            str3 = statItem.value;
        }
        if ((i & 8) != 0) {
            str4 = statItem.delta;
        }
        if ((i & 16) != 0) {
            str5 = statItem.deltaLabel;
        }
        if ((i & 32) != 0) {
            statIcon = statItem.icon;
        }
        if ((i & 64) != 0) {
            statTone = statItem.tone;
        }
        if ((i & 128) != 0) {
            str6 = statItem.actionLabel;
        }
        if ((i & 256) != 0) {
            adminScreen = statItem.actionScreen;
        }
        String str7 = str6;
        AdminScreen adminScreen2 = adminScreen;
        StatIcon statIcon2 = statIcon;
        StatTone statTone2 = statTone;
        String str8 = str5;
        String str9 = str3;
        return statItem.copy(str, str2, str9, str4, str8, statIcon2, statTone2, str7, adminScreen2);
    }

    public final String component1() {
        return this.id;
    }

    public final String component2() {
        return this.label;
    }

    public final String component3() {
        return this.value;
    }

    public final String component4() {
        return this.delta;
    }

    public final String component5() {
        return this.deltaLabel;
    }

    public final StatIcon component6() {
        return this.icon;
    }

    public final StatTone component7() {
        return this.tone;
    }

    public final String component8() {
        return this.actionLabel;
    }

    public final AdminScreen component9() {
        return this.actionScreen;
    }

    public final StatItem copy(String id, String label, String value, String delta, String deltaLabel, StatIcon icon, StatTone tone, String str, AdminScreen adminScreen) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(delta, "delta");
        Intrinsics.checkNotNullParameter(deltaLabel, "deltaLabel");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(tone, "tone");
        return new StatItem(id, label, value, delta, deltaLabel, icon, tone, str, adminScreen);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof StatItem) {
            StatItem statItem = (StatItem) obj;
            return Intrinsics.areEqual(this.id, statItem.id) && Intrinsics.areEqual(this.label, statItem.label) && Intrinsics.areEqual(this.value, statItem.value) && Intrinsics.areEqual(this.delta, statItem.delta) && Intrinsics.areEqual(this.deltaLabel, statItem.deltaLabel) && this.icon == statItem.icon && this.tone == statItem.tone && Intrinsics.areEqual(this.actionLabel, statItem.actionLabel) && this.actionScreen == statItem.actionScreen;
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((this.id.hashCode() * 31) + this.label.hashCode()) * 31) + this.value.hashCode()) * 31) + this.delta.hashCode()) * 31) + this.deltaLabel.hashCode()) * 31) + this.icon.hashCode()) * 31) + this.tone.hashCode()) * 31) + (this.actionLabel == null ? 0 : this.actionLabel.hashCode())) * 31) + (this.actionScreen != null ? this.actionScreen.hashCode() : 0);
    }

    public String toString() {
        String str = this.id;
        String str2 = this.label;
        String str3 = this.value;
        String str4 = this.delta;
        String str5 = this.deltaLabel;
        StatIcon statIcon = this.icon;
        StatTone statTone = this.tone;
        String str6 = this.actionLabel;
        return "StatItem(id=" + str + ", label=" + str2 + ", value=" + str3 + ", delta=" + str4 + ", deltaLabel=" + str5 + ", icon=" + statIcon + ", tone=" + statTone + ", actionLabel=" + str6 + ", actionScreen=" + this.actionScreen + ")";
    }

    public StatItem(String id, String label, String value, String delta, String deltaLabel, StatIcon icon, StatTone tone, String actionLabel, AdminScreen actionScreen) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(delta, "delta");
        Intrinsics.checkNotNullParameter(deltaLabel, "deltaLabel");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(tone, "tone");
        this.id = id;
        this.label = label;
        this.value = value;
        this.delta = delta;
        this.deltaLabel = deltaLabel;
        this.icon = icon;
        this.tone = tone;
        this.actionLabel = actionLabel;
        this.actionScreen = actionScreen;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ StatItem(java.lang.String r2, java.lang.String r3, java.lang.String r4, java.lang.String r5, java.lang.String r6, com.example.model.StatIcon r7, com.example.model.StatTone r8, java.lang.String r9, com.example.model.AdminScreen r10, int r11, kotlin.jvm.internal.DefaultConstructorMarker r12) {
        /*
            r1 = this;
            r12 = r11 & 128(0x80, float:1.794E-43)
            r0 = 0
            if (r12 == 0) goto L6
            r9 = r0
        L6:
            r11 = r11 & 256(0x100, float:3.59E-43)
            if (r11 == 0) goto Lc
            r11 = r0
            goto Ld
        Lc:
            r11 = r10
        Ld:
            r10 = r9
            r9 = r8
            r8 = r7
            r7 = r6
            r6 = r5
            r5 = r4
            r4 = r3
            r3 = r2
            r2 = r1
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.model.StatItem.<init>(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.example.model.StatIcon, com.example.model.StatTone, java.lang.String, com.example.model.AdminScreen, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getId() {
        return this.id;
    }

    public final String getLabel() {
        return this.label;
    }

    public final String getValue() {
        return this.value;
    }

    public final String getDelta() {
        return this.delta;
    }

    public final String getDeltaLabel() {
        return this.deltaLabel;
    }

    public final StatIcon getIcon() {
        return this.icon;
    }

    public final StatTone getTone() {
        return this.tone;
    }

    public final String getActionLabel() {
        return this.actionLabel;
    }

    public final AdminScreen getActionScreen() {
        return this.actionScreen;
    }
}
