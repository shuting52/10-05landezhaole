package com.example.model;

import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b*\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B§\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016¢\u0006\u0004\b\u0017\u0010\u0018J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0006HÆ\u0003J\t\u00101\u001a\u00020\bHÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\u0003HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0016HÆ\u0003Jµ\u0001\u0010?\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u0016HÆ\u0001J\u0013\u0010@\u001a\u00020A2\b\u0010B\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010C\u001a\u00020\bHÖ\u0001J\t\u0010D\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001aR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001aR\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001aR\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001aR\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001aR\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001aR\u0011\u0010\u0010\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u001aR\u0011\u0010\u0011\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001aR\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001aR\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001aR\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001aR\u0013\u0010\u0015\u001a\u0004\u0018\u00010\u0016¢\u0006\b\n\u0000\u001a\u0004\b,\u0010-¨\u0006E"}, d2 = {"Lcom/example/model/ResourceButton;", "", "id", "", HintConstants.AUTOFILL_HINT_NAME, "type", "Lcom/example/model/ButtonType;", "usageCount", "", NotificationCompat.CATEGORY_STATUS, "updatedAt", "url", "desc", "author", "badge", "badgeType", "tags", "apkUrl", "previewUrl", "iconUrl", "mode", "rawJson", "Lorg/json/JSONObject;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Lcom/example/model/ButtonType;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)V", "getId", "()Ljava/lang/String;", "getName", "getType", "()Lcom/example/model/ButtonType;", "getUsageCount", "()I", "getStatus", "getUpdatedAt", "getUrl", "getDesc", "getAuthor", "getBadge", "getBadgeType", "getTags", "getApkUrl", "getPreviewUrl", "getIconUrl", "getMode", "getRawJson", "()Lorg/json/JSONObject;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ResourceButton {
    public static final int $stable = 8;
    private final String apkUrl;
    private final String author;
    private final String badge;
    private final String badgeType;
    private final String desc;
    private final String iconUrl;
    private final String id;
    private final String mode;
    private final String name;
    private final String previewUrl;
    private final JSONObject rawJson;
    private final String status;
    private final String tags;
    private final ButtonType type;
    private final String updatedAt;
    private final String url;
    private final int usageCount;

    public static /* synthetic */ ResourceButton copy$default(ResourceButton resourceButton, String str, String str2, ButtonType buttonType, int i, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, JSONObject jSONObject, int i2, Object obj) {
        JSONObject jSONObject2;
        String str15;
        String str16;
        ResourceButton resourceButton2;
        String str17;
        String str18;
        ButtonType buttonType2;
        int i3;
        String str19;
        String str20;
        String str21;
        String str22;
        String str23;
        String str24;
        String str25;
        String str26;
        String str27;
        String str28;
        String str29 = (i2 & 1) != 0 ? resourceButton.id : str;
        String str30 = (i2 & 2) != 0 ? resourceButton.name : str2;
        ButtonType buttonType3 = (i2 & 4) != 0 ? resourceButton.type : buttonType;
        int i4 = (i2 & 8) != 0 ? resourceButton.usageCount : i;
        String str31 = (i2 & 16) != 0 ? resourceButton.status : str3;
        String str32 = (i2 & 32) != 0 ? resourceButton.updatedAt : str4;
        String str33 = (i2 & 64) != 0 ? resourceButton.url : str5;
        String str34 = (i2 & 128) != 0 ? resourceButton.desc : str6;
        String str35 = (i2 & 256) != 0 ? resourceButton.author : str7;
        String str36 = (i2 & 512) != 0 ? resourceButton.badge : str8;
        String str37 = (i2 & 1024) != 0 ? resourceButton.badgeType : str9;
        String str38 = (i2 & 2048) != 0 ? resourceButton.tags : str10;
        String str39 = (i2 & 4096) != 0 ? resourceButton.apkUrl : str11;
        String str40 = (i2 & 8192) != 0 ? resourceButton.previewUrl : str12;
        String str41 = str29;
        String str42 = (i2 & 16384) != 0 ? resourceButton.iconUrl : str13;
        String str43 = (i2 & 32768) != 0 ? resourceButton.mode : str14;
        if ((i2 & 65536) != 0) {
            str15 = str43;
            jSONObject2 = resourceButton.rawJson;
            str17 = str42;
            str18 = str30;
            buttonType2 = buttonType3;
            i3 = i4;
            str19 = str31;
            str20 = str32;
            str21 = str33;
            str22 = str34;
            str23 = str35;
            str24 = str36;
            str25 = str37;
            str26 = str38;
            str27 = str39;
            str28 = str40;
            str16 = str41;
            resourceButton2 = resourceButton;
        } else {
            jSONObject2 = jSONObject;
            str15 = str43;
            str16 = str41;
            resourceButton2 = resourceButton;
            str17 = str42;
            str18 = str30;
            buttonType2 = buttonType3;
            i3 = i4;
            str19 = str31;
            str20 = str32;
            str21 = str33;
            str22 = str34;
            str23 = str35;
            str24 = str36;
            str25 = str37;
            str26 = str38;
            str27 = str39;
            str28 = str40;
        }
        return resourceButton2.copy(str16, str18, buttonType2, i3, str19, str20, str21, str22, str23, str24, str25, str26, str27, str28, str17, str15, jSONObject2);
    }

    public final String component1() {
        return this.id;
    }

    public final String component10() {
        return this.badge;
    }

    public final String component11() {
        return this.badgeType;
    }

    public final String component12() {
        return this.tags;
    }

    public final String component13() {
        return this.apkUrl;
    }

    public final String component14() {
        return this.previewUrl;
    }

    public final String component15() {
        return this.iconUrl;
    }

    public final String component16() {
        return this.mode;
    }

    public final JSONObject component17() {
        return this.rawJson;
    }

    public final String component2() {
        return this.name;
    }

    public final ButtonType component3() {
        return this.type;
    }

    public final int component4() {
        return this.usageCount;
    }

    public final String component5() {
        return this.status;
    }

    public final String component6() {
        return this.updatedAt;
    }

    public final String component7() {
        return this.url;
    }

    public final String component8() {
        return this.desc;
    }

    public final String component9() {
        return this.author;
    }

    public final ResourceButton copy(String id, String name, ButtonType type, int i, String status, String updatedAt, String url, String desc, String author, String badge, String badgeType, String tags, String apkUrl, String previewUrl, String iconUrl, String mode, JSONObject jSONObject) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(apkUrl, "apkUrl");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        return new ResourceButton(id, name, type, i, status, updatedAt, url, desc, author, badge, badgeType, tags, apkUrl, previewUrl, iconUrl, mode, jSONObject);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ResourceButton) {
            ResourceButton resourceButton = (ResourceButton) obj;
            return Intrinsics.areEqual(this.id, resourceButton.id) && Intrinsics.areEqual(this.name, resourceButton.name) && this.type == resourceButton.type && this.usageCount == resourceButton.usageCount && Intrinsics.areEqual(this.status, resourceButton.status) && Intrinsics.areEqual(this.updatedAt, resourceButton.updatedAt) && Intrinsics.areEqual(this.url, resourceButton.url) && Intrinsics.areEqual(this.desc, resourceButton.desc) && Intrinsics.areEqual(this.author, resourceButton.author) && Intrinsics.areEqual(this.badge, resourceButton.badge) && Intrinsics.areEqual(this.badgeType, resourceButton.badgeType) && Intrinsics.areEqual(this.tags, resourceButton.tags) && Intrinsics.areEqual(this.apkUrl, resourceButton.apkUrl) && Intrinsics.areEqual(this.previewUrl, resourceButton.previewUrl) && Intrinsics.areEqual(this.iconUrl, resourceButton.iconUrl) && Intrinsics.areEqual(this.mode, resourceButton.mode) && Intrinsics.areEqual(this.rawJson, resourceButton.rawJson);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + this.type.hashCode()) * 31) + Integer.hashCode(this.usageCount)) * 31) + this.status.hashCode()) * 31) + this.updatedAt.hashCode()) * 31) + this.url.hashCode()) * 31) + this.desc.hashCode()) * 31) + this.author.hashCode()) * 31) + this.badge.hashCode()) * 31) + this.badgeType.hashCode()) * 31) + this.tags.hashCode()) * 31) + this.apkUrl.hashCode()) * 31) + this.previewUrl.hashCode()) * 31) + this.iconUrl.hashCode()) * 31) + this.mode.hashCode()) * 31) + (this.rawJson == null ? 0 : this.rawJson.hashCode());
    }

    public String toString() {
        String str = this.id;
        String str2 = this.name;
        ButtonType buttonType = this.type;
        int i = this.usageCount;
        String str3 = this.status;
        String str4 = this.updatedAt;
        String str5 = this.url;
        String str6 = this.desc;
        String str7 = this.author;
        String str8 = this.badge;
        String str9 = this.badgeType;
        String str10 = this.tags;
        String str11 = this.apkUrl;
        String str12 = this.previewUrl;
        String str13 = this.iconUrl;
        String str14 = this.mode;
        return "ResourceButton(id=" + str + ", name=" + str2 + ", type=" + buttonType + ", usageCount=" + i + ", status=" + str3 + ", updatedAt=" + str4 + ", url=" + str5 + ", desc=" + str6 + ", author=" + str7 + ", badge=" + str8 + ", badgeType=" + str9 + ", tags=" + str10 + ", apkUrl=" + str11 + ", previewUrl=" + str12 + ", iconUrl=" + str13 + ", mode=" + str14 + ", rawJson=" + this.rawJson + ")";
    }

    public ResourceButton(String id, String name, ButtonType type, int usageCount, String status, String updatedAt, String url, String desc, String author, String badge, String badgeType, String tags, String apkUrl, String previewUrl, String iconUrl, String mode, JSONObject rawJson) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(apkUrl, "apkUrl");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        this.id = id;
        this.name = name;
        this.type = type;
        this.usageCount = usageCount;
        this.status = status;
        this.updatedAt = updatedAt;
        this.url = url;
        this.desc = desc;
        this.author = author;
        this.badge = badge;
        this.badgeType = badgeType;
        this.tags = tags;
        this.apkUrl = apkUrl;
        this.previewUrl = previewUrl;
        this.iconUrl = iconUrl;
        this.mode = mode;
        this.rawJson = rawJson;
    }

    public /* synthetic */ ResourceButton(String str, String str2, ButtonType buttonType, int i, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, JSONObject jSONObject, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, buttonType, i, str3, str4, (i2 & 64) != 0 ? "" : str5, (i2 & 128) != 0 ? "" : str6, (i2 & 256) != 0 ? "" : str7, (i2 & 512) != 0 ? "" : str8, (i2 & 1024) != 0 ? "" : str9, (i2 & 2048) != 0 ? "" : str10, (i2 & 4096) != 0 ? "" : str11, (i2 & 8192) != 0 ? "" : str12, (i2 & 16384) != 0 ? "" : str13, (32768 & i2) != 0 ? "url" : str14, (i2 & 65536) != 0 ? null : jSONObject);
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final ButtonType getType() {
        return this.type;
    }

    public final int getUsageCount() {
        return this.usageCount;
    }

    public final String getStatus() {
        return this.status;
    }

    public final String getUpdatedAt() {
        return this.updatedAt;
    }

    public final String getUrl() {
        return this.url;
    }

    public final String getDesc() {
        return this.desc;
    }

    public final String getAuthor() {
        return this.author;
    }

    public final String getBadge() {
        return this.badge;
    }

    public final String getBadgeType() {
        return this.badgeType;
    }

    public final String getTags() {
        return this.tags;
    }

    public final String getApkUrl() {
        return this.apkUrl;
    }

    public final String getPreviewUrl() {
        return this.previewUrl;
    }

    public final String getIconUrl() {
        return this.iconUrl;
    }

    public final String getMode() {
        return this.mode;
    }

    public final JSONObject getRawJson() {
        return this.rawJson;
    }
}
