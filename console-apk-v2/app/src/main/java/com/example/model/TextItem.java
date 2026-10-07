package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003¢\u0006\u0004\b\u000b\u0010\fJ\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001a\u001a\u00020\bHÆ\u0003J\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003JO\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010!\u001a\u00020\bHÖ\u0001J\t\u0010\"\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u000eR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u000e¨\u0006#"}, d2 = {"Lcom/example/model/TextItem;", "", "id", "", "title", "category", "content", "usageCount", "", "updatedAt", "key", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "getTitle", "getCategory", "getContent", "getUsageCount", "()I", "getUpdatedAt", "getKey", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class TextItem {
    public static final int $stable = 0;
    private final String category;
    private final String content;
    private final String id;
    private final String key;
    private final String title;
    private final String updatedAt;
    private final int usageCount;

    public static /* synthetic */ TextItem copy$default(TextItem textItem, String str, String str2, String str3, String str4, int i, String str5, String str6, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = textItem.id;
        }
        if ((i2 & 2) != 0) {
            str2 = textItem.title;
        }
        if ((i2 & 4) != 0) {
            str3 = textItem.category;
        }
        if ((i2 & 8) != 0) {
            str4 = textItem.content;
        }
        if ((i2 & 16) != 0) {
            i = textItem.usageCount;
        }
        if ((i2 & 32) != 0) {
            str5 = textItem.updatedAt;
        }
        if ((i2 & 64) != 0) {
            str6 = textItem.key;
        }
        String str7 = str5;
        String str8 = str6;
        int i3 = i;
        String str9 = str3;
        return textItem.copy(str, str2, str9, str4, i3, str7, str8);
    }

    public final String component1() {
        return this.id;
    }

    public final String component2() {
        return this.title;
    }

    public final String component3() {
        return this.category;
    }

    public final String component4() {
        return this.content;
    }

    public final int component5() {
        return this.usageCount;
    }

    public final String component6() {
        return this.updatedAt;
    }

    public final String component7() {
        return this.key;
    }

    public final TextItem copy(String id, String title, String category, String content, int i, String updatedAt, String key) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(key, "key");
        return new TextItem(id, title, category, content, i, updatedAt, key);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TextItem) {
            TextItem textItem = (TextItem) obj;
            return Intrinsics.areEqual(this.id, textItem.id) && Intrinsics.areEqual(this.title, textItem.title) && Intrinsics.areEqual(this.category, textItem.category) && Intrinsics.areEqual(this.content, textItem.content) && this.usageCount == textItem.usageCount && Intrinsics.areEqual(this.updatedAt, textItem.updatedAt) && Intrinsics.areEqual(this.key, textItem.key);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((this.id.hashCode() * 31) + this.title.hashCode()) * 31) + this.category.hashCode()) * 31) + this.content.hashCode()) * 31) + Integer.hashCode(this.usageCount)) * 31) + this.updatedAt.hashCode()) * 31) + this.key.hashCode();
    }

    public String toString() {
        String str = this.id;
        String str2 = this.title;
        String str3 = this.category;
        String str4 = this.content;
        int i = this.usageCount;
        String str5 = this.updatedAt;
        return "TextItem(id=" + str + ", title=" + str2 + ", category=" + str3 + ", content=" + str4 + ", usageCount=" + i + ", updatedAt=" + str5 + ", key=" + this.key + ")";
    }

    public TextItem(String id, String title, String category, String content, int usageCount, String updatedAt, String key) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(category, "category");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(updatedAt, "updatedAt");
        Intrinsics.checkNotNullParameter(key, "key");
        this.id = id;
        this.title = title;
        this.category = category;
        this.content = content;
        this.usageCount = usageCount;
        this.updatedAt = updatedAt;
        this.key = key;
    }

    public final String getId() {
        return this.id;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getCategory() {
        return this.category;
    }

    public final String getContent() {
        return this.content;
    }

    public final int getUsageCount() {
        return this.usageCount;
    }

    public final String getUpdatedAt() {
        return this.updatedAt;
    }

    public final String getKey() {
        return this.key;
    }
}
