package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BA\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003¢\u0006\u0004\b\f\u0010\rJ\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001b\u001a\u00020\bHÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003JO\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u0003HÆ\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010#\u001a\u00020$HÖ\u0001J\t\u0010%\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000fR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u000f¨\u0006&"}, d2 = {"Lcom/example/model/ActivityLog;", "", "id", "", "operator", "avatarColorHex", "", "action", "Lcom/example/model/LogActionType;", "content", "time", "ip", "<init>", "(Ljava/lang/String;Ljava/lang/String;JLcom/example/model/LogActionType;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "getOperator", "getAvatarColorHex", "()J", "getAction", "()Lcom/example/model/LogActionType;", "getContent", "getTime", "getIp", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ActivityLog {
    public static final int $stable = 0;
    private final LogActionType action;
    private final long avatarColorHex;
    private final String content;
    private final String id;
    private final String ip;
    private final String operator;
    private final String time;

    public static /* synthetic */ ActivityLog copy$default(ActivityLog activityLog, String str, String str2, long j, LogActionType logActionType, String str3, String str4, String str5, int i, Object obj) {
        if ((i & 1) != 0) {
            str = activityLog.id;
        }
        if ((i & 2) != 0) {
            str2 = activityLog.operator;
        }
        if ((i & 4) != 0) {
            j = activityLog.avatarColorHex;
        }
        if ((i & 8) != 0) {
            logActionType = activityLog.action;
        }
        if ((i & 16) != 0) {
            str3 = activityLog.content;
        }
        if ((i & 32) != 0) {
            str4 = activityLog.time;
        }
        if ((i & 64) != 0) {
            str5 = activityLog.ip;
        }
        long j2 = j;
        return activityLog.copy(str, str2, j2, logActionType, str3, str4, str5);
    }

    public final String component1() {
        return this.id;
    }

    public final String component2() {
        return this.operator;
    }

    public final long component3() {
        return this.avatarColorHex;
    }

    public final LogActionType component4() {
        return this.action;
    }

    public final String component5() {
        return this.content;
    }

    public final String component6() {
        return this.time;
    }

    public final String component7() {
        return this.ip;
    }

    public final ActivityLog copy(String id, String operator, long j, LogActionType action, String content, String time, String ip) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(operator, "operator");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(time, "time");
        Intrinsics.checkNotNullParameter(ip, "ip");
        return new ActivityLog(id, operator, j, action, content, time, ip);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ActivityLog) {
            ActivityLog activityLog = (ActivityLog) obj;
            return Intrinsics.areEqual(this.id, activityLog.id) && Intrinsics.areEqual(this.operator, activityLog.operator) && this.avatarColorHex == activityLog.avatarColorHex && this.action == activityLog.action && Intrinsics.areEqual(this.content, activityLog.content) && Intrinsics.areEqual(this.time, activityLog.time) && Intrinsics.areEqual(this.ip, activityLog.ip);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((this.id.hashCode() * 31) + this.operator.hashCode()) * 31) + Long.hashCode(this.avatarColorHex)) * 31) + this.action.hashCode()) * 31) + this.content.hashCode()) * 31) + this.time.hashCode()) * 31) + this.ip.hashCode();
    }

    public String toString() {
        String str = this.id;
        String str2 = this.operator;
        long j = this.avatarColorHex;
        LogActionType logActionType = this.action;
        String str3 = this.content;
        String str4 = this.time;
        return "ActivityLog(id=" + str + ", operator=" + str2 + ", avatarColorHex=" + j + ", action=" + logActionType + ", content=" + str3 + ", time=" + str4 + ", ip=" + this.ip + ")";
    }

    public ActivityLog(String id, String operator, long avatarColorHex, LogActionType action, String content, String time, String ip) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(operator, "operator");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(time, "time");
        Intrinsics.checkNotNullParameter(ip, "ip");
        this.id = id;
        this.operator = operator;
        this.avatarColorHex = avatarColorHex;
        this.action = action;
        this.content = content;
        this.time = time;
        this.ip = ip;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ ActivityLog(java.lang.String r11, java.lang.String r12, long r13, com.example.model.LogActionType r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, int r19, kotlin.jvm.internal.DefaultConstructorMarker r20) {
        /*
            r10 = this;
            r0 = r19 & 64
            if (r0 == 0) goto L8
            java.lang.String r0 = "-"
            r9 = r0
            goto La
        L8:
            r9 = r18
        La:
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r6 = r15
            r7 = r16
            r8 = r17
            r1.<init>(r2, r3, r4, r6, r7, r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.model.ActivityLog.<init>(java.lang.String, java.lang.String, long, com.example.model.LogActionType, java.lang.String, java.lang.String, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getId() {
        return this.id;
    }

    public final String getOperator() {
        return this.operator;
    }

    public final long getAvatarColorHex() {
        return this.avatarColorHex;
    }

    public final LogActionType getAction() {
        return this.action;
    }

    public final String getContent() {
        return this.content;
    }

    public final String getTime() {
        return this.time;
    }

    public final String getIp() {
        return this.ip;
    }
}
