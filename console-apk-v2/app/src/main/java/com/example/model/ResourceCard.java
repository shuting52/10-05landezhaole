package com.example.model;

import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b-\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B«\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018¢\u0006\u0004\b\u0019\u0010\u001aJ\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0007HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\nHÆ\u0003J\t\u00108\u001a\u00020\fHÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\u0003HÆ\u0003J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\u0003HÆ\u0003J\t\u0010A\u001a\u00020\u0003HÆ\u0003J\t\u0010B\u001a\u00020\u0003HÆ\u0003J\u000b\u0010C\u001a\u0004\u0018\u00010\u0018HÆ\u0003J¿\u0001\u0010D\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0018HÆ\u0001J\u0013\u0010E\u001a\u00020F2\b\u0010G\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010H\u001a\u00020\nHÖ\u0001J\t\u0010I\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001cR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001cR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001cR\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u001cR\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u001cR\u0011\u0010\u0010\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001cR\u0011\u0010\u0011\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001cR\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001cR\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001cR\u0011\u0010\u0015\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010\u001cR\u0011\u0010\u0016\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001cR\u0013\u0010\u0017\u001a\u0004\u0018\u00010\u0018¢\u0006\b\n\u0000\u001a\u0004\b0\u00101¨\u0006J"}, d2 = {"Lcom/example/model/ResourceCard;", "", "id", "", HintConstants.AUTOFILL_HINT_NAME, "description", "buttonType", "Lcom/example/model/ButtonType;", "size", "downloads", "", NotificationCompat.CATEGORY_STATUS, "Lcom/example/model/CardStatus;", "updatedAt", "category", "url", "icon", "fallbackText", "categoryId", "subcatId", "badge", "badgeType", "highlights", "rawJson", "Lorg/json/JSONObject;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/example/model/ButtonType;Ljava/lang/String;ILcom/example/model/CardStatus;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)V", "getId", "()Ljava/lang/String;", "getName", "getDescription", "getButtonType", "()Lcom/example/model/ButtonType;", "getSize", "getDownloads", "()I", "getStatus", "()Lcom/example/model/CardStatus;", "getUpdatedAt", "getCategory", "getUrl", "getIcon", "getFallbackText", "getCategoryId", "getSubcatId", "getBadge", "getBadgeType", "getHighlights", "getRawJson", "()Lorg/json/JSONObject;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class ResourceCard {
    public static final int $stable = 8;
    private final String badge;
    private final String badgeType;
    private final ButtonType buttonType;
    private final String category;
    private final String categoryId;
    private final String description;
    private final int downloads;
    private final String fallbackText;
    private final String highlights;
    private final String icon;
    private final String id;
    private final String name;
    private final JSONObject rawJson;
    private final String size;
    private final CardStatus status;
    private final String subcatId;
    private final String updatedAt;
    private final String url;

    public static /* synthetic */ ResourceCard copy$default(ResourceCard resourceCard, String str, String str2, String str3, ButtonType buttonType, String str4, int i, CardStatus cardStatus, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, JSONObject jSONObject, int i2, Object obj) {
        JSONObject jSONObject2;
        String str15;
        String str16 = (i2 & 1) != 0 ? resourceCard.id : str;
        String str17 = (i2 & 2) != 0 ? resourceCard.name : str2;
        String str18 = (i2 & 4) != 0 ? resourceCard.description : str3;
        ButtonType buttonType2 = (i2 & 8) != 0 ? resourceCard.buttonType : buttonType;
        String str19 = (i2 & 16) != 0 ? resourceCard.size : str4;
        int i3 = (i2 & 32) != 0 ? resourceCard.downloads : i;
        CardStatus cardStatus2 = (i2 & 64) != 0 ? resourceCard.status : cardStatus;
        String str20 = (i2 & 128) != 0 ? resourceCard.updatedAt : str5;
        String str21 = (i2 & 256) != 0 ? resourceCard.category : str6;
        String str22 = (i2 & 512) != 0 ? resourceCard.url : str7;
        String str23 = (i2 & 1024) != 0 ? resourceCard.icon : str8;
        String str24 = (i2 & 2048) != 0 ? resourceCard.fallbackText : str9;
        String str25 = (i2 & 4096) != 0 ? resourceCard.categoryId : str10;
        String str26 = (i2 & 8192) != 0 ? resourceCard.subcatId : str11;
        String str27 = str16;
        String str28 = (i2 & 16384) != 0 ? resourceCard.badge : str12;
        String str29 = (i2 & 32768) != 0 ? resourceCard.badgeType : str13;
        String str30 = (i2 & 65536) != 0 ? resourceCard.highlights : str14;
        if ((i2 & 131072) != 0) {
            str15 = str30;
            jSONObject2 = resourceCard.rawJson;
        } else {
            jSONObject2 = jSONObject;
            str15 = str30;
        }
        return resourceCard.copy(str27, str17, str18, buttonType2, str19, i3, cardStatus2, str20, str21, str22, str23, str24, str25, str26, str28, str29, str15, jSONObject2);
    }

    public final String component1() {
        return this.id;
    }

    public final String component10() {
        return this.url;
    }

    public final String component11() {
        return this.icon;
    }

    public final String component12() {
        return this.fallbackText;
    }

    public final String component13() {
        return this.categoryId;
    }

    public final String component14() {
        return this.subcatId;
    }

    public final String component15() {
        return this.badge;
    }

    public final String component16() {
        return this.badgeType;
    }

    public final String component17() {
        return this.highlights;
    }

    public final JSONObject component18() {
        return this.rawJson;
    }

    public final String component2() {
        return this.name;
    }

    public final String component3() {
        return this.description;
    }

    public final ButtonType component4() {
        return this.buttonType;
    }

    public final String component5() {
        return this.size;
    }

    public final int component6() {
        return this.downloads;
    }

    public final CardStatus component7() {
        return this.status;
    }

    public final String component8() {
        return this.updatedAt;
    }

    public final String component9() {
        return this.category;
    }

    public final ResourceCard copy(String id, String name, String description, ButtonType buttonType, String size, int i, CardStatus status, String updatedAt, String category, String url, String icon, String fallbackText, String categoryId, String subcatId, String badge, String badgeType, String highlights, JSONObject jSONObject) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        Intrinsics.checkNotNullParameter(buttonType, "buttonType");
        Intrinsics.checkNotNullParameter(size, "size");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(fallbackText, "fallbackText");
        Intrinsics.checkNotNullParameter(categoryId, "categoryId");
        Intrinsics.checkNotNullParameter(subcatId, "subcatId");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(highlights, "highlights");
        return new ResourceCard(id, name, description, buttonType, size, i, status, updatedAt, category, url, icon, fallbackText, categoryId, subcatId, badge, badgeType, highlights, jSONObject);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ResourceCard) {
            ResourceCard resourceCard = (ResourceCard) obj;
            return Intrinsics.areEqual(this.id, resourceCard.id) && Intrinsics.areEqual(this.name, resourceCard.name) && Intrinsics.areEqual(this.description, resourceCard.description) && this.buttonType == resourceCard.buttonType && Intrinsics.areEqual(this.size, resourceCard.size) && this.downloads == resourceCard.downloads && this.status == resourceCard.status && Intrinsics.areEqual(this.updatedAt, resourceCard.updatedAt) && Intrinsics.areEqual(this.category, resourceCard.category) && Intrinsics.areEqual(this.url, resourceCard.url) && Intrinsics.areEqual(this.icon, resourceCard.icon) && Intrinsics.areEqual(this.fallbackText, resourceCard.fallbackText) && Intrinsics.areEqual(this.categoryId, resourceCard.categoryId) && Intrinsics.areEqual(this.subcatId, resourceCard.subcatId) && Intrinsics.areEqual(this.badge, resourceCard.badge) && Intrinsics.areEqual(this.badgeType, resourceCard.badgeType) && Intrinsics.areEqual(this.highlights, resourceCard.highlights) && Intrinsics.areEqual(this.rawJson, resourceCard.rawJson);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + this.description.hashCode()) * 31) + this.buttonType.hashCode()) * 31) + this.size.hashCode()) * 31) + Integer.hashCode(this.downloads)) * 31) + this.status.hashCode()) * 31) + this.updatedAt.hashCode()) * 31) + this.category.hashCode()) * 31) + this.url.hashCode()) * 31) + this.icon.hashCode()) * 31) + this.fallbackText.hashCode()) * 31) + this.categoryId.hashCode()) * 31) + this.subcatId.hashCode()) * 31) + this.badge.hashCode()) * 31) + this.badgeType.hashCode()) * 31) + this.highlights.hashCode()) * 31) + (this.rawJson == null ? 0 : this.rawJson.hashCode());
    }

    public String toString() {
        String str = this.id;
        String str2 = this.name;
        String str3 = this.description;
        ButtonType buttonType = this.buttonType;
        String str4 = this.size;
        int i = this.downloads;
        CardStatus cardStatus = this.status;
        String str5 = this.updatedAt;
        String str6 = this.category;
        String str7 = this.url;
        String str8 = this.icon;
        String str9 = this.fallbackText;
        String str10 = this.categoryId;
        String str11 = this.subcatId;
        String str12 = this.badge;
        String str13 = this.badgeType;
        String str14 = this.highlights;
        return "ResourceCard(id=" + str + ", name=" + str2 + ", description=" + str3 + ", buttonType=" + buttonType + ", size=" + str4 + ", downloads=" + i + ", status=" + cardStatus + ", updatedAt=" + str5 + ", category=" + str6 + ", url=" + str7 + ", icon=" + str8 + ", fallbackText=" + str9 + ", categoryId=" + str10 + ", subcatId=" + str11 + ", badge=" + str12 + ", badgeType=" + str13 + ", highlights=" + str14 + ", rawJson=" + this.rawJson + ")";
    }

    public ResourceCard(String id, String name, String description, ButtonType buttonType, String size, int downloads, CardStatus status, String updatedAt, String category, String url, String icon, String fallbackText, String categoryId, String subcatId, String badge, String badgeType, String highlights, JSONObject rawJson) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(description, "description");
        Intrinsics.checkNotNullParameter(buttonType, "buttonType");
        Intrinsics.checkNotNullParameter(size, "size");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(icon, "icon");
        Intrinsics.checkNotNullParameter(fallbackText, "fallbackText");
        Intrinsics.checkNotNullParameter(categoryId, "categoryId");
        Intrinsics.checkNotNullParameter(subcatId, "subcatId");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(badgeType, "badgeType");
        Intrinsics.checkNotNullParameter(highlights, "highlights");
        this.id = id;
        this.name = name;
        this.description = description;
        this.buttonType = buttonType;
        this.size = size;
        this.downloads = downloads;
        this.status = status;
        this.updatedAt = updatedAt;
        this.category = category;
        this.url = url;
        this.icon = icon;
        this.fallbackText = fallbackText;
        this.categoryId = categoryId;
        this.subcatId = subcatId;
        this.badge = badge;
        this.badgeType = badgeType;
        this.highlights = highlights;
        this.rawJson = rawJson;
    }

    public /* synthetic */ ResourceCard(String str, String str2, String str3, ButtonType buttonType, String str4, int i, CardStatus cardStatus, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, JSONObject jSONObject, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, str3, buttonType, str4, i, cardStatus, str5, str6, (i2 & 512) != 0 ? "" : str7, (i2 & 1024) != 0 ? "" : str8, (i2 & 2048) != 0 ? "" : str9, (i2 & 4096) != 0 ? "" : str10, (i2 & 8192) != 0 ? "all" : str11, (i2 & 16384) != 0 ? "" : str12, (32768 & i2) != 0 ? "" : str13, (65536 & i2) != 0 ? "" : str14, (i2 & 131072) != 0 ? null : jSONObject);
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final String getDescription() {
        return this.description;
    }

    public final ButtonType getButtonType() {
        return this.buttonType;
    }

    public final String getSize() {
        return this.size;
    }

    public final int getDownloads() {
        return this.downloads;
    }

    public final CardStatus getStatus() {
        return this.status;
    }

    public final String getUpdatedAt() {
        return this.updatedAt;
    }

    public final String getCategory() {
        return this.category;
    }

    public final String getUrl() {
        return this.url;
    }

    public final String getIcon() {
        return this.icon;
    }

    public final String getFallbackText() {
        return this.fallbackText;
    }

    public final String getCategoryId() {
        return this.categoryId;
    }

    public final String getSubcatId() {
        return this.subcatId;
    }

    public final String getBadge() {
        return this.badge;
    }

    public final String getBadgeType() {
        return this.badgeType;
    }

    public final String getHighlights() {
        return this.highlights;
    }

    public final JSONObject getRawJson() {
        return this.rawJson;
    }
}
