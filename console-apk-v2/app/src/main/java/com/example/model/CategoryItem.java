package com.example.model;

import androidx.autofill.HintConstants;
import androidx.core.app.NotificationCompat;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BU\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f¢\u0006\u0004\b\u000e\u0010\u000fJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\r0\fHÆ\u0003J_\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\u000e\b\u0002\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fHÆ\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010'\u001a\u00020\u0006HÖ\u0001J\t\u0010(\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a¨\u0006)"}, d2 = {"Lcom/example/model/CategoryItem;", "", "id", "", HintConstants.AUTOFILL_HINT_NAME, "cardCount", "", "order", NotificationCompat.CATEGORY_STATUS, "desc", "iconKey", "subcategories", "", "Lcom/example/model/SubCategoryItem;", "<init>", "(Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", "getId", "()Ljava/lang/String;", "getName", "getCardCount", "()I", "getOrder", "getStatus", "getDesc", "getIconKey", "getSubcategories", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class CategoryItem {
    public static final int $stable = 8;
    private final int cardCount;
    private final String desc;
    private final String iconKey;
    private final String id;
    private final String name;
    private final int order;
    private final String status;
    private final List<SubCategoryItem> subcategories;

    public static /* synthetic */ CategoryItem copy$default(CategoryItem categoryItem, String str, String str2, int i, int i2, String str3, String str4, String str5, List list, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = categoryItem.id;
        }
        if ((i3 & 2) != 0) {
            str2 = categoryItem.name;
        }
        if ((i3 & 4) != 0) {
            i = categoryItem.cardCount;
        }
        if ((i3 & 8) != 0) {
            i2 = categoryItem.order;
        }
        if ((i3 & 16) != 0) {
            str3 = categoryItem.status;
        }
        if ((i3 & 32) != 0) {
            str4 = categoryItem.desc;
        }
        if ((i3 & 64) != 0) {
            str5 = categoryItem.iconKey;
        }
        List<SubCategoryItem> list2 = list;
        if ((i3 & 128) != 0) {
            list2 = categoryItem.subcategories;
        }
        String str6 = str5;
        List list3 = list2;
        String str7 = str3;
        String str8 = str4;
        return categoryItem.copy(str, str2, i, i2, str7, str8, str6, list3);
    }

    public final String component1() {
        return this.id;
    }

    public final String component2() {
        return this.name;
    }

    public final int component3() {
        return this.cardCount;
    }

    public final int component4() {
        return this.order;
    }

    public final String component5() {
        return this.status;
    }

    public final String component6() {
        return this.desc;
    }

    public final String component7() {
        return this.iconKey;
    }

    public final List<SubCategoryItem> component8() {
        return this.subcategories;
    }

    public final CategoryItem copy(String id, String name, int i, int i2, String status, String desc, String iconKey, List<SubCategoryItem> subcategories) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(iconKey, "iconKey");
        Intrinsics.checkNotNullParameter(subcategories, "subcategories");
        return new CategoryItem(id, name, i, i2, status, desc, iconKey, subcategories);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CategoryItem) {
            CategoryItem categoryItem = (CategoryItem) obj;
            return Intrinsics.areEqual(this.id, categoryItem.id) && Intrinsics.areEqual(this.name, categoryItem.name) && this.cardCount == categoryItem.cardCount && this.order == categoryItem.order && Intrinsics.areEqual(this.status, categoryItem.status) && Intrinsics.areEqual(this.desc, categoryItem.desc) && Intrinsics.areEqual(this.iconKey, categoryItem.iconKey) && Intrinsics.areEqual(this.subcategories, categoryItem.subcategories);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((this.id.hashCode() * 31) + this.name.hashCode()) * 31) + Integer.hashCode(this.cardCount)) * 31) + Integer.hashCode(this.order)) * 31) + this.status.hashCode()) * 31) + this.desc.hashCode()) * 31) + this.iconKey.hashCode()) * 31) + this.subcategories.hashCode();
    }

    public String toString() {
        String str = this.id;
        String str2 = this.name;
        int i = this.cardCount;
        int i2 = this.order;
        String str3 = this.status;
        String str4 = this.desc;
        String str5 = this.iconKey;
        return "CategoryItem(id=" + str + ", name=" + str2 + ", cardCount=" + i + ", order=" + i2 + ", status=" + str3 + ", desc=" + str4 + ", iconKey=" + str5 + ", subcategories=" + this.subcategories + ")";
    }

    public CategoryItem(String id, String name, int cardCount, int order, String status, String desc, String iconKey, List<SubCategoryItem> subcategories) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(iconKey, "iconKey");
        Intrinsics.checkNotNullParameter(subcategories, "subcategories");
        this.id = id;
        this.name = name;
        this.cardCount = cardCount;
        this.order = order;
        this.status = status;
        this.desc = desc;
        this.iconKey = iconKey;
        this.subcategories = subcategories;
    }

    public /* synthetic */ CategoryItem(String str, String str2, int i, int i2, String str3, String str4, String str5, List list, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, i, i2, (i3 & 16) != 0 ? "enabled" : str3, (i3 & 32) != 0 ? "" : str4, (i3 & 64) != 0 ? "folder" : str5, (i3 & 128) != 0 ? CollectionsKt.listOf(new SubCategoryItem("all", "全部")) : list);
    }

    public final String getId() {
        return this.id;
    }

    public final String getName() {
        return this.name;
    }

    public final int getCardCount() {
        return this.cardCount;
    }

    public final int getOrder() {
        return this.order;
    }

    public final String getStatus() {
        return this.status;
    }

    public final String getDesc() {
        return this.desc;
    }

    public final String getIconKey() {
        return this.iconKey;
    }

    public final List<SubCategoryItem> getSubcategories() {
        return this.subcategories;
    }
}
