package com.example.viewmodel;

import com.example.model.ResourceButton;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: AdminViewModel.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.viewmodel.AdminViewModel$saveSoftware$1", f = "AdminViewModel.kt", i = {0, 0, 0, 0, 0, 0, 0, 0, 0}, l = {1047}, m = "invokeSuspend", n = {"json", "cleanUrl", "cleanApkUrl", "primaryLink", "finalMode", "finalApkUrl", "finalUrl", "swArr", "isFileExt"}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$6", "L$7", "I$0"})
/* loaded from: classes4.dex */
public final class AdminViewModel$saveSoftware$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ String $apkUrl;
    final /* synthetic */ String $author;
    final /* synthetic */ String $badge;
    final /* synthetic */ String $badgeType;
    final /* synthetic */ String $desc;
    final /* synthetic */ ResourceButton $existing;
    final /* synthetic */ String $iconUrl;
    final /* synthetic */ String $mode;
    final /* synthetic */ String $name;
    final /* synthetic */ String $previewUrl;
    final /* synthetic */ String $tags;
    final /* synthetic */ String $url;
    int I$0;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    Object L$4;
    Object L$5;
    Object L$6;
    Object L$7;
    int label;
    final /* synthetic */ AdminViewModel this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AdminViewModel$saveSoftware$1(AdminViewModel adminViewModel, String str, String str2, String str3, ResourceButton resourceButton, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, Continuation<? super AdminViewModel$saveSoftware$1> continuation) {
        super(2, continuation);
        this.this$0 = adminViewModel;
        this.$url = str;
        this.$apkUrl = str2;
        this.$mode = str3;
        this.$existing = resourceButton;
        this.$name = str4;
        this.$desc = str5;
        this.$author = str6;
        this.$badge = str7;
        this.$badgeType = str8;
        this.$tags = str9;
        this.$previewUrl = str10;
        this.$iconUrl = str11;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new AdminViewModel$saveSoftware$1(this.this$0, this.$url, this.$apkUrl, this.$mode, this.$existing, this.$name, this.$desc, this.$author, this.$badge, this.$badgeType, this.$tags, this.$previewUrl, this.$iconUrl, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((AdminViewModel$saveSoftware$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0455 A[Catch: Exception -> 0x049c, TRY_ENTER, TryCatch #4 {Exception -> 0x049c, blocks: (B:99:0x0449, B:102:0x0455, B:104:0x0494, B:103:0x0475), top: B:131:0x0449 }] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0475 A[Catch: Exception -> 0x049c, TryCatch #4 {Exception -> 0x049c, blocks: (B:99:0x0449, B:102:0x0455, B:104:0x0494, B:103:0x0475), top: B:131:0x0449 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00d5 A[Catch: Exception -> 0x04ae, TryCatch #2 {Exception -> 0x04ae, blocks: (B:16:0x005a, B:19:0x007f, B:21:0x008b, B:23:0x0093, B:28:0x009f, B:32:0x00b7, B:35:0x00c1, B:37:0x00d5, B:40:0x00e5, B:42:0x00ec, B:45:0x00f7, B:47:0x00fb, B:54:0x0110, B:56:0x0116, B:57:0x0120, B:50:0x0103, B:53:0x010e), top: B:127:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ec A[Catch: Exception -> 0x04ae, TryCatch #2 {Exception -> 0x04ae, blocks: (B:16:0x005a, B:19:0x007f, B:21:0x008b, B:23:0x0093, B:28:0x009f, B:32:0x00b7, B:35:0x00c1, B:37:0x00d5, B:40:0x00e5, B:42:0x00ec, B:45:0x00f7, B:47:0x00fb, B:54:0x0110, B:56:0x0116, B:57:0x0120, B:50:0x0103, B:53:0x010e), top: B:127:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00fa  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0101  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0103 A[Catch: Exception -> 0x04ae, TryCatch #2 {Exception -> 0x04ae, blocks: (B:16:0x005a, B:19:0x007f, B:21:0x008b, B:23:0x0093, B:28:0x009f, B:32:0x00b7, B:35:0x00c1, B:37:0x00d5, B:40:0x00e5, B:42:0x00ec, B:45:0x00f7, B:47:0x00fb, B:54:0x0110, B:56:0x0116, B:57:0x0120, B:50:0x0103, B:53:0x010e), top: B:127:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0116 A[Catch: Exception -> 0x04ae, TryCatch #2 {Exception -> 0x04ae, blocks: (B:16:0x005a, B:19:0x007f, B:21:0x008b, B:23:0x0093, B:28:0x009f, B:32:0x00b7, B:35:0x00c1, B:37:0x00d5, B:40:0x00e5, B:42:0x00ec, B:45:0x00f7, B:47:0x00fb, B:54:0x0110, B:56:0x0116, B:57:0x0120, B:50:0x0103, B:53:0x010e), top: B:127:0x005a }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0154  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x027a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03d3  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x043c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x043d  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r41) {
        /*
            Method dump skipped, instructions count: 1240
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.viewmodel.AdminViewModel$saveSoftware$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
