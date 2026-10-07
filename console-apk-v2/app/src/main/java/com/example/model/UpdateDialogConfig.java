package com.example.model;

import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BI\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003¢\u0006\u0004\b\n\u0010\u000bJ\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J\u000f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\u0003HÆ\u0003JK\u0010\u001a\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u001fHÖ\u0001J\t\u0010 \u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00030\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r¨\u0006!"}, d2 = {"Lcom/example/model/UpdateDialogConfig;", "", "title", "", "changelog", "", "confirmText", "cancelText", "customCss", "customHtml", "<init>", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getTitle", "()Ljava/lang/String;", "getChangelog", "()Ljava/util/List;", "getConfirmText", "getCancelText", "getCustomCss", "getCustomHtml", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class UpdateDialogConfig {
    public static final int $stable = 8;
    private final String cancelText;
    private final List<String> changelog;
    private final String confirmText;
    private final String customCss;
    private final String customHtml;
    private final String title;

    public UpdateDialogConfig() {
        this(null, null, null, null, null, null, 63, null);
    }

    public static /* synthetic */ UpdateDialogConfig copy$default(UpdateDialogConfig updateDialogConfig, String str, List list, String str2, String str3, String str4, String str5, int i, Object obj) {
        if ((i & 1) != 0) {
            str = updateDialogConfig.title;
        }
        List<String> list2 = list;
        if ((i & 2) != 0) {
            list2 = updateDialogConfig.changelog;
        }
        if ((i & 4) != 0) {
            str2 = updateDialogConfig.confirmText;
        }
        if ((i & 8) != 0) {
            str3 = updateDialogConfig.cancelText;
        }
        if ((i & 16) != 0) {
            str4 = updateDialogConfig.customCss;
        }
        if ((i & 32) != 0) {
            str5 = updateDialogConfig.customHtml;
        }
        String str6 = str4;
        String str7 = str5;
        return updateDialogConfig.copy(str, list2, str2, str3, str6, str7);
    }

    public final String component1() {
        return this.title;
    }

    public final List<String> component2() {
        return this.changelog;
    }

    public final String component3() {
        return this.confirmText;
    }

    public final String component4() {
        return this.cancelText;
    }

    public final String component5() {
        return this.customCss;
    }

    public final String component6() {
        return this.customHtml;
    }

    public final UpdateDialogConfig copy(String title, List<String> changelog, String confirmText, String cancelText, String customCss, String customHtml) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(changelog, "changelog");
        Intrinsics.checkNotNullParameter(confirmText, "confirmText");
        Intrinsics.checkNotNullParameter(cancelText, "cancelText");
        Intrinsics.checkNotNullParameter(customCss, "customCss");
        Intrinsics.checkNotNullParameter(customHtml, "customHtml");
        return new UpdateDialogConfig(title, changelog, confirmText, cancelText, customCss, customHtml);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof UpdateDialogConfig) {
            UpdateDialogConfig updateDialogConfig = (UpdateDialogConfig) obj;
            return Intrinsics.areEqual(this.title, updateDialogConfig.title) && Intrinsics.areEqual(this.changelog, updateDialogConfig.changelog) && Intrinsics.areEqual(this.confirmText, updateDialogConfig.confirmText) && Intrinsics.areEqual(this.cancelText, updateDialogConfig.cancelText) && Intrinsics.areEqual(this.customCss, updateDialogConfig.customCss) && Intrinsics.areEqual(this.customHtml, updateDialogConfig.customHtml);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((this.title.hashCode() * 31) + this.changelog.hashCode()) * 31) + this.confirmText.hashCode()) * 31) + this.cancelText.hashCode()) * 31) + this.customCss.hashCode()) * 31) + this.customHtml.hashCode();
    }

    public String toString() {
        String str = this.title;
        List<String> list = this.changelog;
        String str2 = this.confirmText;
        String str3 = this.cancelText;
        String str4 = this.customCss;
        return "UpdateDialogConfig(title=" + str + ", changelog=" + list + ", confirmText=" + str2 + ", cancelText=" + str3 + ", customCss=" + str4 + ", customHtml=" + this.customHtml + ")";
    }

    public UpdateDialogConfig(String title, List<String> changelog, String confirmText, String cancelText, String customCss, String customHtml) {
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(changelog, "changelog");
        Intrinsics.checkNotNullParameter(confirmText, "confirmText");
        Intrinsics.checkNotNullParameter(cancelText, "cancelText");
        Intrinsics.checkNotNullParameter(customCss, "customCss");
        Intrinsics.checkNotNullParameter(customHtml, "customHtml");
        this.title = title;
        this.changelog = changelog;
        this.confirmText = confirmText;
        this.cancelText = cancelText;
        this.customCss = customCss;
        this.customHtml = customHtml;
    }

    public /* synthetic */ UpdateDialogConfig(String str, List list, String str2, String str3, String str4, String str5, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? CollectionsKt.emptyList() : list, (i & 4) != 0 ? "立即更新" : str2, (i & 8) != 0 ? "稍后再说" : str3, (i & 16) != 0 ? "" : str4, (i & 32) != 0 ? "" : str5);
    }

    public final String getTitle() {
        return this.title;
    }

    public final List<String> getChangelog() {
        return this.changelog;
    }

    public final String getConfirmText() {
        return this.confirmText;
    }

    public final String getCancelText() {
        return this.cancelText;
    }

    public final String getCustomCss() {
        return this.customCss;
    }

    public final String getCustomHtml() {
        return this.customHtml;
    }
}
