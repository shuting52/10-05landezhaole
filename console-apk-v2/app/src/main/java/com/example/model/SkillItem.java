package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b$\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0099\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012¢\u0006\u0004\b\u0013\u0010\u0014J\t\u0010&\u001a\u00020\u0003HÆ\u0003J\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\u000b\u00104\u001a\u0004\u0018\u00010\u0012HÆ\u0003J¡\u0001\u00105\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0012HÆ\u0001J\u0013\u00106\u001a\u0002072\b\u00108\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00109\u001a\u00020:HÖ\u0001J\t\u0010;\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0016R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0016R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0016R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0016R\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0016R\u0011\u0010\u0010\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0016R\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%¨\u0006<"}, d2 = {"Lcom/example/model/SkillItem;", "", "id", "", "type", "promptType", "title", "desc", "prompt", "url", "author", "badge", "tags", "previewUrl", "mediaUrl", "iconUrl", "mode", "rawJson", "Lorg/json/JSONObject;", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)V", "getId", "()Ljava/lang/String;", "getType", "getPromptType", "getTitle", "getDesc", "getPrompt", "getUrl", "getAuthor", "getBadge", "getTags", "getPreviewUrl", "getMediaUrl", "getIconUrl", "getMode", "getRawJson", "()Lorg/json/JSONObject;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class SkillItem {
    public static final int $stable = 8;
    private final String author;
    private final String badge;
    private final String desc;
    private final String iconUrl;
    private final String id;
    private final String mediaUrl;
    private final String mode;
    private final String previewUrl;
    private final String prompt;
    private final String promptType;
    private final JSONObject rawJson;
    private final String tags;
    private final String title;
    private final String type;
    private final String url;

    public final String component1() {
        return this.id;
    }

    public final String component10() {
        return this.tags;
    }

    public final String component11() {
        return this.previewUrl;
    }

    public final String component12() {
        return this.mediaUrl;
    }

    public final String component13() {
        return this.iconUrl;
    }

    public final String component14() {
        return this.mode;
    }

    public final JSONObject component15() {
        return this.rawJson;
    }

    public final String component2() {
        return this.type;
    }

    public final String component3() {
        return this.promptType;
    }

    public final String component4() {
        return this.title;
    }

    public final String component5() {
        return this.desc;
    }

    public final String component6() {
        return this.prompt;
    }

    public final String component7() {
        return this.url;
    }

    public final String component8() {
        return this.author;
    }

    public final String component9() {
        return this.badge;
    }

    public final SkillItem copy(String id, String type, String promptType, String title, String desc, String prompt, String url, String author, String badge, String tags, String previewUrl, String mediaUrl, String iconUrl, String mode, JSONObject jSONObject) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(promptType, "promptType");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(prompt, "prompt");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(mediaUrl, "mediaUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        return new SkillItem(id, type, promptType, title, desc, prompt, url, author, badge, tags, previewUrl, mediaUrl, iconUrl, mode, jSONObject);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SkillItem) {
            SkillItem skillItem = (SkillItem) obj;
            return Intrinsics.areEqual(this.id, skillItem.id) && Intrinsics.areEqual(this.type, skillItem.type) && Intrinsics.areEqual(this.promptType, skillItem.promptType) && Intrinsics.areEqual(this.title, skillItem.title) && Intrinsics.areEqual(this.desc, skillItem.desc) && Intrinsics.areEqual(this.prompt, skillItem.prompt) && Intrinsics.areEqual(this.url, skillItem.url) && Intrinsics.areEqual(this.author, skillItem.author) && Intrinsics.areEqual(this.badge, skillItem.badge) && Intrinsics.areEqual(this.tags, skillItem.tags) && Intrinsics.areEqual(this.previewUrl, skillItem.previewUrl) && Intrinsics.areEqual(this.mediaUrl, skillItem.mediaUrl) && Intrinsics.areEqual(this.iconUrl, skillItem.iconUrl) && Intrinsics.areEqual(this.mode, skillItem.mode) && Intrinsics.areEqual(this.rawJson, skillItem.rawJson);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((this.id.hashCode() * 31) + this.type.hashCode()) * 31) + this.promptType.hashCode()) * 31) + this.title.hashCode()) * 31) + this.desc.hashCode()) * 31) + this.prompt.hashCode()) * 31) + this.url.hashCode()) * 31) + this.author.hashCode()) * 31) + this.badge.hashCode()) * 31) + this.tags.hashCode()) * 31) + this.previewUrl.hashCode()) * 31) + this.mediaUrl.hashCode()) * 31) + this.iconUrl.hashCode()) * 31) + this.mode.hashCode()) * 31) + (this.rawJson == null ? 0 : this.rawJson.hashCode());
    }

    public String toString() {
        String str = this.id;
        String str2 = this.type;
        String str3 = this.promptType;
        String str4 = this.title;
        String str5 = this.desc;
        String str6 = this.prompt;
        String str7 = this.url;
        String str8 = this.author;
        String str9 = this.badge;
        String str10 = this.tags;
        String str11 = this.previewUrl;
        String str12 = this.mediaUrl;
        String str13 = this.iconUrl;
        String str14 = this.mode;
        return "SkillItem(id=" + str + ", type=" + str2 + ", promptType=" + str3 + ", title=" + str4 + ", desc=" + str5 + ", prompt=" + str6 + ", url=" + str7 + ", author=" + str8 + ", badge=" + str9 + ", tags=" + str10 + ", previewUrl=" + str11 + ", mediaUrl=" + str12 + ", iconUrl=" + str13 + ", mode=" + str14 + ", rawJson=" + this.rawJson + ")";
    }

    public SkillItem(String id, String type, String promptType, String title, String desc, String prompt, String url, String author, String badge, String tags, String previewUrl, String mediaUrl, String iconUrl, String mode, JSONObject rawJson) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(promptType, "promptType");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(desc, "desc");
        Intrinsics.checkNotNullParameter(prompt, "prompt");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(badge, "badge");
        Intrinsics.checkNotNullParameter(tags, "tags");
        Intrinsics.checkNotNullParameter(previewUrl, "previewUrl");
        Intrinsics.checkNotNullParameter(mediaUrl, "mediaUrl");
        Intrinsics.checkNotNullParameter(iconUrl, "iconUrl");
        Intrinsics.checkNotNullParameter(mode, "mode");
        this.id = id;
        this.type = type;
        this.promptType = promptType;
        this.title = title;
        this.desc = desc;
        this.prompt = prompt;
        this.url = url;
        this.author = author;
        this.badge = badge;
        this.tags = tags;
        this.previewUrl = previewUrl;
        this.mediaUrl = mediaUrl;
        this.iconUrl = iconUrl;
        this.mode = mode;
        this.rawJson = rawJson;
    }

    public /* synthetic */ SkillItem(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, JSONObject jSONObject, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i & 2) != 0 ? "skill" : str2, (i & 4) != 0 ? "skill" : str3, str4, str5, (i & 32) != 0 ? "" : str6, (i & 64) != 0 ? "" : str7, (i & 128) != 0 ? "懒得找了" : str8, (i & 256) != 0 ? "" : str9, (i & 512) != 0 ? "" : str10, (i & 1024) != 0 ? "" : str11, (i & 2048) != 0 ? "" : str12, (i & 4096) != 0 ? "" : str13, (i & 8192) != 0 ? "file" : str14, (i & 16384) != 0 ? null : jSONObject);
    }

    public final String getId() {
        return this.id;
    }

    public final String getType() {
        return this.type;
    }

    public final String getPromptType() {
        return this.promptType;
    }

    public final String getTitle() {
        return this.title;
    }

    public final String getDesc() {
        return this.desc;
    }

    public final String getPrompt() {
        return this.prompt;
    }

    public final String getUrl() {
        return this.url;
    }

    public final String getAuthor() {
        return this.author;
    }

    public final String getBadge() {
        return this.badge;
    }

    public final String getTags() {
        return this.tags;
    }

    public final String getPreviewUrl() {
        return this.previewUrl;
    }

    public final String getMediaUrl() {
        return this.mediaUrl;
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
