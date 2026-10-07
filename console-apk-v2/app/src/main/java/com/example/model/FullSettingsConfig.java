package com.example.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AdminModels.kt */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b+\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B§\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003¢\u0006\u0004\b\u0014\u0010\u0015J\t\u0010(\u001a\u00020\u0003HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0010HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0010HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J©\u0001\u00108\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u0003HÆ\u0001J\u0013\u00109\u001a\u00020\u00102\b\u0010:\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010;\u001a\u00020<HÖ\u0001J\t\u0010=\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017R\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0017R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0017R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0017R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0017R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u0011\u0010\u0011\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0017R\u0011\u0010\u0012\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b&\u0010$R\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0017¨\u0006>"}, d2 = {"Lcom/example/model/FullSettingsConfig;", "", "appName", "", "slogan", "aboutText", "contactQQ", "contactWechat", "contactAlipay", "qqGroupUrl", "qqGroupUin", "officialWebsite", "feedbackEmail", "logoUrl", "packageName", "securityEnabled", "", "securityExpectedSha", "serverShutdownEnabled", "serverShutdownNotice", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;ZLjava/lang/String;)V", "getAppName", "()Ljava/lang/String;", "getSlogan", "getAboutText", "getContactQQ", "getContactWechat", "getContactAlipay", "getQqGroupUrl", "getQqGroupUin", "getOfficialWebsite", "getFeedbackEmail", "getLogoUrl", "getPackageName", "getSecurityEnabled", "()Z", "getSecurityExpectedSha", "getServerShutdownEnabled", "getServerShutdownNotice", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class FullSettingsConfig {
    public static final int $stable = 0;
    private final String aboutText;
    private final String appName;
    private final String contactAlipay;
    private final String contactQQ;
    private final String contactWechat;
    private final String feedbackEmail;
    private final String logoUrl;
    private final String officialWebsite;
    private final String packageName;
    private final String qqGroupUin;
    private final String qqGroupUrl;
    private final boolean securityEnabled;
    private final String securityExpectedSha;
    private final boolean serverShutdownEnabled;
    private final String serverShutdownNotice;
    private final String slogan;

    public FullSettingsConfig() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, false, null, false, null, 65535, null);
    }

    public static /* synthetic */ FullSettingsConfig copy$default(FullSettingsConfig fullSettingsConfig, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, boolean z, String str13, boolean z2, String str14, int i, Object obj) {
        String str15 = (i & 1) != 0 ? fullSettingsConfig.appName : str;
        return fullSettingsConfig.copy(str15, (i & 2) != 0 ? fullSettingsConfig.slogan : str2, (i & 4) != 0 ? fullSettingsConfig.aboutText : str3, (i & 8) != 0 ? fullSettingsConfig.contactQQ : str4, (i & 16) != 0 ? fullSettingsConfig.contactWechat : str5, (i & 32) != 0 ? fullSettingsConfig.contactAlipay : str6, (i & 64) != 0 ? fullSettingsConfig.qqGroupUrl : str7, (i & 128) != 0 ? fullSettingsConfig.qqGroupUin : str8, (i & 256) != 0 ? fullSettingsConfig.officialWebsite : str9, (i & 512) != 0 ? fullSettingsConfig.feedbackEmail : str10, (i & 1024) != 0 ? fullSettingsConfig.logoUrl : str11, (i & 2048) != 0 ? fullSettingsConfig.packageName : str12, (i & 4096) != 0 ? fullSettingsConfig.securityEnabled : z, (i & 8192) != 0 ? fullSettingsConfig.securityExpectedSha : str13, (i & 16384) != 0 ? fullSettingsConfig.serverShutdownEnabled : z2, (i & 32768) != 0 ? fullSettingsConfig.serverShutdownNotice : str14);
    }

    public final String component1() {
        return this.appName;
    }

    public final String component10() {
        return this.feedbackEmail;
    }

    public final String component11() {
        return this.logoUrl;
    }

    public final String component12() {
        return this.packageName;
    }

    public final boolean component13() {
        return this.securityEnabled;
    }

    public final String component14() {
        return this.securityExpectedSha;
    }

    public final boolean component15() {
        return this.serverShutdownEnabled;
    }

    public final String component16() {
        return this.serverShutdownNotice;
    }

    public final String component2() {
        return this.slogan;
    }

    public final String component3() {
        return this.aboutText;
    }

    public final String component4() {
        return this.contactQQ;
    }

    public final String component5() {
        return this.contactWechat;
    }

    public final String component6() {
        return this.contactAlipay;
    }

    public final String component7() {
        return this.qqGroupUrl;
    }

    public final String component8() {
        return this.qqGroupUin;
    }

    public final String component9() {
        return this.officialWebsite;
    }

    public final FullSettingsConfig copy(String appName, String slogan, String aboutText, String contactQQ, String contactWechat, String contactAlipay, String qqGroupUrl, String qqGroupUin, String officialWebsite, String feedbackEmail, String logoUrl, String packageName, boolean z, String securityExpectedSha, boolean z2, String serverShutdownNotice) {
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(slogan, "slogan");
        Intrinsics.checkNotNullParameter(aboutText, "aboutText");
        Intrinsics.checkNotNullParameter(contactQQ, "contactQQ");
        Intrinsics.checkNotNullParameter(contactWechat, "contactWechat");
        Intrinsics.checkNotNullParameter(contactAlipay, "contactAlipay");
        Intrinsics.checkNotNullParameter(qqGroupUrl, "qqGroupUrl");
        Intrinsics.checkNotNullParameter(qqGroupUin, "qqGroupUin");
        Intrinsics.checkNotNullParameter(officialWebsite, "officialWebsite");
        Intrinsics.checkNotNullParameter(feedbackEmail, "feedbackEmail");
        Intrinsics.checkNotNullParameter(logoUrl, "logoUrl");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(securityExpectedSha, "securityExpectedSha");
        Intrinsics.checkNotNullParameter(serverShutdownNotice, "serverShutdownNotice");
        return new FullSettingsConfig(appName, slogan, aboutText, contactQQ, contactWechat, contactAlipay, qqGroupUrl, qqGroupUin, officialWebsite, feedbackEmail, logoUrl, packageName, z, securityExpectedSha, z2, serverShutdownNotice);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof FullSettingsConfig) {
            FullSettingsConfig fullSettingsConfig = (FullSettingsConfig) obj;
            return Intrinsics.areEqual(this.appName, fullSettingsConfig.appName) && Intrinsics.areEqual(this.slogan, fullSettingsConfig.slogan) && Intrinsics.areEqual(this.aboutText, fullSettingsConfig.aboutText) && Intrinsics.areEqual(this.contactQQ, fullSettingsConfig.contactQQ) && Intrinsics.areEqual(this.contactWechat, fullSettingsConfig.contactWechat) && Intrinsics.areEqual(this.contactAlipay, fullSettingsConfig.contactAlipay) && Intrinsics.areEqual(this.qqGroupUrl, fullSettingsConfig.qqGroupUrl) && Intrinsics.areEqual(this.qqGroupUin, fullSettingsConfig.qqGroupUin) && Intrinsics.areEqual(this.officialWebsite, fullSettingsConfig.officialWebsite) && Intrinsics.areEqual(this.feedbackEmail, fullSettingsConfig.feedbackEmail) && Intrinsics.areEqual(this.logoUrl, fullSettingsConfig.logoUrl) && Intrinsics.areEqual(this.packageName, fullSettingsConfig.packageName) && this.securityEnabled == fullSettingsConfig.securityEnabled && Intrinsics.areEqual(this.securityExpectedSha, fullSettingsConfig.securityExpectedSha) && this.serverShutdownEnabled == fullSettingsConfig.serverShutdownEnabled && Intrinsics.areEqual(this.serverShutdownNotice, fullSettingsConfig.serverShutdownNotice);
        }
        return false;
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((this.appName.hashCode() * 31) + this.slogan.hashCode()) * 31) + this.aboutText.hashCode()) * 31) + this.contactQQ.hashCode()) * 31) + this.contactWechat.hashCode()) * 31) + this.contactAlipay.hashCode()) * 31) + this.qqGroupUrl.hashCode()) * 31) + this.qqGroupUin.hashCode()) * 31) + this.officialWebsite.hashCode()) * 31) + this.feedbackEmail.hashCode()) * 31) + this.logoUrl.hashCode()) * 31) + this.packageName.hashCode()) * 31) + Boolean.hashCode(this.securityEnabled)) * 31) + this.securityExpectedSha.hashCode()) * 31) + Boolean.hashCode(this.serverShutdownEnabled)) * 31) + this.serverShutdownNotice.hashCode();
    }

    public String toString() {
        String str = this.appName;
        String str2 = this.slogan;
        String str3 = this.aboutText;
        String str4 = this.contactQQ;
        String str5 = this.contactWechat;
        String str6 = this.contactAlipay;
        String str7 = this.qqGroupUrl;
        String str8 = this.qqGroupUin;
        String str9 = this.officialWebsite;
        String str10 = this.feedbackEmail;
        String str11 = this.logoUrl;
        String str12 = this.packageName;
        boolean z = this.securityEnabled;
        String str13 = this.securityExpectedSha;
        boolean z2 = this.serverShutdownEnabled;
        return "FullSettingsConfig(appName=" + str + ", slogan=" + str2 + ", aboutText=" + str3 + ", contactQQ=" + str4 + ", contactWechat=" + str5 + ", contactAlipay=" + str6 + ", qqGroupUrl=" + str7 + ", qqGroupUin=" + str8 + ", officialWebsite=" + str9 + ", feedbackEmail=" + str10 + ", logoUrl=" + str11 + ", packageName=" + str12 + ", securityEnabled=" + z + ", securityExpectedSha=" + str13 + ", serverShutdownEnabled=" + z2 + ", serverShutdownNotice=" + this.serverShutdownNotice + ")";
    }

    public FullSettingsConfig(String appName, String slogan, String aboutText, String contactQQ, String contactWechat, String contactAlipay, String qqGroupUrl, String qqGroupUin, String officialWebsite, String feedbackEmail, String logoUrl, String packageName, boolean securityEnabled, String securityExpectedSha, boolean serverShutdownEnabled, String serverShutdownNotice) {
        Intrinsics.checkNotNullParameter(appName, "appName");
        Intrinsics.checkNotNullParameter(slogan, "slogan");
        Intrinsics.checkNotNullParameter(aboutText, "aboutText");
        Intrinsics.checkNotNullParameter(contactQQ, "contactQQ");
        Intrinsics.checkNotNullParameter(contactWechat, "contactWechat");
        Intrinsics.checkNotNullParameter(contactAlipay, "contactAlipay");
        Intrinsics.checkNotNullParameter(qqGroupUrl, "qqGroupUrl");
        Intrinsics.checkNotNullParameter(qqGroupUin, "qqGroupUin");
        Intrinsics.checkNotNullParameter(officialWebsite, "officialWebsite");
        Intrinsics.checkNotNullParameter(feedbackEmail, "feedbackEmail");
        Intrinsics.checkNotNullParameter(logoUrl, "logoUrl");
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(securityExpectedSha, "securityExpectedSha");
        Intrinsics.checkNotNullParameter(serverShutdownNotice, "serverShutdownNotice");
        this.appName = appName;
        this.slogan = slogan;
        this.aboutText = aboutText;
        this.contactQQ = contactQQ;
        this.contactWechat = contactWechat;
        this.contactAlipay = contactAlipay;
        this.qqGroupUrl = qqGroupUrl;
        this.qqGroupUin = qqGroupUin;
        this.officialWebsite = officialWebsite;
        this.feedbackEmail = feedbackEmail;
        this.logoUrl = logoUrl;
        this.packageName = packageName;
        this.securityEnabled = securityEnabled;
        this.securityExpectedSha = securityExpectedSha;
        this.serverShutdownEnabled = serverShutdownEnabled;
        this.serverShutdownNotice = serverShutdownNotice;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ FullSettingsConfig(java.lang.String r18, java.lang.String r19, java.lang.String r20, java.lang.String r21, java.lang.String r22, java.lang.String r23, java.lang.String r24, java.lang.String r25, java.lang.String r26, java.lang.String r27, java.lang.String r28, java.lang.String r29, boolean r30, java.lang.String r31, boolean r32, java.lang.String r33, int r34, kotlin.jvm.internal.DefaultConstructorMarker r35) {
        /*
            Method dump skipped, instructions count: 176
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.model.FullSettingsConfig.<init>(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, boolean, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final String getAppName() {
        return this.appName;
    }

    public final String getSlogan() {
        return this.slogan;
    }

    public final String getAboutText() {
        return this.aboutText;
    }

    public final String getContactQQ() {
        return this.contactQQ;
    }

    public final String getContactWechat() {
        return this.contactWechat;
    }

    public final String getContactAlipay() {
        return this.contactAlipay;
    }

    public final String getQqGroupUrl() {
        return this.qqGroupUrl;
    }

    public final String getQqGroupUin() {
        return this.qqGroupUin;
    }

    public final String getOfficialWebsite() {
        return this.officialWebsite;
    }

    public final String getFeedbackEmail() {
        return this.feedbackEmail;
    }

    public final String getLogoUrl() {
        return this.logoUrl;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final boolean getSecurityEnabled() {
        return this.securityEnabled;
    }

    public final String getSecurityExpectedSha() {
        return this.securityExpectedSha;
    }

    public final boolean getServerShutdownEnabled() {
        return this.serverShutdownEnabled;
    }

    public final String getServerShutdownNotice() {
        return this.serverShutdownNotice;
    }
}
